package com.bosch.mtprotocol.linelaser.message.GCLDevInfo;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Created by acn8kor on 4/18/2016.
 */
public class GCLDevInfoFrameFactory implements MtFrameFactory, MtFrameConstants {


    @Override
    public MtFrame createFrame(MtMessage message) {
        if(message instanceof GCLDevInfoOutputMessage){
            GCLDevInfoOutputMessage m= (GCLDevInfoOutputMessage)message;

            MtRequestFrame frame = new MtRequestFrame(255);
            frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
            frame.setCommand((byte) 70);

            GCLDeviceInfo gclDeviceInfo = new GCLDeviceInfo();

            gclDeviceInfo.subCommand.setValue(m.getSubCommand());
            frame.pushUint8ToData(gclDeviceInfo.getByte());

            return frame;
        }

        throw new IllegalArgumentException("Can't create MtFrame from " + message);
    }
    @SuppressWarnings("serial")
    class GCLDeviceInfo extends BitField{
        public Field subCommand=new Field(this,8);

    }

}
