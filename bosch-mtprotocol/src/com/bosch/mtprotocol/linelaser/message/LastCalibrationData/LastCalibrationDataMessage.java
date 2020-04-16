package com.bosch.mtprotocol.linelaser.message.LastCalibrationData;

import com.bosch.mtprotocol.MtMessage;

/**
 * Input message for command 76
 * Last Calibration Data Message
 *
 * Created by tos2si1 on 10.05.2017.
 */

public class LastCalibrationDataMessage implements MtMessage {

    // Timestamp of the last calibration done
    private int lastCalibrationTimestamp;

    // Ex-factory counter for supported calibration events
    private int shockEventsFactoryCounter;
    private int shockEventsSinceLastClear;
    private int tempLowEventsFactoryCounter;
    private int tempLowEventsSinceLastClear;
    private int tempHighEventsFactoryCounter;
    private int tempHighEventsSinceLastClear;

    public int getLastCalibrationTimestamp() {
        return lastCalibrationTimestamp;
    }

    public void setLastCalibrationTimestamp(int lastCalibrationTimestamp) {
        this.lastCalibrationTimestamp = lastCalibrationTimestamp;
    }

    public int getShockEventsFactoryCounter() {
        return shockEventsFactoryCounter;
    }

    public void setShockEventsFactoryCounter(int shockEventsFactoryCounter) {
        this.shockEventsFactoryCounter = shockEventsFactoryCounter;
    }

    public int getTempLowEventsFactoryCounter() {
        return tempLowEventsFactoryCounter;
    }

    public void setTempLowEventsFactoryCounter(int tempLowEventsFactoryCounter) {
        this.tempLowEventsFactoryCounter = tempLowEventsFactoryCounter;
    }

    public int getTempHighEventsFactoryCounter() {
        return tempHighEventsFactoryCounter;
    }

    public void setTempHighEventsFactoryCounter(int tempHighEventsFactoryCounter) {
        this.tempHighEventsFactoryCounter = tempHighEventsFactoryCounter;
    }

    public int getShockEventsSinceLastClear() {
        return shockEventsSinceLastClear;
    }

    public void setShockEventsSinceLastClear(int shockEventsSinceLastClear) {
        this.shockEventsSinceLastClear = shockEventsSinceLastClear;
    }

    public int getTempLowEventsSinceLastClear() {
        return tempLowEventsSinceLastClear;
    }

    public void setTempLowEventsSinceLastClear(int tempLowEventsSinceLastClear) {
        this.tempLowEventsSinceLastClear = tempLowEventsSinceLastClear;
    }

    public int getTempHighEventsSinceLastClear() {
        return tempHighEventsSinceLastClear;
    }

    public void setTempHighEventsSinceLastClear(int tempHighEventsSinceLastClear) {
        this.tempHighEventsSinceLastClear = tempHighEventsSinceLastClear;
    }

    public String toString() {
        return "LastCalibrationData: [LastCalibrationTimestamp = " + lastCalibrationTimestamp
                + "; ShockEventsCounter = " + shockEventsFactoryCounter + "; ShockEventsSinceLastClear = " + shockEventsSinceLastClear
                + "; TempLowEventsFactoryCounter = " + tempLowEventsFactoryCounter + "; TempLowEventsSinceLastClear = " + tempLowEventsSinceLastClear
                + "; TempHighEventsFactoryCounter = " + tempHighEventsFactoryCounter + "; TempHighEventsSinceLastClear = " + tempHighEventsSinceLastClear + "]";
    }
}
