package com.bosch.mtprotocol.rotation.message.status;

import com.bosch.mtprotocol.MtMessage;

/**
 * Status message for GRL devices
 * 
 * Contains information about Battery SOC, System Mode, Orientation,
 * Slope-, ADS-, CAL-, and Spindle Status, Error Data, LED Status, 
 * Access Lock and Sync Mode
 * 
 * 
 * @author tos2si1
 */
public class SyncStatusInputMessage implements MtMessage {
	
	public static final int MODE_AUTOSYNC_CONTROL_ON = 1;
	public static final int MODE_AUTOSYNC_CONTROL_OFF = 0;
	
	public static final int STATUS_OFF = 0;
	public static final int STATUS_ON = 1;
	
	public static final int STATUS_LED_OFF = 0;
	public static final int STATUS_LED_FLASHING = 1;
	public static final int STATUS_LED_ON = 2;
	
	public static final int ACCESS_LOCK_OFF = 0;
	public static final int ACCESS_LOCK_ON = 1;
	
	public static final int SYSTEM_MODE_INITIALIZING = 0;
	public static final int SYSTEM_MODE_LEVEL = 1;
	public static final int SYSTEM_MODE_SLOPE = 2;
	public static final int SYSTEM_MODE_CALIBRATION = 3;
	public static final int SYSTEM_MODE_ERROR = 4;
	
	public static final int ORIENTATION_UNDEFINED = 0;
	public static final int ORIENTATION_HORIZONTAL = 1;
	public static final int ORIENTATION_LAYDOWN = 2;
	
	public static final int SLOPE_STATUS_UNCHANGED = 0;
	public static final int SLOPE_STATUS_LEVELLING = 1;
	public static final int SLOPE_STATUS_LEVELLED = 2;
	public static final int SLOPE_STATUS_ANGLE_CHANGED = 3;
	
	public static final int ADS_STATUS_OFF = 0;
	public static final int ADS_STATUS_ARMED = 1;
	public static final int ADS_STATUS_LOCKED = 2;
	public static final int ADS_STATUS_TRIGGERED = 3;
	
	public static final int SPINDLE_STATUS_MODE_CONTINUOUS = 0;
	public static final int SPINDLE_STATUS_MODE_SWEEP = 1;
	public static final int SPINDLE_STATUS_MODE_ERROR = 2;
	
	public static final int SPINDLE_STATUS_RPM_150 = 0;
	public static final int SPINDLE_STATUS_RPM_300 = 1;
	public static final int SPINDLE_STATUS_RPM_600 = 2;
	public static final int SPINDLE_STATUS_RPM_SPOT = 3;
	
	private int syncStatus;
	
	private int batterySOC; // In percent (0-100)
	private int systemMode;
	private int orientation;
	private int slopeStatus;
	private int adsStatus;
	
	// Calibration guard active
	// Can have two states:
	// 0 is no cal event, 1 is cal event
	private int calTimeActive;
	private int calShockActive;
	private int calTempActive;
	
	// Spindle status
	private int spindleStatusMode;
	private int spindleStatusRPM;
	
	// Error data elements 
	// Can have two states:
	// 0 is no error, 1 is error
	private int xAxisOutsideLevellingRange;
	private int yAxisOutsideLevellingRange;
	private int zAxisOutsideLevellingRange;
	private int xSlopeOutsideRange;
	private int ySlopeOutsideRange;
	private int levellingTimeout;
	private int systemError;
	private int highOperatingTemperature;
	
	private int vialBroken;
	private int spindleError;
	private int bluetoothError;
	
	// LED  
	// Can have three states: 
	// 0 is off, 1 is flashing, 2 is static light
	private int ledBatteryCritical;
	private int ledPowerGreen;
	private int ledPowerRed;
	private int ledADSRed;
	private int ledADSGreen;
	private int ledCalGuard;
	private int ledCalibration;
	private int ledBluetooth;
	
	// Access Lock
	// Can have two states:
	// 0 is not active, 1 is active
	private int accessLock;
	
	/**
	 * @return the syncStatus
	 */
	public int getSyncStatus() {
		return syncStatus;
	}

	/**
	 * @param syncStatus the syncStatus to set
	 */
	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	/**
	 * @return the batterySOC
	 */
	public int getBatterySOC() {
		return batterySOC;
	}

	/**
	 * @param batterySOC the batterySOC to set
	 */
	public void setBatterySOC(int batterySOC) {
		this.batterySOC = batterySOC;
	}

	/**
	 * @return the systemMode
	 */
	public int getSystemMode() {
		return systemMode;
	}

	/**
	 * @param systemMode the systemMode to set
	 */
	public void setSystemMode(int systemMode) {
		this.systemMode = systemMode;
	}

	/**
	 * @return the orientation
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the slopeStatus
	 */
	public int getSlopeStatus() {
		return slopeStatus;
	}

	/**
	 * @param slopeStatus the slopeStatus to set
	 */
	public void setSlopeStatus(int slopeStatus) {
		this.slopeStatus = slopeStatus;
	}

	/**
	 * @return the adsStatus
	 */
	public int getAdsStatus() {
		return adsStatus;
	}

	/**
	 * @param adsStatus the adsStatus to set
	 */
	public void setAdsStatus(int adsStatus) {
		this.adsStatus = adsStatus;
	}

	/**
	 * @return the calTimeActive
	 */
	public int getCalTimeActive() {
		return calTimeActive;
	}

	/**
	 * @param calTimeActive the calTimeActive to set
	 */
	public void setCalTimeActive(int calTimeActive) {
		this.calTimeActive = calTimeActive;
	}

	/**
	 * @return the calShockActive
	 */
	public int getCalShockActive() {
		return calShockActive;
	}

	/**
	 * @param calShockActive the calShockActive to set
	 */
	public void setCalShockActive(int calShockActive) {
		this.calShockActive = calShockActive;
	}

	/**
	 * @return the calTempActive
	 */
	public int getCalTempActive() {
		return calTempActive;
	}

	/**
	 * @param calTempActive the calTempActive to set
	 */
	public void setCalTempActive(int calTempActive) {
		this.calTempActive = calTempActive;
	}

	/**
	 * @return the spindleStatusMode
	 */
	public int getSpindleStatusMode() {
		return spindleStatusMode;
	}

	/**
	 * @param spindleStatusMode the spindleStatusMode to set
	 */
	public void setSpindleStatusMode(int spindleStatusMode) {
		this.spindleStatusMode = spindleStatusMode;
	}

	/**
	 * @return the spindleStatusRPM
	 */
	public int getSpindleStatusRPM() {
		return spindleStatusRPM;
	}

	/**
	 * @param spindleStatusRPM the spindleStatusRPM to set
	 */
	public void setSpindleStatusRPM(int spindleStatusRPM) {
		this.spindleStatusRPM = spindleStatusRPM;
	}

	/**
	 * @return the xAxisOutsideLevellingRange
	 */
	public int getxAxisOutsideLevellingRange() {
		return xAxisOutsideLevellingRange;
	}

	/**
	 * @param xAxisOutsideLevellingRange the xAxisOutsideLevellingRange to set
	 */
	public void setxAxisOutsideLevellingRange(int xAxisOutsideLevellingRange) {
		this.xAxisOutsideLevellingRange = xAxisOutsideLevellingRange;
	}

	/**
	 * @return the yAxisOutsideLevellingRange
	 */
	public int getyAxisOutsideLevellingRange() {
		return yAxisOutsideLevellingRange;
	}

	/**
	 * @param yAxisOutsideLevellingRange the yAxisOutsideLevellingRange to set
	 */
	public void setyAxisOutsideLevellingRange(int yAxisOutsideLevellingRange) {
		this.yAxisOutsideLevellingRange = yAxisOutsideLevellingRange;
	}

	/**
	 * @return the zAxisOutsideLevellingRange
	 */
	public int getzAxisOutsideLevellingRange() {
		return zAxisOutsideLevellingRange;
	}

	/**
	 * @param zAxisOutsideLevellingRange the zAxisOutsideLevellingRange to set
	 */
	public void setzAxisOutsideLevellingRange(int zAxisOutsideLevellingRange) {
		this.zAxisOutsideLevellingRange = zAxisOutsideLevellingRange;
	}

	/**
	 * @return the xSlopeOutsideRange
	 */
	public int getxSlopeOutsideRange() {
		return xSlopeOutsideRange;
	}

	/**
	 * @param xSlopeOutsideRange the xSlopeOutsideRange to set
	 */
	public void setxSlopeOutsideRange(int xSlopeOutsideRange) {
		this.xSlopeOutsideRange = xSlopeOutsideRange;
	}

	/**
	 * @return the ySlopeOutsideRange
	 */
	public int getySlopeOutsideRange() {
		return ySlopeOutsideRange;
	}

	/**
	 * @param ySlopeOutsideRange the ySlopeOutsideRange to set
	 */
	public void setySlopeOutsideRange(int ySlopeOutsideRange) {
		this.ySlopeOutsideRange = ySlopeOutsideRange;
	}

	/**
	 * @return the levellingTimeout
	 */
	public int getLevellingTimeout() {
		return levellingTimeout;
	}

	/**
	 * @param levellingTimeout the levellingTimeout to set
	 */
	public void setLevellingTimeout(int levellingTimeout) {
		this.levellingTimeout = levellingTimeout;
	}

	/**
	 * @return the systemError
	 */
	public int getSystemError() {
		return systemError;
	}

	/**
	 * @param systemError the systemError to set
	 */
	public void setSystemError(int systemError) {
		this.systemError = systemError;
	}

	/**
	 * @return the highOperatingTemperature
	 */
	public int getHighOperatingTemperature() {
		return highOperatingTemperature;
	}

	/**
	 * @param highOperatingTemperature the highOperatingTemperature to set
	 */
	public void setHighOperatingTemperature(int highOperatingTemperature) {
		this.highOperatingTemperature = highOperatingTemperature;
	}

	/**
	 * @return the vialBroken
	 */
	public int getVialBroken() {
		return vialBroken;
	}

	/**
	 * @param vialBroken the vialBroken to set
	 */
	public void setVialBroken(int vialBroken) {
		this.vialBroken = vialBroken;
	}

	/**
	 * @return the spindleError
	 */
	public int getSpindleError() {
		return spindleError;
	}

	/**
	 * @param spindleError the spindleError to set
	 */
	public void setSpindleError(int spindleError) {
		this.spindleError = spindleError;
	}

	/**
	 * @return the bluetoothError
	 */
	public int getBluetoothError() {
		return bluetoothError;
	}

	/**
	 * @param bluetoothError the bluetoothError to set
	 */
	public void setBluetoothError(int bluetoothError) {
		this.bluetoothError = bluetoothError;
	}

	/**
	 * @return the ledBatteryCritical
	 */
	public int getLedBatteryCritical() {
		return ledBatteryCritical;
	}

	/**
	 * @param ledBatteryCritical the ledBatteryCritical to set
	 */
	public void setLedBatteryCritical(int ledBatteryCritical) {
		this.ledBatteryCritical = ledBatteryCritical;
	}

	/**
	 * @return the ledPowerGreen
	 */
	public int getLedPowerGreen() {
		return ledPowerGreen;
	}

	/**
	 * @param ledPowerGreen the ledPowerGreen to set
	 */
	public void setLedPowerGreen(int ledPowerGreen) {
		this.ledPowerGreen = ledPowerGreen;
	}

	/**
	 * @return the ledPowerRed
	 */
	public int getLedPowerRed() {
		return ledPowerRed;
	}

	/**
	 * @param ledPowerRed the ledPowerRed to set
	 */
	public void setLedPowerRed(int ledPowerRed) {
		this.ledPowerRed = ledPowerRed;
	}

	/**
	 * @return the ledADSRed
	 */
	public int getLedADSRed() {
		return ledADSRed;
	}

	/**
	 * @param ledADSRed the ledADSRed to set
	 */
	public void setLedADSRed(int ledADSRed) {
		this.ledADSRed = ledADSRed;
	}

	/**
	 * @return the ledADSGreen
	 */
	public int getLedADSGreen() {
		return ledADSGreen;
	}

	/**
	 * @param ledADSGreen the ledADSGreen to set
	 */
	public void setLedADSGreen(int ledADSGreen) {
		this.ledADSGreen = ledADSGreen;
	}

	/**
	 * @return the ledCalGuard
	 */
	public int getLedCalGuard() {
		return ledCalGuard;
	}

	/**
	 * @param ledCalGuard the ledCalGuard to set
	 */
	public void setLedCalGuard(int ledCalGuard) {
		this.ledCalGuard = ledCalGuard;
	}

	/**
	 * @return the ledCalibration
	 */
	public int getLedCalibration() {
		return ledCalibration;
	}

	/**
	 * @param ledCalibration the ledCalibration to set
	 */
	public void setLedCalibration(int ledCalibration) {
		this.ledCalibration = ledCalibration;
	}

	/**
	 * @return the ledBluetooth
	 */
	public int getLedBluetooth() {
		return ledBluetooth;
	}

	/**
	 * @param ledBluetooth the ledBluetooth to set
	 */
	public void setLedBluetooth(int ledBluetooth) {
		this.ledBluetooth = ledBluetooth;
	}

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
		return "GRL Status Message: [syncStatus=" + syncStatus + "; batterySOC=" + batterySOC + "; systemMode=" + systemMode 
				+ "; orientation=" + orientation + "; slopeStatus=" + slopeStatus + "; adsStatus=" + adsStatus 
				+ "; calTimeAtcive=" + calTimeActive + "; calShockActive=" + calShockActive + "; calTempActive=" + calTempActive
				+ "; spindleStatusMode=" + spindleStatusMode + "; spindleStatusRPM=" + spindleStatusRPM
				+ "; xAxisLR=" + xAxisOutsideLevellingRange + "; yAxisLR=" + yAxisOutsideLevellingRange + "; zAxisLR=" + zAxisOutsideLevellingRange
				+ "; xSlopeOutsideRange=" + xSlopeOutsideRange + "; ySlopeOutsideRange=" + ySlopeOutsideRange
				+ "; levellingTimeout=" + levellingTimeout + "; systemError=" + systemError + "; highOpTemp=" + highOperatingTemperature
				+ "; vialBroken=" + vialBroken + "; spindleError=" + spindleError + "; bluetoothError=" + bluetoothError
				+ "; ledBatteryCritical=" + ledBatteryCritical + "; ledPowerGreen=" + ledPowerGreen + "; ledPowerRed=" + ledPowerRed
				+ "; ledADSGreen=" + ledADSGreen + "; ledADSRed=" + ledADSRed + "; ledCalGuard=" + ledCalGuard
				+ "; ledCalibration=" + ledCalibration + "; ledBluetooth=" + ledBluetooth
				+ "; accessLock=" + accessLock + "]";
	}

}
