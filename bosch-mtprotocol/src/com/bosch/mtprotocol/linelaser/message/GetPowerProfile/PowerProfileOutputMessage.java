package com.bosch.mtprotocol.linelaser.message.GetPowerProfile;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/29/2016.
 */
public class PowerProfileOutputMessage implements MtMessage {

    //byte 1
    private int profileNumber;

    public int getProfileNumber() {
        return profileNumber;
    }

    public void setProfileNumber(int profileNumber) {
        this.profileNumber = profileNumber;
    }

    public String toString() {
        return "PowerProfileOutputMessage: [profileNumber=" + profileNumber + "]";
    }

}
