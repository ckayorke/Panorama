/**
 * 
 */
package com.bosch.mtprotocol;

import java.io.IOException;

/**
 * @author AndrejsC
 *
 */
public interface MtConnection {
	
	/**
	 * @return
	 */
	public boolean isOpen();
	
	/**
	 *  Opens a connection to MT device
	 */
	public void openConnection();
	
	/**
	 * Closes connection and releases all resources associated with the connection
	 */
	public void closeConnection();
	
	/**
	 * Reads bytes from connected device and stores them into the buffer array.
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	public int read(byte[] buffer) throws IOException;
	
	/**
	 * @param data
	 * @throws IOException
	 */
	public void write(byte[] data) throws IOException;
}
