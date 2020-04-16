/**
 * 
 */
package com.bosch.mtprotocol.util.statemachine;

import com.bosch.mtprotocol.util.statemachine.exc.StateMachineDescriptorException;





/**
 * @author AndrejsC
 *
 */

public interface StateMachine {

    /**
     * @return current state
     */
    public String getCurrentState();

    /**
     * @return a descriptor of state machine
     */
    public Descriptor getDescriptor();

    /**
     * Modifies the current state
     * @param state
     */
    public void setCurrentState(String state);

    
    /**
     * @param event
     * @throws StateMachineDescriptorException
     */
    public void processEvent(String event) throws StateMachineDescriptorException;
}
