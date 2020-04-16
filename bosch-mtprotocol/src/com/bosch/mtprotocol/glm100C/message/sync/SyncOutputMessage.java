package com.bosch.mtprotocol.glm100C.message.sync;

import com.bosch.mtprotocol.MtMessage;



public class SyncOutputMessage implements MtMessage {

	//modeHeader1
	public static final int MEAS_MODE_GETSYNC = 0;
	public static final int MEAS_MODE_SINGLE = 1;
	public static final int MEAS_MODE_AREA = 2;
	public static final int MEAS_MODE_VOLUME = 3;
	public static final int MEAS_MODE_ANGLE = 4;
	public static final int MEAS_MODE_RAIL = 5;
	public static final int MEAS_MODE_MIN_MAX = 6;
	public static final int MEAS_MODE_INDIRECT_HEIGHT = 7;
	public static final int MEAS_MODE_INDIRECT_LENGTH = 8;
	public static final int MEAS_MODE_DOUBLE_INDIRECT_HRIGHT = 9;
	public static final int MEAS_MODE_WALL_AREA = 10;
	public static final int MEAS_MODE_SETUP = 11;
	public static final int MEAS_MODE_TRAPEZOID = 14;

	// switch mode
	public static final int MODE_SWITCH_ON = 1;
	public static final int MODE_SWITCH_OFF = 0;
	
	//signalOperation
	public static final int OPERATION_START = 1;
	public static final int OPERATION_STOP = 0;
	public static final int OPERATION_UNDEFINED = OPERATION_STOP;

	//syncControl
	public static final int MODE_AUTOSYNC_CONTROL_ON = 1;
	public static final int MODE_AUTOSYNC_CONTROL_OFF = 0;

	// distReference constants
	public static final int DIST_REFERENCE_FRONT = 0;
	public static final int DIST_REFERENCE_TRIPOD = 1;
	public static final int DIST_REFERENCE_REAR = 2;
	public static final int DIST_REFERENCE_PIN = 3;

	// angleReference constants
	public static final int ANGLE_REFERENCE_BACK = 0;
	public static final int ANGLE_REFERENCE_SIDE = 1;
	public static final int ANGLE_REFERENCE_RAIL = 2;
	public static final int ANGLE_REFERENCE_INVALID = 7;
	
	private int mode;
	private int switchMode;
	private int signalOperation;
	private int syncControl;
	private int distReference;
	private int angleReference;
	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	/**
	 * @return the signalOperation
	 */
	public int getSignalOperation() {
		return signalOperation;
	}
	/**
	 * @param signalOperation the signalOperation to set
	 */
	public void setSignalOperation(int signalOperation) {
		this.signalOperation = signalOperation;
	}
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
	 * @return the distReference
	 */
	public int getDistReference() {
		return distReference;
	}
	/**
	 * @param distReference the distReference to set
	 */
	public void setDistReference(int distReference) {
		this.distReference = distReference;
	}
	/**
	 * @return the angleReference
	 */
	public int getAngleReference() {
		return angleReference;
	}
	/**
	 * @param angleReference the angleReference to set
	 */
	public void setAngleReference(int angleReference) {
		this.angleReference = angleReference;
	}
	/**
	 * @return the switchMode
	 */
	public int getSwitchMode() {
		return switchMode;
	}
	/**
	 * @param switchMode the switchMode to set
	 */
	public void setSwitchMode(int switchMode) {
		this.switchMode = switchMode;
	}
}
