package com.bosch.mtprotocol.linelaser.message.GCLDevInfo;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/18/2016.
 */
public class GCLDevInfoOutputMessage implements MtMessage {
    //byte 1 for subcommand
    private int subCommand;


    public int getSubCommand() {
        return subCommand;
    }

    public void setSubCommand(int subCommand) {
        this.subCommand = subCommand;
    }



    public String toString() {
        return "GCLDevInfoOutputMessage: [SubCommand=" + subCommand +"]";
    }
}
