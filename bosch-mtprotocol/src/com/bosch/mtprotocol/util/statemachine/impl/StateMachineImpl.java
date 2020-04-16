package com.bosch.mtprotocol.util.statemachine.impl;

import com.bosch.mtprotocol.util.statemachine.Descriptor;
import com.bosch.mtprotocol.util.statemachine.StateMachine;
import com.bosch.mtprotocol.util.statemachine.exc.EventNotDefinedException;
import com.bosch.mtprotocol.util.statemachine.exc.StateMachineDescriptorException;

/**
 * @author AndrejsC
 * 
 */
public class StateMachineImpl implements StateMachine {

	private String currentState;
	private Descriptor descriptor;

	/**
	 * @param descriptor descriptor
	 */
	public StateMachineImpl(Descriptor descriptor) {
		super();
		this.descriptor = descriptor;
		this.currentState = descriptor.getStartState();
	}

	/* (non-Javadoc)
	 * @see de.bde.bosch.mtprotocol.statemachine.StateMachine#setCurrentState(java.lang.String)
	 */
	@Override
	public void setCurrentState(String state) {
		this.currentState = state;
	}

	/* (non-Javadoc)
	 * @see de.bode.bosch.mtprotocolstatemachine.StateMachine#getCurrentState()
	 */
	@Override
	public String getCurrentState() {
		return currentState;
	}


	/* (non-Javadoc)
	 * @see de.bosde.bosch.mtprotocoltatemachine.StateMachine#getDescriptor()
	 */
	@Override
	public Descriptor getDescriptor() {
		return descriptor;
	}


	/* (non-Javadoc)
	 * @see de.boscde.bosch.mtprotocolatemachine.StateMachine#processEvent(java.lang.String)
	 */
	@Override
	public void processEvent(String event) throws StateMachineDescriptorException {

		if (!descriptor.isEvent(event)){
			throw new EventNotDefinedException("Event " + event + " not defined");
		}

		String source = getCurrentState();
		String target = descriptor.getTargetState(source, event);
		setCurrentState(target);
	}

}
