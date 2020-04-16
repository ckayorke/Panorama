package com.bosch.mtprotocol.glm100C.message.edc;

import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.message.sync.SyncInputMessage;
import com.bosch.mtprotocol.type.UnionUint32;

/**
 * @author tos2si1
 *
 */
public class EDCInputMessage implements MtMessage {

	// reference edge constants
	public static final int REF_EDGE_ANGLE_BACK = 0;
	public static final int REF_EDGE_ANGLE_SIDE = 1;
	public static final int REF_EDGE_ANGLE_RAIL = 2;
	public static final int REF_EDGE_DISTANCE_FRONT = 0;
	public static final int REF_EDGE_DISTANCE_TRIPOD = 1;
	public static final int REF_EDGE_DISTANCE_REAR = 2;
	public static final int REF_EDGE_DISTANCE_PIN = 3;
	
	// device mode constants
	// measurement modes
	public static final int MODE_NO_ACTION = 0;
	public static final int MODE_SINGLE_DISTANCE = 1;
	public static final int MODE_CONTINUOUS_DISTANCE = 2;
	public static final int MODE_AREA_PART_1 = 3;
	public static final int MODE_AREA_FINAL = 4;
	public static final int MODE_VOLUME_PART_1 = 5;
	public static final int MODE_VOLUME_PART_2 = 6;
	public static final int MODE_VOLUME_FINAL = 7;
	public static final int MODE_SINGLE_ANGLE = 8;
	public static final int MODE_CONTINUOUS_ANGLE = 9;
	public static final int MODE_INDIRECT_HEIGHT = 10;
	public static final int MODE_INDIRECT_LENGTH = 11;
	public static final int MODE_DOUBLE_INDIRECT_HEIGHT_PART_1 = 12;
	public static final int MODE_DOUBLE_INDIRECT_HEIGHT_FINAL = 13;
	public static final int MODE_WALL_AREA_PART_1 = 14;
	public static final int MODE_WALL_AREA_FINAL = 15;
	public static final int MODE_CALCULATED_DISTANCE_PLUS = 16;
	public static final int MODE_CALCULATED_DISTANCE_MINUS = 17;
	public static final int MODE_CALCULATED_AREA_PLUS = 18;
	public static final int MODE_CALCULATED_AREA_MINUS = 19;
	public static final int MODE_CALCULATED_VOLUME_PLUS = 20;
	public static final int MODE_CALCULATED_VOLUME_MINUS = 21;
	public static final int MODE_SINGLE_LEVEL = 22;
	public static final int MODE_CONTINUOUS_LEVEL = 23;
	public static final int MODE_TRAPEZOID_PART_1 = 24;
	public static final int MODE_TRAPEZOID_PART_2 = 25;
	public static final int MODE_TRAPEZOID_FINAL = 26;
	// control modes
	public static final int MODE_GET_TIMESTAMP_OF_LIST_ITEM = 57;
	public static final int MODE_GET_LIST_ITEM_BY_INDEX = 58;
	public static final int MODE_TEMPERATURE_AND_SOC = 59;
	public static final int MODE_SET_DEV_MODE = 60;
	public static final int MODE_SET_ANGLE_REFERENCE = 61;
	public static final int MODE_SET_DISTANCE_REFERENCE = 62;
	public static final int MODE_ERROR_RESPONSE = 63;
	
	// warnings
	public static final int WARNING_TEMP_STATUS_OFF = 0;
	public static final int WARNING_TEMP_STATUS_ON = 1;
	public static final int WARNING_BATT_STATUS_OFF = 0;
	public static final int WARNING_BATT_STATUS_ON = 1;
	
	// laser status
	public static final int LASER_OFF = 0;
	public static final int LASER_ON = 1;
	
	// configuration units
	public static final int CONFIG_UNITS_METRIC = 0;
	public static final int CONFIG_UNITS_IMPERIAL = 1;
	
	// reference edge
	private int refEdge;
	
	// type of measurement
	private int devMode;
	
	// error status
	private int status;
	
	// laser status
	private int laserOn;
	
	// temperature status
	private int tempStatus;
	
	// battery low status
	private int batLowStatus;
	
	// configuration units
	private int configUnits;
	
	// unique ID for every measurement
	// if two measurements have the same ID, they are parts of same measurement
	// e.g. length 1 and result of an area measurement have the same ID
	private int measID; 

	// refer to current version of MT connectivity protocol (section 4.2.4) for the three delivered values
	private float result; // could be indirect L/H (m) or Act.L (m) or Area (m2) or Volume (m3)
	private float comp1; // could be Distance (m) or Min.L (m) or I3 (m)
	private float comp2; // could be Angle or Max.L (m)
	/**
	 * @return the refEdge
	 */
	public int getRefEdge() {
		return refEdge;
	}
	/**
	 * @param refEdge the refEdge to set
	 */
	public void setRefEdge(int refEdge) {
		this.refEdge = refEdge;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
	 * @return the tempStatus
	 */
	public int getTempStatus() {
		return tempStatus;
	}
	/**
	 * @param tStatus the tempStatus to set
	 */
	public void setTempStatus(int tStatus) {
		this.tempStatus = tStatus;
	}
	/**
	 * @return the batLowStatus
	 */
	public int getBatLowStatus() {
		return batLowStatus;
	}
	/**
	 * @param bLowStatus the bLowStatus to set
	 */
	public void setBatLowStatus(int bLowStatus) {
		this.batLowStatus = bLowStatus;
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
	/**
	 * @return the measID
	 */
	public int getMeasID() {
		return measID;
	}
	/**
	 * @param measID the measID to set
	 */
	public void setMeasID(int measID) {
		this.measID = measID;
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
	 * @return the comp1
	 */
	public float getComp1() {
		return comp1;
	}
	/**
	 * @param comp1 the comp1 to set
	 */
	public void setComp1(float comp1) {
		this.comp1 = comp1;
	}
	/**
	 * @return the comp2
	 */
	public float getComp2() {
		return comp2;
	}
	/**
	 * @param comp2 the comp2 to set
	 */
	public void setComp2(float comp2) {
		this.comp2 = comp2;
	}

	/**
	 * Converts measurement mode of MT devices, that use sync message, to measurement mode of
	 * MT devices, that use EDC message.
	 *
	 * @param syncMode measurement mode of sync message based device
	 * @return corresponding measurement mode of EDC based device
	 */
	public static int turnSyncModeToEDCMode(int syncMode) {
		int edcMode = 0;
		switch (syncMode) {
			case SyncInputMessage.MEAS_MODE_SINGLE:
				edcMode = EDCInputMessage.MODE_SINGLE_DISTANCE;
				break;
			case SyncInputMessage.MEAS_MODE_AREA:
				edcMode = EDCInputMessage.MODE_AREA_FINAL;
				break;
			case SyncInputMessage.MEAS_MODE_VOLUME:
				edcMode = EDCInputMessage.MODE_VOLUME_FINAL;
				break;
			case SyncInputMessage.MEAS_MODE_ANGLE:
				edcMode = EDCInputMessage.MODE_SINGLE_ANGLE;
				break;
			case SyncInputMessage.MEAS_MODE_MIN_MAX:
				edcMode = EDCInputMessage.MODE_CONTINUOUS_DISTANCE;
				break;
			case SyncInputMessage.MEAS_MODE_INDIRECT_HEIGHT:
				edcMode = EDCInputMessage.MODE_INDIRECT_HEIGHT;
				break;
			case SyncInputMessage.MEAS_MODE_INDIRECT_LENGTH:
				edcMode = EDCInputMessage.MODE_INDIRECT_LENGTH;
				break;
			case SyncInputMessage.MEAS_MODE_DOUBLE_INDIRECT_HRIGHT:
				edcMode = EDCInputMessage.MODE_DOUBLE_INDIRECT_HEIGHT_FINAL;
				break;
			case SyncInputMessage.MEAS_MODE_WALL_AREA:
				edcMode = EDCInputMessage.MODE_WALL_AREA_FINAL;
				break;
			case SyncInputMessage.MEAS_MODE_TRAPEZOID:
				edcMode = EDCInputMessage.MODE_TRAPEZOID_FINAL;
				break;
			default:
				edcMode = EDCInputMessage.MODE_NO_ACTION;
				break;
		}
		return edcMode;
	}

	/**
	 * Converts measurement mode of MT devices, that use EDC message, to measurement mode of
	 * MT devices, that use sync message.
	 *
	 * @param edcMode measurement mode of EDC based device
	 * @return corresponding measurement mode of sync message based device
	 */
	public static int turnEDCModeToSyncMode(int edcMode) {
		int syncMode;
		switch (edcMode) {
			case EDCInputMessage.MODE_SINGLE_DISTANCE:
			case EDCInputMessage.MODE_CALCULATED_DISTANCE_PLUS:
			case EDCInputMessage.MODE_CALCULATED_DISTANCE_MINUS:
				syncMode = SyncInputMessage.MEAS_MODE_SINGLE;
				break;
			case EDCInputMessage.MODE_CONTINUOUS_DISTANCE:
				syncMode = SyncInputMessage.MEAS_MODE_MIN_MAX;
				break;
			case EDCInputMessage.MODE_AREA_PART_1:
			case EDCInputMessage.MODE_AREA_FINAL:
			case EDCInputMessage.MODE_CALCULATED_AREA_PLUS:
			case EDCInputMessage.MODE_CALCULATED_AREA_MINUS:
				syncMode = SyncInputMessage.MEAS_MODE_AREA;
				break;
			case EDCInputMessage.MODE_VOLUME_PART_1:
			case EDCInputMessage.MODE_VOLUME_PART_2:
			case EDCInputMessage.MODE_VOLUME_FINAL:
			case EDCInputMessage.MODE_CALCULATED_VOLUME_PLUS:
			case EDCInputMessage.MODE_CALCULATED_VOLUME_MINUS:
				syncMode = SyncInputMessage.MEAS_MODE_VOLUME;
				break;
			case EDCInputMessage.MODE_SINGLE_ANGLE:
			case EDCInputMessage.MODE_CONTINUOUS_ANGLE:
				syncMode = SyncInputMessage.MEAS_MODE_ANGLE;
				break;
			case EDCInputMessage.MODE_INDIRECT_HEIGHT:
				syncMode = SyncInputMessage.MEAS_MODE_INDIRECT_HEIGHT;
				break;
			case EDCInputMessage.MODE_INDIRECT_LENGTH:
				syncMode = SyncInputMessage.MEAS_MODE_INDIRECT_LENGTH;
				break;
			case EDCInputMessage.MODE_DOUBLE_INDIRECT_HEIGHT_PART_1:
			case EDCInputMessage.MODE_DOUBLE_INDIRECT_HEIGHT_FINAL:
				syncMode = SyncInputMessage.MEAS_MODE_DOUBLE_INDIRECT_HRIGHT;
				break;
			case EDCInputMessage.MODE_WALL_AREA_PART_1:
			case EDCInputMessage.MODE_WALL_AREA_FINAL:
				syncMode = SyncInputMessage.MEAS_MODE_WALL_AREA;
				break;
			case EDCInputMessage.MODE_TRAPEZOID_PART_1:
			case EDCInputMessage.MODE_TRAPEZOID_PART_2:
			case EDCInputMessage.MODE_TRAPEZOID_FINAL:
				syncMode = SyncInputMessage.MEAS_MODE_TRAPEZOID;
				break;
			default:
				syncMode = 0;
				break;
		}
		return syncMode;
	}

	/**
	 * Utility method to cast correctly EDC message field formatted as float to int.
	 * Shall be used, when a float field of EDC message is filled with int by the GLM device.
	 * Converts the float value back to the initial byte stream and then to int.
	 *
	 * @param floatValue float value to be casted
	 * @return casted int value
	 */
	public static int mtCastFromFloatToInt(float floatValue) {
		UnionUint32 intValue = new UnionUint32();
		intValue.setValue(Float.floatToIntBits(floatValue));
		return intValue.getValue();
	}
	
	public String toString() {
		return "EDCInputMessage [devMode=" + devMode
				+ ", refEdge=" + refEdge + ", errStatus="
				+ status + ", laserOn=" + laserOn + ", configUnits=" + configUnits
				+ ", temperature warning=" + tempStatus + ", battery warning=" + batLowStatus
				+ ", measID=" + measID + ", result=" + result + ", comp1=" + comp1 
				+ ", comp2=" + comp2 + "]";
	}
}
