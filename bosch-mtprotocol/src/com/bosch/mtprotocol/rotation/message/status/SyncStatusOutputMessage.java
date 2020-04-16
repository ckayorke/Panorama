package com.bosch.mtprotocol.rotation.message.status;

import com.bosch.mtprotocol.MtMessage;

/**
 * Output message for command 100
 * Synchronize status
 * 
 * @author tos2si1
 */
public class SyncStatusOutputMessage implements MtMessage {
	
	// syncControl header
	public static final int MODE_AUTOSYNC_CONTROL_ON = 1;
	public static final int MODE_AUTOSYNC_CONTROL_OFF = 0;
	
	private int syncControl;

	/**
	 * @return the syncControl
	 */
	public int getSyncControl() {
		return syncControl;
	}

	/**
	 * @param syncControl the syncControl to set
	 */
	public void setSyncControl(int syncControl) {
		this.syncControl = syncControl;
	}
	
	public String toStirng() {
		return "SyncStatusOutputMessage: [syncControl=" + syncControl + "]";
	}

}
