package com.bosch.mtprotocol.rotation.message.calibration;

import com.bosch.mtprotocol.MtMessage;

/**
 * Input message for command 107
 * Get Calibration Guard Data
 * 
 * @author tos2si1
 */
public class CalibrationGuardDataInputMessage implements MtMessage {
	
	public final static int CALIBRATION_GUARD_NO_FAILURE = 0;
	public final static int CALIBRATION_GUARD_FAILURE = 1;
	
	private int tempFailure;
	private int shockFailure;
	private int timeFailure;
	
	private int tempFailTimestamp;
	private int shockFailTimestamp;
	private int timeFailTimestamp;
	
	private int tempAtFailure;
	
	/**
	 * @return the tempFailure
	 */
	public int getTempFailure() {
		return tempFailure;
	}
	/**
	 * @param tempFailure the tempFailure to set
	 */
	public void setTempFailure(int tempFailure) {
		this.tempFailure = tempFailure;
	}
	/**
	 * @return the shockFailure
	 */
	public int getShockFailure() {
		return shockFailure;
	}
	/**
	 * @param shockFailure the shockFailure to set
	 */
	public void setShockFailure(int shockFailure) {
		this.shockFailure = shockFailure;
	}
	/**
	 * @return the timeFailure
	 */
	public int getTimeFailure() {
		return timeFailure;
	}
	/**
	 * @param timeFailure the timeFailure to set
	 */
	public void setTimeFailure(int timeFailure) {
		this.timeFailure = timeFailure;
	}
	/**
	 * @return the tempFailTimestamp
	 */
	public int getTempFailTimestamp() {
		return tempFailTimestamp;
	}
	/**
	 * @param tempFailTimestamp the tempFailTimestamp to set
	 */
	public void setTempFailTimestamp(int tempFailTimestamp) {
		this.tempFailTimestamp = tempFailTimestamp;
	}
	/**
	 * @return the shockFailTimestamp
	 */
	public int getShockFailTimestamp() {
		return shockFailTimestamp;
	}
	/**
	 * @param shockFailTimestamp the shockFailTimestamp to set
	 */
	public void setShockFailTimestamp(int shockFailTimestamp) {
		this.shockFailTimestamp = shockFailTimestamp;
	}
	/**
	 * @return the timeFailTimestamp
	 */
	public int getTimeFailTimestamp() {
		return timeFailTimestamp;
	}
	/**
	 * @param timeFailTimestamp the timeFailTimestamp to set
	 */
	public void setTimeFailTimestamp(int timeFailTimestamp) {
		this.timeFailTimestamp = timeFailTimestamp;
	}
	
	/**
	 * @return the tempAtFailure
	 */
	public int getTempAtFailure() {
		return tempAtFailure;
	}
	/**
	 * @param tempAtFailure the tempAtFailure to set
	 */
	public void setTempAtFailure(int tempAtFailure) {
		this.tempAtFailure = tempAtFailure;
	}
	
	public String toString() {
		return "CalibrationGuardDataInputMessage: [tempFailure=" + tempFailure + "; shockFailure=" + shockFailure
				+ "; timeFailure=" + timeFailure + ";tempFailTimestamp=" + tempFailTimestamp 
				+ "; shockFailTimestamp=" + shockFailTimestamp + "; timeFailTimestamp=" + timeFailTimestamp 
				+ "; tempAtFailure=" + tempAtFailure + "]";
	}

}
