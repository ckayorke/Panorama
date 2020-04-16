/**
 * 
 */
package com.bosch.mtprotocol.glm100C.state;

import com.bosch.mtprotocol.util.statemachine.Descriptor;
import com.bosch.mtprotocol.util.statemachine.exc.StateMachineDescriptorException;
import com.bosch.mtprotocol.util.statemachine.impl.DescriptorImpl;
import com.bosch.mtprotocol.util.statemachine.impl.StateMachineImpl;



/**
 * @author AndrejsC
 *
 */
public class MtProtocolStateMachine extends StateMachineImpl implements MtProtocolStates{

	private static Descriptor descriptor;
	
	public MtProtocolStateMachine() {
		super(getOrCreateDescriptor());
	}

	private synchronized static Descriptor getOrCreateDescriptor(){
		if(descriptor == null){
			descriptor = new DescriptorImpl();
			
			// Events
			descriptor.defineEvent(EVENT_INITIALIZE_MASTER);
			descriptor.defineEvent(EVENT_INITIALIZE_SLAVE);
			descriptor.defineEvent(EVENT_SWITCH_TO_SLAVE);
			descriptor.defineEvent(EVENT_SWITCH_TO_MASTER);
			descriptor.defineEvent(EVENT_SEND_START);
			descriptor.defineEvent(EVENT_SEND_FINISH);
			descriptor.defineEvent(EVENT_RECEIVE_START);
			descriptor.defineEvent(EVENT_RECEIVE_FINISH);

			descriptor.defineEvent(EVENT_RESEND);
			descriptor.defineEvent(EVENT_SEND_ERROR_FRAME);
			descriptor.defineEvent(EVENT_SET_TIMEOUT);
			
			descriptor.defineEvent(EVENT_RESET);

			// States
			try {
				descriptor.defineState(STATE_NOT_INITIALIZED, true);

				descriptor.defineState(STATE_MASTER_READY);
				descriptor.defineState(STATE_MASTER_SENDING);
				descriptor.defineState(STATE_MASTER_RECEIVING);

				descriptor.defineState(STATE_SLAVE_LISTENING);
				descriptor.defineState(STATE_SLAVE_RECEIVING);

				// Transitions
				descriptor.defineTransition(STATE_NOT_INITIALIZED, STATE_MASTER_READY, EVENT_INITIALIZE_MASTER);
				descriptor.defineTransition(STATE_NOT_INITIALIZED, STATE_SLAVE_LISTENING, EVENT_INITIALIZE_SLAVE);

				descriptor.defineTransition(STATE_MASTER_READY, STATE_SLAVE_LISTENING, EVENT_SWITCH_TO_SLAVE);
				descriptor.defineTransition(STATE_SLAVE_LISTENING, STATE_MASTER_READY, EVENT_SWITCH_TO_MASTER);

				descriptor.defineTransition(STATE_MASTER_READY, STATE_MASTER_SENDING, EVENT_SEND_START);
				descriptor.defineTransition(STATE_MASTER_SENDING, STATE_MASTER_RECEIVING, EVENT_SEND_FINISH);

				descriptor.defineTransition(STATE_SLAVE_LISTENING, STATE_SLAVE_RECEIVING, EVENT_RECEIVE_START);
				descriptor.defineTransition(STATE_SLAVE_RECEIVING, STATE_SLAVE_LISTENING, EVENT_RECEIVE_FINISH);
				descriptor.defineTransition(STATE_MASTER_RECEIVING, STATE_MASTER_READY, EVENT_RECEIVE_FINISH);

				descriptor.defineTransition(STATE_MASTER_RECEIVING, STATE_MASTER_SENDING, EVENT_RESEND);

				descriptor.defineTransition(STATE_MASTER_SENDING, STATE_MASTER_READY, EVENT_SET_TIMEOUT);
				descriptor.defineTransition(STATE_MASTER_RECEIVING, STATE_MASTER_READY, EVENT_SET_TIMEOUT);
				descriptor.defineTransition(STATE_SLAVE_RECEIVING, STATE_SLAVE_LISTENING, EVENT_SET_TIMEOUT);
				
				

				descriptor.defineTransition(STATE_SLAVE_LISTENING, STATE_NOT_INITIALIZED, EVENT_RESET);
				descriptor.defineTransition(STATE_MASTER_READY, STATE_NOT_INITIALIZED, EVENT_RESET);
			} catch (StateMachineDescriptorException e) {
				throw new RuntimeException("Can't define a state machine", e);
			}
			
		}
		
		return descriptor;
	}

	
	
	
}
