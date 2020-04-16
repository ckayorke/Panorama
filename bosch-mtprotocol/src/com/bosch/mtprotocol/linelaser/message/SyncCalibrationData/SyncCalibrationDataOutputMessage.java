package com.bosch.mtprotocol.linelaser.message.SyncCalibrationData;

import com.bosch.mtprotocol.MtMessage;

/**
 * Output message for command 77
 * Get Cal Guard Data
 *
 * Created by tos2si1 on 12.05.2017.
 */

public class SyncCalibrationDataOutputMessage implements MtMessage {

    private int eventType;
    private int eventIndex;

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

    public String toString() {
        return "SyncCalibrationDataOutputMessage: [eventType = " + eventType + "; eventIndex = " + eventIndex + "]";
    }
}
