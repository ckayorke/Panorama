package com.bosch.mtprotocol.linelaser.message.GCLDevInfo;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/18/2016.
 */
public class GCLDevInfoInputMessage implements MtMessage {
    //byte 1 and 2
    private int status;

    //variable for subcommand byte1
    private int subCommand;
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }



    public int getSubCommand() {
        return subCommand;
    }

    public void setSubCommand(int subCommand) {
        this.subCommand = subCommand;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return "GCLDevInfoInputMessage: [ Status =" +status + "; SubCommand=" +subCommand + "; Payload=" + payload + "]";
    }
}
