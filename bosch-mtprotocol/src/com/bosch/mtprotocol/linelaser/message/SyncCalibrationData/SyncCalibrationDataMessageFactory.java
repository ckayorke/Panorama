package com.bosch.mtprotocol.linelaser.message.SyncCalibrationData;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Factory for device response to command 77
 * Get Cal Guard Data
 * Created by tos2si1 on 12.05.2017.
 */

public class SyncCalibrationDataMessageFactory implements MtMessageFactory {

    @Override
    public MtMessage createMessage(MtFrame frame) {
        if(frame instanceof MtBaseFrame){
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            SyncCalibrationDataInputMessage message = new SyncCalibrationDataInputMessage();

            // byte 1
            message.setEventType(f.popUint8FromPayloadData());

            // byte 2, 3
            message.setEventIndex(f.popUint16FromPayloadData());

            // byte 4, 5, 6, 7
            message.setStartTimestamp(f.popUint32FromPayloadData());

            // byte 8, 9, 10, 11
            message.setEndTimestamp(f.popUint32FromPayloadData());

            // byte 12
            message.setExtremumValue(f.popUint8FromPayloadData());

            return message;
        }

        throw new IllegalArgumentException("Can't create SyncCalibrationInputMessage from " + frame);
    }
}
