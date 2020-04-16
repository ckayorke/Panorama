/**
 * 
 */
package com.bosch.mtprotocol.util.statemachine.impl;

/**
 * @author AndrejsC
 *
 */
public class Transition {

	private String source;
	private String target;
	private String event;

	/**
	 * @param source
	 * @param target
	 * @param event
	 */
	public Transition(String source, String target, String event) {
		super();
		this.source = source;
		this.target = target;
		this.event = event;
	}
	
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

}
