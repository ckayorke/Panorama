package com.bosch.mtprotocol.rotation.message.calibration;

import com.bosch.mtprotocol.MtMessage;

/**
 * Input message for command 108
 * Sync Cal Operations
 * 
 * @author tos2si1
 */
public class SyncCalibrationInputMessage implements MtMessage {
	
	public final static int CALIBRATION_NOT_INITIALIZED = 0;
	public final static int CALIBRATION_IN_PROGRESS = 1;
	public final static int CALIBRATION_COMPLETED = 2;
	public final static int CALIBRATION_ERROR = 3;
	
	private int xCal;
	private int yCal;
	private int zCal;
	private int devCal;
	/**
	 * @return the xCal
	 */
	public int getxCal() {
		return xCal;
	}
	/**
	 * @param xCal the xCal to set
	 */
	public void setxCal(int xCal) {
		this.xCal = xCal;
	}
	/**
	 * @return the yCal
	 */
	public int getyCal() {
		return yCal;
	}
	/**
	 * @param yCal the yCal to set
	 */
	public void setyCal(int yCal) {
		this.yCal = yCal;
	}
	/**
	 * @return the zCal
	 */
	public int getzCal() {
		return zCal;
	}
	/**
	 * @param zCal the zCal to set
	 */
	public void setzCal(int zCal) {
		this.zCal = zCal;
	}
	/**
	 * @return the devCal
	 */
	public int getDevCal() {
		return devCal;
	}
	/**
	 * @param devCal the devCal to set
	 */
	public void setDevCal(int devCal) {
		this.devCal = devCal;
	}
	
	public String toString() {
		return "SyncCalibrationInputMessage: [xCal=" + xCal + "; yCal=" + yCal
				+ "; zCal=" + zCal + "; devCal=" + devCal + "]";
	}

}
