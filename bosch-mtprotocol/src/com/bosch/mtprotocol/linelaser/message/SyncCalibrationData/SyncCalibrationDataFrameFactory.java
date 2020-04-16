package com.bosch.mtprotocol.linelaser.message.SyncCalibrationData;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;

/**
 * Factory for app request to command 77
 * Get Cal Guard Data
 *
 * Created by tos2si1 on 12.05.2017.
 */

public class SyncCalibrationDataFrameFactory implements MtFrameFactory, MtFrameConstants {

    @Override
    public MtFrame createFrame(MtMessage message) {

        if(message instanceof SyncCalibrationDataOutputMessage){
            SyncCalibrationDataOutputMessage m = (SyncCalibrationDataOutputMessage) message;

            MtRequestFrame frame = new MtRequestFrame(255);
            frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
            frame.setCommand((byte) 77);

            frame.pushUint8ToData((byte) m.getEventType());
            frame.pushUint16ToData((short) m.getEventIndex());

            return frame;
        }

        throw new IllegalArgumentException("Can't create MtFrame from " + message);
    }
}
