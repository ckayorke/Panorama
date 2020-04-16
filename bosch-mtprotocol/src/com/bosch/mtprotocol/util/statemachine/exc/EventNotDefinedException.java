package com.bosch.mtprotocol.util.statemachine.exc;

/**
 * @author AndrejsC
 *
 */
public class EventNotDefinedException extends StateMachineDescriptorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2540686146474967443L;

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public EventNotDefinedException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 */
	public EventNotDefinedException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public EventNotDefinedException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	

	
	
}
