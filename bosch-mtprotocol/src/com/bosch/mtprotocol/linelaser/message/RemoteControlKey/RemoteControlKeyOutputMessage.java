package com.bosch.mtprotocol.linelaser.message.RemoteControlKey;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/27/2016.
 */
public class RemoteControlKeyOutputMessage implements MtMessage {

    public final static int KEY_FAST_CW = 1;
    public final static int KEY_FAST_CCW = 2;
    public final static int KEY_SLOW_CW = 3;
    public final static int KEY_SLOW_CCW = 4;
    public final static int KEY_STEP_CW = 5;
    public final static int KEY_STEP_CCW = 6;
    public final static int KEY_SELECTION_MODE = 7;
    public final static int KEY_PULSE_MODE = 8;

    //byte 1

    private int keyValue;

    public int getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }

    public String toString() {
        return "RemoteControlKeyOutputMessage: keyValue = " + keyValue;
    }

}
