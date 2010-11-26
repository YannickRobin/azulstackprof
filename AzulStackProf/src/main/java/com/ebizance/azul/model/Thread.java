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
	private int state;
	
	public static final int STATE_RUNNING=0;
	public static final int STATE_SLEEPING=1;
	public static final int STATE_IO_WAIT=2;
	public static final int STATE_WAITING_ON_MONITOR=3;
	public static final int STATE_ACQUIRING_MONITOR=4;
	public static final int STATE_ACQUIRING_RELEASING=5;
	public static final int STATE_WAITING_WEBLOGIC_SOCKET_MUXER=6;
	public static final int STATE_UNKNOWN=7;
	
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setState(String sState) {	
		if (sState.equals("running"))
			state=STATE_RUNNING;
		else if (sState.equals("sleeping"))
			state=STATE_SLEEPING;
		else if (sState.equals("I/O wait"))
			state=STATE_IO_WAIT;		
		else if (sState.equals("waiting on monitor"))
			state=STATE_WAITING_ON_MONITOR;		
		else if (sState.equals("acquiring monitor"))
			state=STATE_ACQUIRING_MONITOR;		
		else if (sState.startsWith("acquiring and releasing"))
			state=STATE_ACQUIRING_RELEASING;
		else if (sState.equals("waiting for Weblogic socket muxer"))
			state=STATE_WAITING_WEBLOGIC_SOCKET_MUXER;		
		else
			state=STATE_UNKNOWN;
	}	
	
}
