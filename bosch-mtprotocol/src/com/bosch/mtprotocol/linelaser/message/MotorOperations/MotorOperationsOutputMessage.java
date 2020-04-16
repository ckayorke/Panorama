package com.bosch.mtprotocol.linelaser.message.MotorOperations;

import com.bosch.mtprotocol.MtMessage;

/**
 * Created by acn8kor on 4/15/2016.
 */
public class MotorOperationsOutputMessage implements MtMessage {

    //variables byte1

    private int motorSpeed = 0; // 0 represents slow,. 1 represents fast
    private int singleStep = 0; // 0 represents off,1 represents on
    private int motorDirection = 0; // 0 represents ccw, 1 represents cw
    private int reservedBits;
    private int turnOnMotor=0; //0 represents tur off motor, 1 represents turn on motor

    // byte2 and byte 3
    private int degreeSpeed;

    public int getDegreeSpeed() {
        return degreeSpeed;
    }

    public void setDegreeSpeed(int degreeSpeed) {
        this.degreeSpeed = degreeSpeed;
    }


    public int getTurnOnMotor() {
        return turnOnMotor;
    }

    public void setTurnOnMotor(int turnOnMotor) {
        this.turnOnMotor = turnOnMotor;
    }

    public int getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(int motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    public int getSingleStep() {
        return singleStep;
    }

    public void setSingleStep(int singleStep) {
        this.singleStep = singleStep;
    }

    public int getMotorDirection() {
        return motorDirection;
    }

    public void setMotorDirection(int motorDirection) {
        this.motorDirection = motorDirection;
    }

    public int getReservedBits() {
        return reservedBits;
    }

    public void setReservedBits(int reservedBits) {
        this.reservedBits = reservedBits;
    }

    public String toString() {
        return "MotorOperationsOutputMessage: [MotorSpeed=" + motorSpeed + "; SingleStep=" + singleStep
                + "; MotorDirection=" + motorDirection+ "; ReservedBits=" + reservedBits
                +";TurnOnMotor="+turnOnMotor+ "]";
    }

}
