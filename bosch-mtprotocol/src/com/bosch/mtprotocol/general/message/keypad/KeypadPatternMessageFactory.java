package com.bosch.mtprotocol.general.message.keypad;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Created by acn8kor on 6/15/2016.
 */
public class KeypadPatternMessageFactory implements MtMessageFactory {

    /* (non-Javadoc)
     * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
     */
    @Override
    public MtMessage createMessage(MtFrame frame) {
        if(frame instanceof MtBaseFrame){
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            KeypadPatternMessage message = new KeypadPatternMessage();

            // byte 1, 2, 3, 4
            message.setKeypadPattern(f.popUint32FromPayloadData());
            return message;
        }

        throw new IllegalArgumentException("Can't create KeypadPatternMessageFactory from " + frame);
    }
}
