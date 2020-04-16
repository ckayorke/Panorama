package com.bosch.mtprotocol.linelaser.message.RemoteControlKey;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Created by acn8kor on 4/27/2016.
 */
public class RemoteControlKeyMessageFactory implements MtMessageFactory {
    @Override
    public MtMessage createMessage(MtFrame frame) {
        if (frame instanceof MtBaseFrame) {
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            RemoteControlKeyInputMessage message = new RemoteControlKeyInputMessage();

            return message;
        }

        throw new IllegalArgumentException("Can't create RemoteControlKeyMessageFactory from " + frame);
    }
}