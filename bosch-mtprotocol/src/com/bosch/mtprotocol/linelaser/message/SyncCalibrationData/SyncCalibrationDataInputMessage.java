package com.bosch.mtprotocol.linelaser.message.SyncCalibrationData;

import com.bosch.mtprotocol.MtMessage;

/**
 * Input message for command 77
 * Get Cal Guard Data
 *
 * Created by tos2si1 on 12.05.2017.
 */

public class SyncCalibrationDataInputMessage implements MtMessage {

    public static final int CAL_GUARD_EVENT_TYPE_SHOCK = 0;
    public static final int CAL_GUARD_EVENT_TYPE_TEMP_LOW = 1;
    public static final int CAL_GUARD_EVENT_TYPE_TEMP_HIGH = 2;

    private int eventType; // shock, temperature low / high
    private int eventIndex; // most recent event has index 0

    private int startTimestamp;
    private int endTimestamp;

    private int extremumValue; // could be lowest / highest temperature value in the detected period
    // current temperature for shock - should be ignored


    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getEventIndex() {
        return eventIndex;
    }

    public void setEventIndex(int eventIndex) {
        this.eventIndex = eventIndex;
    }

    public int getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(int startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public int getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(int endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public int getExtremumValue() {
        return extremumValue;
    }

    public void setExtremumValue(int extremumValue) {
        this.extremumValue = extremumValue;
    }

    public String toString() {
        return "SyncCalibrationDataInputMessage: [eventType = " + eventType + "; eventIndex = " + eventIndex
                + "; startTimestamp = " + startTimestamp + "; endTimestamp = " + endTimestamp
                + "; extremumValue = " + extremumValue + "]";
    }
}
