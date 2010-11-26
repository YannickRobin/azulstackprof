package com.ebizance.azul.stack;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This class implements {@link AzulStack}.<p/>
 * 
 * It uses Drools engine to decide the elements to parse (file, thread, ...).<br/>
 * To update the parsing rules, edit <code>AzulStackProf-Header.java.drl</code> and <code>AzulStackProf-Thread.java.drl</code>.<br/>
 * 
 * See <A HREF="http://downloads.jboss.com/drools/docs/5.1.1.34858.FINAL/drools-expert/html/ch02.html">Drools documentation</A>.
 * 
 * @author Yannick Robin
 * 
 * @see AzulStack
 * 
 */

public class AzulStackDroolsImpl extends AzulStack {
	
    private static final Logger logger = Logger.getLogger(AzulStackDroolsImpl.class);
    private KnowledgeBase kbase;
    
    {       
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();        
        kbuilder.add(ResourceFactory.newClassPathResource("AzulStackProf-Header.java.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newClassPathResource("AzulStackProf-Thread.java.drl"), ResourceType.DRL);
        
		if (kbuilder.hasErrors()) {
          System.err.println(kbuilder.getErrors().toString());
        }         

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());        
    }
    
    
	public AzulStackDroolsImpl(String filePath)
	{
		super(filePath);
	}

	@Override
	public boolean doParseFile() {
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.insert(header);
		ksession.fireAllRules();
		ksession.dispose();
		return header.getContinueParsing();
	}
	
	/**
	 * 
	 * @param currentChildNode
	 * @return continue parsing
	 */
	@Override
	public boolean doParseThread()
	{
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.insert(thread);
		ksession.fireAllRules();
		ksession.dispose();	
		return thread.getContinueParsing();
	}

	@Override
	public boolean doParseStackTrace() {
		/**@TODO*/
		return true;
	}

	@Override
	public boolean doParseStackFrame() {
		/**@TODO*/
		return true;
	}

	@Override
	public boolean doParseMethodInfo() {
		/**@TODO*/
		return true;
	}

}
