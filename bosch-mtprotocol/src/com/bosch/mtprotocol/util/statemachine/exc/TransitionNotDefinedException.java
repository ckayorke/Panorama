package com.bosch.mtprotocol.util.statemachine.exc;

/**
 * @author AndrejsC
 *
 */
public class TransitionNotDefinedException extends StateMachineDescriptorException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8864557668344148316L;

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public TransitionNotDefinedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 */
	public TransitionNotDefinedException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public TransitionNotDefinedException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	

	
	
}
