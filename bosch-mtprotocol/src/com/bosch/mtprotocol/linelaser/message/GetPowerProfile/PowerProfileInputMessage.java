package com.bosch.mtprotocol.linelaser.message.GetPowerProfile;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/29/2016.
 */
public class PowerProfileInputMessage implements MtMessage {

    private int profileNumber;

    private int estimatedBatteryVoltage;
    private int estimatedTimeLeftP1;
    private int estimatedTimeLeftP2;
    private int estimatedTimeLeftP3;

    public int getEstimatedBatteryVoltage() {
        return estimatedBatteryVoltage;
    }

    public void setEstimatedBatteryVoltage(int estimatedBatteryVoltage) {
        this.estimatedBatteryVoltage = estimatedBatteryVoltage;
    }

    public int getEstimatedTimeLeftP1() {
        return estimatedTimeLeftP1;
    }

    public void setEstimatedTimeLeftP1(int estimatedTimeLeftP1) {
        this.estimatedTimeLeftP1 = estimatedTimeLeftP1;
    }

    public int getEstimatedTimeLeftP2() {
        return estimatedTimeLeftP2;
    }

    public void setEstimatedTimeLeftP2(int estimatedTimeLeftP2) {
        this.estimatedTimeLeftP2 = estimatedTimeLeftP2;
    }

    public int getEstimatedTimeLeftP3() {
        return estimatedTimeLeftP3;
    }

    public void setEstimatedTimeLeftP3(int estimatedTimeLeftP3) {
        this.estimatedTimeLeftP3 = estimatedTimeLeftP3;
    }

    public int getProfileNumber() {
        return profileNumber;
    }

    public void setProfileNumber(int profileNumber) {
        this.profileNumber = profileNumber;
    }

    public String toString() {
        return "PowerProfileOutputMessage: [profileNumber = " + profileNumber +"; estimatedBatteryVoltage = " + estimatedBatteryVoltage +
                "; estimatedTimeLeftP1 = " + estimatedTimeLeftP1 + "; estimatedTimeLeftP2 = " + estimatedTimeLeftP2 + "; estimatedTimeLeftP3 = " + estimatedTimeLeftP3 + "]";
    }
}
