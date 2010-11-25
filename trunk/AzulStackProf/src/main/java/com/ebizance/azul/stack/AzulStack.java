package com.ebizance.azul.stack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ebizance.azul.model.Header;
import com.ebizance.azul.model.Thread;

/**
 * This abstract class is responsible for the stack file parsing.<br/>
 * It browses thread > stack_trace > stack_frame > method_info.<p/>
 * 
 * At each steps, it calls the abstract method doParse<i>TheStep</i>().<br/>
 * So far, we propose two implementation:<br/>
 * - {@link AzulStackMockImpl}, for testing <br/>
 * - {@link AzulStackDroolsImpl}, for filtering based on rules (Drools engine) <br/>
 *  
 * @author Yannick Robin
 * 
 * @see AzulStackMockImpl
 * @see AzulStackDroolsImpl
 * 
 */

public abstract class AzulStack {
	
	protected Header header;
    protected Thread thread;
    
    private static final Logger logger = Logger.getLogger(AzulStack.class);
    private int threadCounter;
    private Map<String, Integer> methods = new HashMap<String, Integer>();
    private Document doc;
    
	public AzulStack(String filePath)
	{
		header = new Header();
		doc = parseXmlFile(filePath, false);
	}
	
    private Document parseXmlFile(String filePath, boolean validating) {
		try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validating);

            // Create the builder and parse the file
            File file = new File(filePath);
            header.setFileName(file.getName());
            header.setFileSize(file.length()/1024);
            Document doc = factory.newDocumentBuilder().parse(file);
            return doc;
        } catch (SAXException e) {
            // A parsing error occurred; the xml input is not valid
        	logger.error("SAXException", e);
        } catch (ParserConfigurationException e) {
        	logger.error("ParserConfigurationException", e);
        } catch (IOException e) {
        	logger.error("IOException", e);
        }
        return null;
    }
	
    public void parse()
    {
    	setHeader();
		boolean doNotStop = doParseFile();
		if (doNotStop)
			parseFile();	
    }
    
	private void setHeader() {
		NodeList childNodes = doc.getElementsByTagName("server").item(0).getChildNodes();
		//Get thread name
		for (int i=0; i<childNodes.getLength(); i++)
		{
			Node childNode = childNodes.item(i);
			
			if (childNode.getNodeName().equals("date"))
			{
				String sDate = childNode.getTextContent();
				header.setDate(sDate);
			}
		}
	}    
    
	private void parseFile()
	{
		NodeList childNodes = doc.getElementsByTagName("thread");
		for (int i=0; i<childNodes.getLength(); i++)
		{
			threadCounter++;
			Node childNode = childNodes.item(i);
			setThread(childNode);
			boolean doNotStop = doParseThread();
			if (doNotStop)
				parseThread(childNode);
		}
	}
	
	private void setThread(Node currentChildNode) {
		thread = new Thread();
		NodeList childNodes = currentChildNode.getChildNodes();
		//Get thread name
		for (int i=0; i<childNodes.getLength(); i++)
		{
			Node childNode = childNodes.item(i);
			
			if (childNode.getNodeName().equals("name"))
				thread.setName(childNode.getTextContent());
		}
	}

	private void parseThread(Node currentChildNode)
	{
		NodeList childNodes = currentChildNode.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++)
		{
			Node childNode = childNodes.item(i);
			if (childNode.getNodeName().equals("stack_trace"))
			{
				boolean doNotStop = doParseStackTrace();
				if (doNotStop)
					parseStackTrace(childNode);
			}
		}
	}

	private void parseStackTrace(Node currentChildNode)
	{
		NodeList childNodes = currentChildNode.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++)
		{ 
			Node childNode = childNodes.item(i);
			if (childNode.getNodeName().equals("stack_frame"))
			{
				boolean doNotStop = doParseStackFrame();
				if (doNotStop)
					parseStackFrame(childNode);
			}
		}		
	}

	private void parseStackFrame(Node currentChildNode)
	{
		NodeList childNodes = currentChildNode.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++)
		{ 
			Node childNode = childNodes.item(i);
			if (childNode.getNodeName().equals("method_info"))
			{
				boolean doNotStop = doParseMethodInfo();
				if (doNotStop)
					parseMethodInfo(childNode);
			}
		}
	}
	
	private void parseMethodInfo(Node currentChildNode)
	{
		NodeList childNodes = currentChildNode.getChildNodes();
		String objectName = null;
		String lineInfo = null;
		
		for (int i=0; i<childNodes.getLength(); i++)
		{ 
			Node childNode = childNodes.item(i);
						
			if (objectName == null && childNode.getNodeName().equals("object_ref"))
			{
				objectName = getObjectName(childNode);
			}
			else if (lineInfo == null && childNode.getNodeName().equals("line_info"))
			{
				lineInfo = childNode.getTextContent();
			}			
		}
		
		if (lineInfo!= null)
		{
			String method = objectName + "(" +  lineInfo + ")";
			logger.info("method: " + method);
			Integer counter = (Integer)methods.get(method);
			if (counter == null)
				methods.put(method, 1);
			else
				methods.put(method, counter + 1);
		}
	}
	
	private String getObjectName(Node currentChildNode)
	{
		String lineInfo = null;
		NodeList childNodes = currentChildNode.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++)
		{ 
			Node childNode = childNodes.item(i);
			if (childNode.getNodeName().equals("name"))
			{
				lineInfo = childNode.getTextContent();
			}
		}
		return lineInfo;
	}

	public int getThreadCounter() {
		return threadCounter;
	}

	public Map<String, Integer> getMethods() {
		return methods;
	}

	public abstract boolean doParseFile();
	public abstract boolean doParseThread();
	public abstract boolean doParseStackTrace();
	public abstract boolean doParseStackFrame();
	public abstract boolean doParseMethodInfo();
}
