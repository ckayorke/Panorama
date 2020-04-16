package com.bosch.mtprotocol.glm100C.connection;

import com.bosch.mtprotocol.MtConnection;




/**
 * @author AndrejsC
 *
 */
public interface MtAsyncConnection extends MtConnection {

	public static final int STATE_NONE = 0;				// we're doing nothing
	public static final int STATE_CONNECTING = 1;		// initiating an outgoing connection
	public static final int STATE_CONNECTED = 2;		// connected to a remote device
	public static final int STATE_TIMEOUT = 3;			// timeout during connection opening
	
	/**
	 * @return
	 */
	public int getState();
	
	/**
	 * @param observer
	 */
	public void addObserver(MTAsyncConnectionObserver observer);
	
	/**
	 * @param observer
	 */
	public void removeObserver(MTAsyncConnectionObserver observer);
	

	public interface MTAsyncConnectionObserver{
		/**
		 * @param connection
		 */
		public void onConnectionStateChanged(MtAsyncConnection connection);
	}
}
