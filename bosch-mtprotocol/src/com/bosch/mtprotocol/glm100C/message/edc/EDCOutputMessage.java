package com.bosch.mtprotocol.glm100C.message.edc;

import com.bosch.mtprotocol.MtMessage;

/**
 * @author tos2si1
 *
 */
public class EDCOutputMessage implements MtMessage {
	// syncControl header (bit 0)
	public static final int MODE_AUTOSYNC_CONTROL_ON = 1;
	public static final int MODE_AUTOSYNC_CONTROL_OFF = 0;

	// keypadBypass header (bit 1)
	public static final int ENABLE_KEYPAD_BYPASS = 1;
	public static final int DISABLE_KEYPAD_BYPASS = 0;

	// devMode header (bit 7 down to 2)
	public static final int READ_ONLY_MODE = 0; // default header send from app to device to start sync
	// further values in devMode header (1 to 10 and 63) relevant only for remote mode and testing
	public static final int SINGLE_DISTANCE_REQUEST = 1;
	// TODO: insert all valid values
	
	// remoteCtrlData header
	public static final int REMOTE_CTRL_DATA = 0; //unused
	
	private int syncControl;
	private int keypadBypass;
	private int devMode;
	private int remoteCtrlData; // unused
	
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
	/**
	 * @return the keypadBypass
	 */
	public int getKeypadBypass() {
		return keypadBypass;
	}
	/**
	 * @param keypadBypass the keypadBypass to set
	 */
	public void setKeypadBypass(int keypadBypass) {
		this.keypadBypass = keypadBypass;
	}
	/**
	 * @return the devMode
	 */
	public int getDevMode() {
		return devMode;
	}
	/**
	 * @param devMode the devMode to set
	 */
	public void setDevMode(int devMode) {
		this.devMode = devMode;
	}
	/**
	 * @return the remoteCtrlData
	 */
	public int getRemoteCtrlData() {
		return remoteCtrlData;
	}
	/**
	 * @param remoteCtrlData the remoteCtrlData to set
	 */
	public void setRemoteCtrlData(int remoteCtrlData) {
		this.remoteCtrlData = remoteCtrlData;
	}
}
