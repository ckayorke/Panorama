/**
 * 
 */
package com.bosch.mtprotocol.util.statemachine.impl;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author AndrejsC
 *
 */
public class State implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5710568557182282484L;
	private String name;
	private HashMap<String, String> transitions;

	public State(String name) {
		this.name = name;
		this.transitions = new HashMap<String, String>();
	}

	public void defineTransition(String sourceState, String targetState,
			String event) {
		if (!transitions.containsKey(event))
			transitions.put(event, targetState);
	}

	public HashMap<String, String> getTransitions() {
		return this.transitions;
	}

	public String toString() {
		return name;
	}
}
