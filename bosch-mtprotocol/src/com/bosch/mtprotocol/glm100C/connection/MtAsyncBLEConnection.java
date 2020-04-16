package com.bosch.mtprotocol.glm100C.connection;

/**
 * @author tos2si1
 *
 */
public interface MtAsyncBLEConnection extends MtAsyncConnection {
	
	/**
	 * @param recipient The recipient of the callback
	 */
	void addCallbackRecipient(MTAsyncBLEConnectionCallbackRecipient recipient);
	
	/**
	 * @param recipient The recipient of the callback
	 */
	void removeCallbackRecipient(MTAsyncBLEConnectionCallbackRecipient recipient);

	interface MTAsyncBLEConnectionCallbackRecipient {
		/**
		 * @param data incoming raw data
		 */
		void onBLECharacteristicChanged(byte[] data);

		/**
		 *
		 */
		void onBLEWriteFinished(int status);
	}
}
