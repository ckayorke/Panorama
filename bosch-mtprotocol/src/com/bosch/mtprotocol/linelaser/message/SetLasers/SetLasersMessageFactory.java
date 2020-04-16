package com.bosch.mtprotocol.linelaser.message.SetLasers;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
;

/**
 * Created by acn8kor on 4/11/2016.
 */
public class SetLasersMessageFactory implements MtMessageFactory {
    @Override
    public MtMessage createMessage(MtFrame frame) {
        if(frame instanceof MtBaseFrame){
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            SetLasersInputMessage message = new SetLasersInputMessage();

            return message;
        }

        throw new IllegalArgumentException("Can't create SetLasersMessageFactory from " + frame);
    }
}
