package com.bosch.mtprotocol.util.statemachine.exc;

/**
 * @author AndrejsC
 *
 */
public class StateNotDefinedException extends StateMachineDescriptorException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2033948758239281415L;

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public StateNotDefinedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 */
	public StateNotDefinedException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public StateNotDefinedException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	

	
	
}
