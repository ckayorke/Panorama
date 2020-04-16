package com.bosch.mtprotocol.linelaser.message.LastCalibrationData;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Message factory for input message of command 76
 * Last calibration data message
 *
 * Created by tos2si1 on 10.05.2017.
 */

public class LastCalibrationDataMessageFactory implements MtMessageFactory {

    @Override
    public MtMessage createMessage(MtFrame frame) {
        if(frame instanceof MtBaseFrame){
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            return createLastCalibrationDataMessage(f);
        }

        throw new IllegalArgumentException("Can't create SystemStateMessageFactory from " + frame);
    }

    public LastCalibrationDataMessage createLastCalibrationDataMessage(MtBaseFrame f) {

        LastCalibrationDataMessage message = new LastCalibrationDataMessage();

        // bytes 1, 2, 3, 4
        message.setLastCalibrationTimestamp(f.popUint32FromPayloadData());

        // bytes 5, 6
        message.setShockEventsFactoryCounter(f.popUint16FromPayloadData());

        // bytes 7, 8
        message.setShockEventsSinceLastClear(f.popUint16FromPayloadData());

        // bytes 9, 10
        message.setTempLowEventsFactoryCounter(f.popUint16FromPayloadData());

        // byte 11, 12
        message.setTempLowEventsSinceLastClear(f.popUint16FromPayloadData());

        // byte 13, 14
        message.setTempHighEventsFactoryCounter(f.popUint16FromPayloadData());

        // byte 15, 16
        message.setTempHighEventsSinceLastClear(f.popUint16FromPayloadData());

        return message;
    }
}
