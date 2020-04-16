/**
 * 
 */
package com.bosch.mtprotocol.util.statemachine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import com.bosch.mtprotocol.util.statemachine.Descriptor;
import com.bosch.mtprotocol.util.statemachine.exc.StateMachineDescriptorException;
import com.bosch.mtprotocol.util.statemachine.exc.StateNotDefinedException;
import com.bosch.mtprotocol.util.statemachine.exc.TransitionNotDefinedException;




/**
 * @author AndrejsC
 *
 */
public class DescriptorImpl implements Descriptor {

	private final static Logger LOG = Logger.getLogger(DescriptorImpl.class.getName());
	
	private String startState;

	private HashMap<String, State> states;
	private HashSet<String> events;

	public DescriptorImpl() {
		this.states = new HashMap<String, State>();
		this.events = new HashSet<String>();
	}

	/* (non-Javadoc)
	 * @see de.bde.bosch.mtprotocol.statemachine.Descriptor#defineState(java.lang.String)
	 */
	@Override
	public void defineState(String state) throws StateMachineDescriptorException {
		this.defineState(state, false);
	}

	/* (non-Javadoc)
	 * @see de.bode.bosch.mtprotocolstatemachine.Descriptor#defineState(java.lang.String, boolean, boolean)
	 */
	@Override
	public void defineState(String state, boolean isStart) throws StateMachineDescriptorException {
		if (state == null)
			throw new IllegalArgumentException("Can not define a state with null value");

		if (isStart && startState != null){
			throw new StateMachineDescriptorException("Cannot define state "
					+ state + " as start state because " + startState
					+ " is already defined as the one");
		}

		if (!states.containsKey(state))
			states.put(state, new State(state));

		LOG.finest("#defineState succeed for state id " + state);

		if (isStart)
			this.startState = state;

	}

	/* (non-Javadoc)
	 * @see de.bosde.bosch.mtprotocoltatemachine.Descriptor#isState(java.lang.String)
	 */
	@Override
	public boolean isState(String state) {
		return states.containsKey(state);
	}

	/* (non-Javadoc)
	 * @see de.boscde.bosch.mtprotocolatemachine.Descriptor#getStates()
	 */
	@Override
	public List<String> getStates() {
		ArrayList<String> result = new ArrayList<String>();
		for (String key : states.keySet())
			result.add(key);
		return result;
	}

	/* (non-Javadoc)
	 * @see de.boschde.bosch.mtprotocoltemachine.Descriptor#getStartState()
	 */
	@Override
	public String getStartState() {
		return startState;
	}

	/* (non-Javadoc)
	 * @see de.bosch.de.bosch.mtprotocolemachine.Descriptor#defineEvent(java.lang.String)
	 */
	@Override
	public void defineEvent(String event) {
		if (event == null)
			throw new IllegalArgumentException(
					"Can not define an event with null value");

		events.add(event);

		LOG.finest("#defineEvent succeed for event id " + event);

	}

	/* (non-Javadoc)
	 * @see de.bosch.mde.bosch.mtprotocolmachine.Descriptor#isEvent(java.lang.String)
	 */
	@Override
	public boolean isEvent(String event) {
		return events.contains(event);
	}

	/* (non-Javadoc)
	 * @see de.bosch.mtde.bosch.mtprotocolachine.Descriptor#getEvents()
	 */
	@Override
	public List<String> getEvents() {
		ArrayList<String> copy = new ArrayList<String>();
        for (String evt : events)
            copy.add(evt);

        return copy;
	}

	/* (non-Javadoc)
	 * @see de.bosch.mtpde.bosch.mtprotocolchine.Descriptor#getEvents(java.lang.String)
	 */
	@Override
	public List<String> getEvents(String state) {
		List<String> result = Collections.emptyList();

        if (this.isState(state)) {
            HashMap<String, String> transitions = states.get(state).getTransitions();
            for (String key : transitions.keySet())
                result.add(key);
        }

        return result;
	}

	/* (non-Javadoc)
	 * @see de.bosch.mtprde.bosch.mtprotocolhine.Descriptor#defineTransition(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void defineTransition(String sourceState, String targetState,
			String event) throws StateMachineDescriptorException {
		State source = states.get(sourceState);
        if (source == null)
            throw new StateNotDefinedException(
                    "Cannot define a transition for a source state "
                            + sourceState + " that doesn't exist");

        if (!states.containsKey(targetState))
            throw new StateNotDefinedException(
                    "Cannot define a transition for a target state "
                            + targetState + " that doesn't exist");

        if (!events.contains(event))
            throw new StateNotDefinedException(
                    "Cannot define a transition for an event " + event
                            + " that doesn't exist");

        source.defineTransition(sourceState, targetState, event);

	}

	/* (non-Javadoc)
	 * @see de.bosch.mtprode.bosch.mtprotocoline.Descriptor#getTargetState(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTargetState(String source, String event) throws StateNotDefinedException, TransitionNotDefinedException {
		State src = this.states.get(source);
        if (src == null)
        	  throw new StateNotDefinedException("State " + source + " not defined");
        
        HashMap<String, String> txs = src.getTransitions();
        String target = txs.get(event);
        if (target == null)
            throw new TransitionNotDefinedException("Transition from state "
                    + source + " with event " + event + " not defined");

        return target;
	}

}
