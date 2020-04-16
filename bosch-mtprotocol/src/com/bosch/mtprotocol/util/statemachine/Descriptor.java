/**
 * 
 */
package com.bosch.mtprotocol.util.statemachine;

import java.util.List;

import com.bosch.mtprotocol.util.statemachine.exc.StateMachineDescriptorException;
import com.bosch.mtprotocol.util.statemachine.exc.StateNotDefinedException;
import com.bosch.mtprotocol.util.statemachine.exc.TransitionNotDefinedException;




/**
 * @author AndrejsC
 * 
 * Contains a set of states and a set of transitions between states
 */
public interface Descriptor {

	/**
	 * Define the state in order to define transitions later on
	 * @param state the name of the state
	 * @throws StateMachineDescriptorException
	 */
	public void defineState(String state)
			throws StateMachineDescriptorException;

	/**
	 * Define the state in order to define transitions later on
	 * @param state the name of the state
	 * @param isStart the state is a starting point
	 * @throws StateMachineDescriptorException
	 */
	public void defineState(String state, boolean isStart)
			throws StateMachineDescriptorException;


	/**
	 * Define a transition between states
	 * @param sourceState
	 * @param targetState
	 * @param event
	 * @throws StateNotDefinedException
	 * @throws StateMachineDescriptorException
	 */
	public void defineTransition(String sourceState, String targetState,
			String event) throws StateNotDefinedException,
			StateMachineDescriptorException;
	
	/**
	 * @param state
	 * @return
	 */
	public boolean isState(String state);

	/**
	 * @return
	 */
	public List<String> getStates();

	/**
	 * @return
	 */
	public String getStartState();

	/**
	 * Define the event in order to define transitions later on
	 * @param event
	 */
	public void defineEvent(String event);

	/**
	 * @param event
	 * @return
	 */
	public boolean isEvent(String event);

	/**
	 * @return
	 */
	public List<String> getEvents();

	/**
	 * @param state
	 * @return
	 */
	public List<String> getEvents(String state);

	/**
	 * @param source
	 * @param event
	 * @return
	 * @throws StateNotDefinedException
	 * @throws TransitionNotDefinedException
	 */
	public String getTargetState(String source, String event)
			throws StateNotDefinedException, TransitionNotDefinedException;

}
