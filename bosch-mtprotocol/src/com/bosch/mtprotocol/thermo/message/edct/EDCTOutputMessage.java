package com.bosch.mtprotocol.thermo.message.edct;

import com.bosch.mtprotocol.MtMessage;

public class EDCTOutputMessage implements MtMessage {
	// syncControl header (bit 0)
	public static final int MODE_AUTOSYNC_CONTROL_ON = 1;
	public static final int MODE_AUTOSYNC_CONTROL_OFF = 0;

	// remoteMode header (bit 7 down to 2)
	public static final int REMOTE_NO_ACTION = 0; // default header send from app to device if no command necessary
	public static final int REMOTE_SET_PING_CYCLE = 1; // set ping cycle with length given in remoteCtrlData

	// remoteCtrlData header
	public static final int REMOTE_CTRL_NO_DATA = 0;
	
	private int syncControl;
	private int remoteMode;
	private int remoteCtrlData;

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
	 * @return the devMode
	 */
	public int getRemoteMode() {
		return remoteMode;
	}
	/**
	 * @param devMode the devMode to set
	 */
	public void setRemoteMode(int devMode) {
		this.remoteMode = devMode;
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
