package com.bosch.mtprotocol.rotation.message.access_lock;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message for command 104
 * Set Access Lock
 * 
 * @author tos2si1
 */
public class AccessLockOutputMessage implements MtMessage {
	
	// TODO: define valid states for accessLock
	
	private int accessLock;

	/**
	 * @return the accessLock
	 */
	public int getAccessLock() {
		return accessLock;
	}

	/**
	 * @param accessLock the accessLock to set
	 */
	public void setAccessLock(int accessLock) {
		this.accessLock = accessLock;
	}
	
	public String toString() {
		return "AccessLockOutputMessage: [accessLock=" + accessLock + "]";
	}

}
