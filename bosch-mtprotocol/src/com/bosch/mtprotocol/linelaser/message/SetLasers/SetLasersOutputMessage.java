package com.bosch.mtprotocol.linelaser.message.SetLasers;


import com.bosch.mtprotocol.MtMessage;


/**
 * SetLaserMessage as described in command 73
 *
 * Created by acn8kor on 4/11/2016.
 */
public class SetLasersOutputMessage implements MtMessage {

    //variables to set lasers
    private int laser1 = 0; // status 0 represents laser is off
    private int laser2 = 0; // status 1 represents laser on
    private int laser3 = 0;
    private int tmtOff = 0;
    private int buzzer = 0;

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

    public int getTmtOff() {
        return tmtOff;
    }

    public void setTmtOff(int tmtOff) {
        this.tmtOff = tmtOff;
    }

    public int getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(int buzzer) {
        this.buzzer = buzzer;
    }

    public String toString() {
        return "SetLasersOutputMessage: [HorizontalLaser = " + laser1 + "; VerticalLaser = " + laser2
                + "; PlumbLaser = " + laser3 + "; TmTOff" + tmtOff + "; Buzzer = " + buzzer + "]";
    }


}