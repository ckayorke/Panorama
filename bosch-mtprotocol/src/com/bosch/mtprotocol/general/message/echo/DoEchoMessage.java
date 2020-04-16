package com.bosch.mtprotocol.general.message.echo;

import com.bosch.mtprotocol.MtMessage;

/**
 * Do echo message
 * First implementation allows only
 * echos with 1 byte payload
 *
 * @author tos2si1
 */

public class DoEchoMessage implements MtMessage {

    private int payload;
    private byte[] byteload = new byte[5];

    public int getPayload() {
        return payload;
    }

    public void setPayload(int payload) {
        this.payload = payload;
    }

    public byte[] getByteload() {
        return byteload;
    }

    public void setByteload(byte[] byteload) {
        this.byteload = byteload;
    }

    public String toString() {
        return "DoEchoMessage with Payload = " + payload;
    }
}
