/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.settings;

import com.bosch.mtprotocol.MtMessage;



/**
 * @author AndrejsC
 *
 */
public class SettingsMessage implements MtMessage {

	public static final int BACKLIGHT_MODE_AUTO = 0;
	public static final int BACKLIGHT_MODE_ENABLED = 1;
	public static final int BACKLIGHT_MODE_DISABLED = 2;
	
	public static final int ANGLE_UNIT_DEGREE = 16;
	public static final int ANGLE_UNIT_IN_FT = 19;
	public static final int ANGLE_UNIT_MM_M = 18;
	public static final int ANGLE_UNIT_PERCENT = 17;
	
	public static final int MEASUREMENT_UNIT_M = 2;
	public static final int MEASUREMENT_UNIT_CM = 3;
	public static final int MEASUREMENT_UNIT_MM = 4;
	public static final int MEASUREMENT_UNIT_YD = 5;
	public static final int MEASUREMENT_UNIT_FT = 6;
	public static final int MEASUREMENT_UNIT_FEET_INCH_FRACT = 7;
	public static final int MEASUREMENT_UNIT_INCH = 8;
	public static final int MEASUREMENT_UNIT_INCH_FRACT = 9;
	
	private int spiritLevelEnabled;
	private int dispRotationEnabled;
	private int speakerEnabled;
	private int laserPointerEnabled;
	private int backlightMode;
	private int angleUnit;
	private int measurementUnit;
	private int devConfiguration; // unused; set to 0
	private int lastUsedListIndex;
	
	/**
	 * @return the spiritLevelEnabled
	 */
	public int getSpiritLevelEnabled() {
		return spiritLevelEnabled;
	}
	/**
	 * @param spiritLevelEnabled the spiritLevelEnabled to set
	 */
	public void setSpiritLevelEnabled(int spiritLevelEnabled) {
		this.spiritLevelEnabled = spiritLevelEnabled;
	}
	/**
	 * @return the dispRotationEnabled
	 */
	public int getDispRotationEnabled() {
		return dispRotationEnabled;
	}
	/**
	 * @param dispRotationEnabled the dispRotationEnabled to set
	 */
	public void setDispRotationEnabled(int dispRotationEnabled) {
		this.dispRotationEnabled = dispRotationEnabled;
	}
	/**
	 * @return the speakerEnabled
	 */
	public int getSpeakerEnabled() {
		return speakerEnabled;
	}
	/**
	 * @param speakerEnabled the speakerEnabled to set
	 */
	public void setSpeakerEnabled(int speakerEnabled) {
		this.speakerEnabled = speakerEnabled;
	}
	/**
	 * @return the laserPointerEnabled
	 */
	public int getLaserPointerEnabled() {
		return laserPointerEnabled;
	}
	/**
	 * @param laserPointerEnabled the laserPointerEnabled to set
	 */
	public void setLaserPointerEnabled(int laserPointerEnabled) {
		this.laserPointerEnabled = laserPointerEnabled;
	}
	/**
	 * @return the backlightMode
	 */
	public int getBacklightMode() {
		return backlightMode;
	}
	/**
	 * @param backlightMode the backlightMode to set
	 */
	public void setBacklightMode(int backlightMode) {
		this.backlightMode = backlightMode;
	}
	/**
	 * @return the angleUnit
	 */
	public int getAngleUnit() {
		return angleUnit;
	}
	/**
	 * @param angleUnit the angleUnit to set
	 */
	public void setAngleUnit(int angleUnit) {
		this.angleUnit = angleUnit;
	}
	/**
	 * @return the measurementUnit
	 */
	public int getMeasurementUnit() {
		return measurementUnit;
	}
	/**
	 * @param measurementUnit the measurementUnit to set
	 */
	public void setMeasurementUnit(int measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	/**
	 * @return the devConfiguration
	 */
	public int getDevConfiguration() {
		return devConfiguration;
	}
	/**
	 * @param devConfiguration the devConfiguration to set
	 */
	public void setDevConfiguration(int devConfiguration) {
		this.devConfiguration = devConfiguration;
	}
	/**
	 * @return the lastUsedListIndex
	 */
	public int getLastUsedListIndex() {
		return lastUsedListIndex;
	}
	/**
	 * @param lastUsedListIndex the lastUsedListIndex to set
	 */
	public void setLastUsedListIndex(int lastUsedListIndex) {
		this.lastUsedListIndex = lastUsedListIndex;
	}
	/**
	 * Return the content of the settings message as String
	 */
	public String toString() {
		return "Spirit Level Enabled = " + spiritLevelEnabled + "; Display Rotation Enabled = " + dispRotationEnabled 
				+ "; Speaker Enabled = " + speakerEnabled + "; Laser Pointer Enabled = " + laserPointerEnabled 
				+ "; Backlight Mode = " + backlightMode + "; Angle Unit = " + angleUnit 
				+ "; Measurement Unit = " + measurementUnit + "; Last Used List Index = " + lastUsedListIndex;
	}

}
