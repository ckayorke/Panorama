/**
 * 
 */
package com.bosch.mtprotocol.util.statemachine.exc;

/**
 * @author AndrejsC
 *
 */
public class StateMachineException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6786172350229698999L;

	/**
	 * @param detailMessage
	 */
	public StateMachineException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public StateMachineException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public StateMachineException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	
}
