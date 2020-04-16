package com.bosch.mtprotocol.linelaser.message.SystemStates;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Message factory for input message of command 71
 *
 * Created by acn8kor on 4/11/2016.
 */
public class SystemStateMessageFactory implements MtMessageFactory {
    @Override
    public MtMessage createMessage(MtFrame frame) {
        if(frame instanceof MtBaseFrame){
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();

            return createSystemStateInputMessage(f);
        }

        throw new IllegalArgumentException("Can't create SystemStateMessageFactory from " + frame);
    }

    public SystemStateInputMessage createSystemStateInputMessage(MtBaseFrame f) {

        SystemStateInputMessage message = new SystemStateInputMessage();

        // byte 1
        GCLDeviceStatus deviceStatus = new GCLDeviceStatus();
        deviceStatus.setByte(f.popUint8FromPayloadData());
        message.setOol(deviceStatus.ool.getValue());
        message.setPulseMode(deviceStatus.pulseMode.getValue());
        message.setPendulumLocked(deviceStatus.pendulumLock.getValue());
        message.setRotaryBasePluggedIn(deviceStatus.rotaryBasePlug.getValue());
        message.setShockCalGuardTriggered(deviceStatus.shockEventTriggered.getValue());
        message.settLowCalGuardTriggered(deviceStatus.tLowEventTriggered.getValue());
        message.settHighCalGuardTriggered(deviceStatus.tHighEventTriggered.getValue());
        message.setTimeCalGuardTriggered(deviceStatus.timeEventTriggered.getValue());

        // byte 2
        LaserModeStatus laserModeStatus=new LaserModeStatus();
        laserModeStatus.setByte(f.popUint8FromPayloadData());
        message.setLaser1(laserModeStatus.laser1.getValue());
        message.setLaser2(laserModeStatus.laser2.getValue());
        message.setLaser3(laserModeStatus.laser3.getValue());
        message.setReserveBits(laserModeStatus.reserved.getValue());
        message.setTmtOff(laserModeStatus.tmtOff.getValue());
        message.setBuzzer(laserModeStatus.buzzer.getValue());

        //byte 3
         message.setBattery(f.popUint8FromPayloadData());

        //byte 4
        message.setTemperature(f.popUint8FromPayloadData());

      /*  // for crc mentioned in the excel
        f.popUint8FromPayloadData();
        f.popUint8FromPayloadData();*/

        return message;
    }

    @SuppressWarnings("serial")
    class GCLDeviceStatus extends BitField {
        public Field ool = new Field(this, 1);
        public Field pulseMode = new Field(this, 1);
        public Field pendulumLock = new Field(this, 1);
        public Field rotaryBasePlug = new Field(this, 1);
        public Field shockEventTriggered = new Field(this, 1);
        public Field tLowEventTriggered = new Field(this, 1);
        public Field tHighEventTriggered = new Field(this, 1);
        public Field timeEventTriggered = new Field(this, 1);
    }

     @SuppressWarnings("serial")
    class LaserModeStatus extends BitField{
         public Field laser1 = new Field(this, 1);
         public Field laser2 = new Field(this, 1);
         public Field laser3 = new Field(this, 1);
         public Field reserved = new Field(this, 3);
         public Field tmtOff = new Field(this, 1);
         public Field buzzer = new Field(this, 1);
     }
}
