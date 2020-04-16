package com.bosch.mtprotocol.linelaser.message.SystemStates;

import com.bosch.mtprotocol.MtMessage;

/**
 * Input message for command 71
 * System Status Message
 *
 * Created by acn8kor on 4/11/2016.
 */
public class SystemStateInputMessage implements MtMessage {

    // variables for byte 1
    private int timeCalGuardTriggered;
    private int tHighCalGuardTriggered;
    private int tLowCalGuardTriggered;
    private int shockCalGuardTriggered;
    private int rotaryBasePluggedIn;
    private int pendulumLocked;
    private int pulseMode;
    private int ool;

    // variables for byte 2
    private int laser1;
    private int laser2;
    private int laser3;
    private int reserveBits;
    private int tmtOff;
    private int buzzer;

    // variables for byte 3 and 4
    private int battery;
    private int temperature;

    public int getTimeCalGuardTriggered() {
        return timeCalGuardTriggered;
    }

    public void setTimeCalGuardTriggered(int timeCalGuardTriggered) {
        this.timeCalGuardTriggered = timeCalGuardTriggered;
    }

    public int gettHighCalGuardTriggered() {
        return tHighCalGuardTriggered;
    }

    public void settHighCalGuardTriggered(int tHighCalGuardTriggered) {
        this.tHighCalGuardTriggered = tHighCalGuardTriggered;
    }

    public int gettLowCalGuardTriggered() {
        return tLowCalGuardTriggered;
    }

    public void settLowCalGuardTriggered(int tLowCalGuardTriggered) {
        this.tLowCalGuardTriggered = tLowCalGuardTriggered;
    }

    public int getShockCalGuardTriggered() {
        return shockCalGuardTriggered;
    }

    public void setShockCalGuardTriggered(int shockCalGuardTriggered) {
        this.shockCalGuardTriggered = shockCalGuardTriggered;
    }

    public int getRotaryBasePluggedIn() {
        return rotaryBasePluggedIn;
    }

    public void setRotaryBasePluggedIn(int rotaryBasePluggedIn) {
        this.rotaryBasePluggedIn = rotaryBasePluggedIn;
    }

    public int getPendulumLocked() {
        return pendulumLocked;
    }

    public void setPendulumLocked(int pendulumLocked) {
        this.pendulumLocked = pendulumLocked;
    }

    public int getPulseMode() {
        return pulseMode;
    }

    public void setPulseMode(int pulseMode) {
        this.pulseMode = pulseMode;
    }

    public int getOol() {
        return ool;
    }

    public void setOol(int ool) {
        this.ool = ool;
    }

    public int getLaser1() {
        return laser1;
    }

    public void setLaser1(int laser1) {
        this.laser1 = laser1;
    }

    public int getLaser2() {
        return laser2;
    }

    public void setLaser2(int laser2) {
        this.laser2 = laser2;
    }

    public int getLaser3() {
        return laser3;
    }

    public void setLaser3(int laser3) {
        this.laser3 = laser3;
    }

    public int getReserveBits() {
        return reserveBits;
    }

    public void setReserveBits(int reserveBits) {
        this.reserveBits = reserveBits;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(int buzzer) {
        this.buzzer = buzzer;
    }

    public int getTmtOff() {
        return tmtOff;
    }

    public void setTmtOff(int tmtOff) {
        this.tmtOff = tmtOff;
    }

    public String toString() {
        return "GCL System Status: [TimeCalGuardTriggered = " + timeCalGuardTriggered + "; ShockCalGuardTriggered = " + shockCalGuardTriggered
                + "; TLowCalGuardTriggered = " + tLowCalGuardTriggered + "; THighCalGuardTriggered = " + tHighCalGuardTriggered
                + "; IsRotatoryBasePluggedIn = " + rotaryBasePluggedIn + "; IsPendulumLocked = " + pendulumLocked
                + "; IsPulseMode = " + pulseMode + "; IsOOL = " + ool
                + "; HorizontalLaser = "+ laser1 +"; VerticalLaser = " + laser2
                +"; UpDownLaser = " + laser3
                +"; Buzzer = " + buzzer + "; TmTOff = " + tmtOff
                + "]";
    }


}
