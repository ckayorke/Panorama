package com.bosch.mtprotocol.linelaser.message.ClearCalibrationEvents;

import com.bosch.mtprotocol.MtMessage;

/**
 * Output message to clear all calibration events on GLL device
 * Like described in command 78
 *
 * Created by tos2si1 on 20.06.2017.
 */

public class ClearCalibrationEventsMessage implements MtMessage {

    public static int CLEAR_CALIBRATION_EVENTS_DISABLED = 0;
    public static int CLEAR_CALIBRATION_EVENTS_ENABLED = 1;

    private int clearEvents;

    public int getClearEvents() {
        return clearEvents;
    }

    public void setClearEvents(int clearEvents) {
        this.clearEvents = clearEvents;
    }

    public String toString() {
        return "ClearCalibrationEventsMessage [clearEvents = " + clearEvents + "]";
    }
}
