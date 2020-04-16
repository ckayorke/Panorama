package com.bosch.mtprotocol.linelaser.message.MotorOperations;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;


/**
 * Created by acn8kor on 4/15/2016.
 */
public class MotorOperationsMessageFactory implements MtMessageFactory {
    @Override
    public MtMessage createMessage(MtFrame frame) {
        if(frame instanceof MtBaseFrame){
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            MotorOperationsInputMessage message = new MotorOperationsInputMessage();

            return message;
        }

        throw new IllegalArgumentException("Can't create MotorOperationsMessageFactory from " + frame);
    }
}
