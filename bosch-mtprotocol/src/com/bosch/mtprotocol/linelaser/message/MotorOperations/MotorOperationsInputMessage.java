package com.bosch.mtprotocol.linelaser.message.MotorOperations;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/15/2016.
 */
public class MotorOperationsInputMessage implements MtMessage {

    private int response;
    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String toString() {
        return "MotorOperationsInputMessage: [responseReceived=" + response+ "]";
    }
}
