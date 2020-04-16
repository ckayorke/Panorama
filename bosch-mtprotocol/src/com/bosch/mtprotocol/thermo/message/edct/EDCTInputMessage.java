package com.bosch.mtprotocol.thermo.message.edct;

import com.bosch.mtprotocol.MtMessage;

public class EDCTInputMessage implements MtMessage {

	// device mode constants send by app
	public static final int MODE_REQUEST_SYNC = 0; // no action -> will not be set by device
	
	// device mode constants in response package (send by device)
	public static final int MODE_SURFACE_TEMPERATURE = 0;
	public static final int MODE_THERMAL_BRIDGE = 1;
	public static final int MODE_DEW_POINT = 2;
	public static final int MODE_USER_DEFINED = 3;
	
	// all 4 packets will be sent with the same unique ID by GIS device on event (live measurement or send measurement from gallery)
	public static final int PACKET_NUM_1 = 1; // contains temp IR, temp IR min and temp IR max
	public static final int PACKET_NUM_2 = 2; // contains temp IR average, humidity and temp ambient
	public static final int PACKET_NUM_3 = 3; // contains temp dew point, temp K type and emission degree (ED)
	public static final int PACKET_NUM_4 = 4; // contains scale value, thermal leak delta value and empty float

	// warning constants
	public static final int WARNING_AMB_TEMP_STATUS_OFF = 0;
	public static final int WARNING_AMB_TEMP_STATUS_ON = 1;
	public static final int WARNING_HUMIDITY_STATUS_OFF = 0;
	public static final int WARNING_HUMIDITY_STATUS_ON = 1;
	public static final int WARNING_DEW_POINT_STATUS_OFF = 0;
	public static final int WARNING_DEW_POINT_STATUS_ON = 1;
	
	// alarms constants
	public static final int ALARM_TEMP_IR_OFF = 0;
	public static final int ALARM_TEMP_IR_LOW = 1;
	public static final int ALARM_TEMP_IR_HIGH = 2;
	
	// error status
	public static final int ERROR_STATUS_OK = 0; // unused: always 0
	
	// packet identifier
	private int packetNum;

	// type of measurement
	private int devMode;

	// alarms
	private int alarms;
	
	// warning ambient temperature
	private int warnAmbTemp;
	
	// warning humidity
	private int warnHumidity;
	
	// warning dew point
	private int warnDewPoint;

	// unique ID for every measurement
	// if two packets have the same ID, they are parts of same measurement
	private int measID; 
	
	private int reserved;

	// refer to current version of MT connectivity protocol thermo specification
	private float result; 
	private float comp1; 
	private float comp2; 
	
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
	 * @return the packetNum
	 */
	public int getPacketNum() {
		return packetNum;
	}
	/**
	 * @param pNum the packetNum to set
	 */
	public void setPacketNum(int pNum) {
		this.packetNum = pNum;
	}
	/**
	 * @return the alarms
	 */
	public int getAlarms() {
		return alarms;
	}
	/**
	 * @param alarms the alarms to set
	 */
	public void setAlarms(int alarms) {
		this.alarms = alarms;
	}
	/**
	 * @return the warnAmbTemp
	 */
	public int getWarnAmbTemp() {
		return warnAmbTemp;
	}
	/**
	 * @param warnATemp the WarnAmbTemp to set
	 */
	public void setWarnAmbTemp(int warnATemp) {
		this.warnAmbTemp = warnATemp;
	}
	/**
	 * @return the warnHumidity
	 */
	public int getWarnHumidity() {
		return warnHumidity;
	}
	/**
	 * @param warnHum the warnHumidity to set
	 */
	public void setWarnHumidity(int warnHum) {
		this.warnHumidity = warnHum;
	}
	/**
	 * @return the warnDewPoint
	 */
	public int getWarnDewPoint() {
		return warnDewPoint;
	}
	/**
	 * @param warnDP the warnDewPoint to set
	 */
	public void setWarnDewPoint(int warnDP) {
		this.warnDewPoint = warnDP;
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

	public String toString() {
		return "EDCTInputMessage [devMode=" + devMode + ", packetNum=" + packetNum + " reserved=" + reserved
				+ ", alarms=" + alarms + ", warnAmbTemp=" + warnAmbTemp
				+ ", warnHumidity=" + warnHumidity + ", warnDewPoint=" + warnDewPoint
				+ ", measID=" + measID + ", result=" + result + ", comp1=" + comp1 
				+ ", comp2=" + comp2 + "]";
	}
	/**
	 * @return the reserved
	 */
	public int getReserved() {
		return reserved;
	}
	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

}
