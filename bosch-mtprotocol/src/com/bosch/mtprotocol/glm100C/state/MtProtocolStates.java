/**
 * 
 */
package com.bosch.mtprotocol.glm100C.state;

/**
 * @author AndrejsC
 *
 */
public interface MtProtocolStates {

	public static String STATE_NOT_INITIALIZED = "NOT_INITIALIZED";

	// Master states
	public static String STATE_MASTER_READY = "MASTER_READY";
	public static String STATE_MASTER_SENDING = "MASTER_SENDING";
	public static String STATE_MASTER_RECEIVING = "MASTER_RECEIVING";

	// Slave states
	public static String STATE_SLAVE_LISTENING = "SLAVE_LISTENING";
	public static String STATE_SLAVE_SENDING = "SLAVE_SENDING";
	public static String STATE_SLAVE_RECEIVING = "SLAVE_RECEIVING";

	// Error state

	// ============================
	// Events
	// ============================
	public static String EVENT_INITIALIZE_MASTER = "INITIALIZE_MASTER";
	public static String EVENT_INITIALIZE_SLAVE = "INITIALIZE_SLAVE";

	public static String EVENT_SWITCH_TO_MASTER = "SWITCH_TO_MASTER";
	public static String EVENT_SWITCH_TO_SLAVE = "SWITCH_TO_SLAVE";

	public static String EVENT_SEND_START = "SEND_START";
	public static String EVENT_SEND_FINISH = "SEND_FINISH";

	public static String EVENT_RECEIVE_START = "RECEIVE_START";
	public static String EVENT_RECEIVE_FINISH = "RECEIVE_FINISH";

	// Errors
	public static String EVENT_RESEND = "RESEND";
	public static String EVENT_SEND_ERROR_FRAME = "SEND_ERROR_FRAME";
	public static String EVENT_SET_TIMEOUT = "SET_TIMEOUT";

	public static String EVENT_RESET = "RESET";
	
}
