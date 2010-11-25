package com.ebizance.azul;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ebizance.azul.stack.AzulStack;
import com.ebizance.azul.stack.AzulStackDroolsImpl;
import com.ebizance.azul.stack.AzulStackMockImpl;

/**
 * Command line application to run AzulStackProf.<br/><br/>
 * 
 * <code>
 * Usage: mvn exec:java -Dexec.mainClass="com.ebizance.azul.AzulStackProf" -Dexec.args="[file|dir]"<br/><br/>
 * 
 *
 *
 * -Arguments-<br/>
 * file: Specify stack file path (e.g: C:/temp/rtpm_11212020/STACK.1.xml). The application will parse the indicated file only.<br/>
 * or<br/>
 * dir: Specify folder path (e.g: C:/temp/rtpm_11212020). The application will parse all stack files inside the indicated folder.<br/>
 * </code>
 * 
 * @author Yannick Robin
 *
 */
public class AzulStackProf 
{
	private static final Logger logger = Logger.getLogger(AzulStackProf.class);
	
	public static void main(String[] args)
    {    	
    	if (args.length == 0)
    	{
    		displayUsageMessage();
    		System.exit(-1);
    	}
    	
    	String path = args[0];
    	File file = new File(path);
    	if (!file.exists())
    	{
    		displayUsageMessage();
	    	System.exit(-1);
    	}
    	String[] stackFiles = null;
    	String dirpath = null;
    	
    	if (file.isFile())
    	{
    		dirpath = file.getParent();
    		stackFiles = new String[1];
    		stackFiles[0]=file.getName();
    	}
    	else if (file.isDirectory())
    	{
    		dirpath = path;
    		stackFiles = getStackFiles(path);
    	}
    	else
    	{
    		displayUsageMessage();
	    	System.exit(-1);
    	}
    		
    	Map<String,Integer> methods = null;
    	for (int i=0; i< stackFiles.length; i++)
    	{
    		AzulStack azulStack = new AzulStackDroolsImpl(dirpath + File.separator + stackFiles[i]);
    		azulStack.parse();
    		logger.info("Parsing " + stackFiles[i] + "...");
    		Map<String,Integer> methodsTemp = azulStack.getMethods();
    		if (methods == null)
    			methods = methodsTemp;
    		else
    			mergeMethods(methods, methodsTemp);		
    	}
    	methods = sortHashMapByValues(methods, false);
		displayMethods(methods);
    }

    private static void displayUsageMessage()
    {
    	System.out.println();
    	System.out.println("Usage: mvn exec:java -Dexec.mainClass=\"com.ebizance.azul.AzulStackProf\" -Dexec.args=\"[file|dir]\"");
    	System.out.println("\n-Arguments-");
    	System.out.println("file\t Specify a stack file path (e.g: C:/temp/rtpm_11212020/STACK.1.xml). The application will parse the indicated file only.");
    	System.out.println("or");
    	System.out.println("dir\t Specify a folder path (e.g: C:/temp/rtpm_11212020). The application will parse all stack files inside the indicated folder.");
    	System.out.println();
    }
    
    private static String[] getStackFiles(String dirPath)
    {
    	File dir = new File(dirPath);

    	String[] children = dir.list();
    	if (children == null) {
    	    // Either dir does not exist or is not a directory
    	} else {
    	    for (int i=0; i<children.length; i++) {
    	        // Get filename of file or directory
    	        String filename = children[i];
    	    }
    	}

    	//We filter STACK files
    	FilenameFilter filter = new FilenameFilter() {
    	    public boolean accept(File dir, String name) {    	    	
    	    	return name.startsWith("STACK.") && name.endsWith(".xml");
    	    }
    	};
    	
    	return dir.list(filter);
    }
    
    private static void mergeMethods(Map<String,Integer> methods1, Map<String,Integer> methods2)
    {        
    	for (Iterator <Map.Entry<String, Integer>> it=methods2.entrySet().iterator(); it.hasNext(); )
    	{
			Map.Entry<String, Integer> entry = it.next();
    		String method2 = entry.getKey();
    		Integer counter2 = entry.getValue();
    		
    		if (methods1.containsKey(method2))
    		{
    			Integer counter1 = methods1.get(method2);
    			methods1.put(method2, counter1 + counter2);
    		}
    		else
    		{
    			methods1.put(method2, counter2);
    		}
    	} 
    }

    private static LinkedHashMap sortHashMapByValues(Map passedMap, boolean ascending) {
    	List mapKeys = new ArrayList(passedMap.keySet());
    	List mapValues = new ArrayList(passedMap.values());
    	Collections.sort(mapValues);
    	Collections.sort(mapKeys);

    	if (!ascending)
    		Collections.reverse(mapValues);

    	LinkedHashMap someMap = new LinkedHashMap();
    	Iterator valueIt = mapValues.iterator();
    	while (valueIt.hasNext()) {
    		Object val = valueIt.next();
    		Iterator keyIt = mapKeys.iterator();
	    	while (keyIt.hasNext()) {
	    		Object key = keyIt.next();
	    		if (passedMap.get(key).toString().equals(val.toString())) {
	    			passedMap.remove(key);
	    			mapKeys.remove(key);
	    			someMap.put(key, val);
	    			break;
	    		}
    		}
    	}
    	return someMap;
    } 
    
    private static void displayMethods(final Map<String,Integer> methods)
    {
    	int i=1;
    	System.out.println("\n\n***Azul Stack Prof result***");
		System.out.println("rank\tmethod\tcount");
    	for (Iterator <Map.Entry<String, Integer>> it=methods.entrySet().iterator(); it.hasNext(); )
    	{
			Map.Entry<String, Integer> entry = it.next();
    		String method = entry.getKey();
    		Integer counter = entry.getValue();
    		System.out.println(i + "\t" + counter + "\t" + method);
    		i++;
    	} 
    }
    
}
