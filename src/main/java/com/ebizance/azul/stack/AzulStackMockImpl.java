package com.ebizance.azul.stack;

import org.apache.log4j.Logger;

public class AzulStackMockImpl extends AzulStack {
	
    private static final Logger logger = Logger.getLogger(AzulStackMockImpl.class);

	public AzulStackMockImpl(String filePath)
	{
		super(filePath);
	}

	@Override
	public boolean doParseFile() {
		return true;
	}
	
	@Override
	public boolean doParseThread() {
		return true;
	}

	@Override
	public boolean doParseStackTrace() {
		return true;
	}

	@Override
	public boolean doParseStackFrame() {
		return true;
	}

	@Override
	public boolean doParseMethodInfo() {
		return true;
	}
	
	
}
