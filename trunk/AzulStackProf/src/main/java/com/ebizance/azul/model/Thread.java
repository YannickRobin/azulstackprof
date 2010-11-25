package com.ebizance.azul.model;

/**
 * This is a POJO representing the current parsed thread.<br/>
 * This object is loaded into drools engine so it can be used
 * to filter threads to parse (e.g.: filter by name).
 * 
 * @author Yannick Robin
 */

public class Thread {
	private String name;
	private boolean continueParsing;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getContinueParsing() {
		return continueParsing;
	}

	public void setContinueParsing(boolean continueParsing) {
		this.continueParsing = continueParsing;
	}

}
