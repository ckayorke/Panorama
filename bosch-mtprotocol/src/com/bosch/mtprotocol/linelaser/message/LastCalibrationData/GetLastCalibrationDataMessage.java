package com.bosch.mtprotocol.linelaser.message.LastCalibrationData;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;

/**
 * Created by tos2si1 on 10.05.2017.
 */

public class GetLastCalibrationDataMessage extends SimpleMessage {

    public GetLastCalibrationDataMessage() {
        super((byte) 76);
    }
}
