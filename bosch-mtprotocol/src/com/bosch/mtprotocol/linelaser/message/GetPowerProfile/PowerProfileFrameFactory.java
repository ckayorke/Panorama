package com.bosch.mtprotocol.linelaser.message.GetPowerProfile;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Created by acn8kor on 4/29/2016.
 */
public class PowerProfileFrameFactory  implements MtFrameFactory, MtFrameConstants {
    @Override
    public MtFrame createFrame(MtMessage message) {

        if(message instanceof PowerProfileOutputMessage){
            PowerProfileOutputMessage m = (PowerProfileOutputMessage) message;

            MtRequestFrame frame = new MtRequestFrame(255);
            frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
            frame.setCommand((byte) 75);

            SetPowerProfile setPowerProfile = new SetPowerProfile();
            setPowerProfile.profileNumber.setValue(m.getProfileNumber());

            frame.pushUint8ToData(setPowerProfile.getByte());

            return frame;
        }

        throw new IllegalArgumentException("Can't create MtFrame from " + message);
    }

    @SuppressWarnings("serial")
    class SetPowerProfile extends BitField {
        public Field profileNumber = new Field(this, 8);

    }
}
