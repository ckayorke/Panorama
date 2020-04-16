/**
 * 
 */
package com.bosch.mtprotocol;


/**
 * @author AndrejsC
 *
 */
public interface MtProtocol {

	/**
	 * Initialize protocol
	 * @param connection
	 */
	public void initialize(MtConnection connection);
	
	/**
	 * Cancel all transactions and free resources
	 */
	public void destroy();
	
	/**
	 * Send message to connected device
	 * @param message
	 */
	public void sendMessage(MtMessage message);
	
	/**
	 * Adds an observer
	 * @param observer Object registering as an observer.
	 */
	public void addObserver(MTProtocolEventObserver observer);
	
	/**
	 * Removes observer
	 * @param observer The observer to remove
	 */
	public void removeObserver(MTProtocolEventObserver observer);

	/**
	 * To reset protocol on error event
	 */
	public void reset();

	/**
	 * Set timeout for missing response
	 * @param timeout
	 */
	public void setTimeout(int timeout);
	
	public interface MTProtocolEventObserver{
		public void onEvent(MTProtocolEvent event);
	}
	
	public interface MTProtocolEvent{}
}
