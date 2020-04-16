package com.bosch.mtprotocol.linelaser.message.RemoteControlKey;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Created by acn8kor on 4/27/2016.
 */
public class RemoteControlKeyFrameFactory implements MtFrameFactory, MtFrameConstants {
    @Override
    public MtFrame createFrame(MtMessage message) {

        if(message instanceof RemoteControlKeyOutputMessage){
            RemoteControlKeyOutputMessage m = (RemoteControlKeyOutputMessage) message;

            MtRequestFrame frame = new MtRequestFrame(255);
            frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
            frame.setCommand((byte) 74);

            SetLasers setLasers = new SetLasers();
            setLasers.keyValue.setValue(m.getKeyValue());
            frame.pushUint8ToData(setLasers.getByte());

            return frame;
        }

        throw new IllegalArgumentException("Can't create MtFrame from " + message);
    }

    @SuppressWarnings("serial")
    class SetLasers extends BitField {
        public Field keyValue = new Field(this, 8);
    }
}
