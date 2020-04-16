/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.sync;

import com.bosch.mtprotocol.MtMessage;



/**
 * @author acernikovs
 *
 */
public class SyncInputMessage implements MtMessage{

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
	public static final int MEAS_MODE_CALIBRATION = 12;
	public static final int MEAS_MODE_MEAS_LIST = 13;
	public static final int MEAS_MODE_TRAPEZOID = 14;

	//signalOperation
	public static final int OPERATION_START = 1;
	public static final int OPERATION_STOP = 0;

	//calcIndicator
	public static final int CALCIND_IGNORE = 0;
	public static final int CALCIND_PLUS = 1;
	public static final int CALCIND_MINUS = 2;
	public static final int CALCIND_PLUS_EQUAL_= 3;
	public static final int CALCIND_MINUS_EQUAL_= 4;


	// distReference constants
	public static final int DIST_REFERENCE_FRONT = 0;
	public static final int DIST_REFERENCE_TRIPOD = 1;
	public static final int DIST_REFERENCE_REAR = 2;
	public static final int DIST_REFERENCE_PIN = 3;

	// angleReference constants
	public static final int ANGLE_REFERENCE_BACK = 0;
	public static final int ANGLE_REFERENCE_SIDE = 1;
	public static final int ANGLE_REFERENCE_RAIL = 2;
	public static final int ANGLE_REFERENCE_INVALID = 3;
	
	// deviceConfig
	public static final int CONFIG_UNITS_METRIC = 0;
	public static final int CONFIG_UNITS_IMPERIAL = 1; 
	
	// measurement settings 
	private int mode;
	private int calcIndicator;
	private int distReference;
	private int angleReference;
	private int configUnits;
	
	// measurement data
	private int soc;
	private int temperature;
	private float result;
	private float distance1, distance2, distance3;
	private float angle;
	private int timestamp;

	// additional
	private int laserOn;
	private int errors;
	private int measListIndex;
	
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
	 * @return the calcIndicator
	 */
	public int getCalcIndicator() {
		return calcIndicator;
	}
	/**
	 * @param calcIndicator the calcIndicator to set
	 */
	public void setCalcIndicator(int calcIndicator) {
		this.calcIndicator = calcIndicator;
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
	 * @return the soc
	 */
	public int getSoc() {
		return soc;
	}
	/**
	 * @param soc the soc to set
	 */
	public void setSoc(int soc) {
		this.soc = soc;
	}
	/**
	 * @return the temperature
	 */
	public int getTemperature() {
		return temperature;
	}
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	/**
	 * @return the result
	 */
	public float getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(float result) {
		this.result = result;
	}
	/**
	 * @return the distance1
	 */
	public float getDistance1() {
		return distance1;
	}
	/**
	 * @param distance1 the distance1 to set
	 */
	public void setDistance1(float distance1) {
		this.distance1 = distance1;
	}
	/**
	 * @return the distance2
	 */
	public float getDistance2() {
		return distance2;
	}
	/**
	 * @param distance2 the distance2 to set
	 */
	public void setDistance2(float distance2) {
		this.distance2 = distance2;
	}
	/**
	 * @return the distance3
	 */
	public float getDistance3() {
		return distance3;
	}
	/**
	 * @param distance3 the distance3 to set
	 */
	public void setDistance3(float distance3) {
		this.distance3 = distance3;
	}
	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}
	/**
	 * @param angle the angle to set
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}
	/**
	 * @return the timestamp
	 */
	public int getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the measListIndex
	 */
	public int getMeasListIndex() {
		return measListIndex;
	}
	/**
	 * @param measListIndex the measListIndex to set
	 */
	public void setMeasListIndex(int measListIndex) {
		this.measListIndex = measListIndex;
	}
	/**
	 * @return the laserOn
	 */
	public int getLaserOn() {
		return laserOn;
	}
	/**
	 * @param laserOn the laserOn to set
	 */
	public void setLaserOn(int laserOn) {
		this.laserOn = laserOn;
	}
	/**
	 * @return the errors
	 */
	public int getErrors() {
		return errors;
	}
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(int errors) {
		this.errors = errors;
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
	 * @return the configUnits
	 */
	public int getConfigUnits() {
		return configUnits;
	}
	/**
	 * @param configUnits the configUnits to set
	 */
	public void setConfigUnits(int configUnits) {
		this.configUnits = configUnits;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SyncInputMessage [mode=" + mode
				+ ", calcIndicator=" + calcIndicator + ", distReference="
				+ distReference + ", angleReference=" + angleReference
				+ ", configUnits=" + configUnits + ", soc=" + soc
				+ ", temperature=" + temperature + ", result=" + result
				+ ", distance1=" + distance1 + ", distance2=" + distance2
				+ ", distance3=" + distance3 + ", angle=" + angle
				+ ", timestamp=" + timestamp + ", laserOn=" + laserOn
				+ ", errors=" + errors + ", measListIndex=" + measListIndex + "]";
	}
}
