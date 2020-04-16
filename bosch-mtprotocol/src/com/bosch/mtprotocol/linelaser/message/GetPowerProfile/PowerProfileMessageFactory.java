package com.bosch.mtprotocol.linelaser.message.GetPowerProfile;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Created by acn8kor on 4/29/2016.
 */
public class PowerProfileMessageFactory implements MtMessageFactory {
    @Override
    public MtMessage createMessage(MtFrame frame) {
        if (frame instanceof MtBaseFrame) {
            MtBaseFrame f = (MtBaseFrame) frame;
            f.reset();


            return createPowerProfileInputMessage(f);
        }

        throw new IllegalArgumentException("Can't create PowerProfileMessageFactory from " + frame);
    }

    public PowerProfileInputMessage createPowerProfileInputMessage(MtBaseFrame f) {

        PowerProfileInputMessage message = new PowerProfileInputMessage();

        // byte 1
        GetPowerProfile getPowerProfile = new GetPowerProfile();
        getPowerProfile.setByte(f.popUint8FromPayloadData());
        message.setProfileNumber(getPowerProfile.profileNumber.getValue());

        // byte 2 and 3
        message.setEstimatedBatteryVoltage(f.popUint16FromPayloadData());

        // byte 4 to 9
        message.setEstimatedTimeLeftP1(f.popUint16FromPayloadData());
        message.setEstimatedTimeLeftP2(f.popUint16FromPayloadData());
        message.setEstimatedTimeLeftP3(f.popUint16FromPayloadData());

        return message;
    }

    @SuppressWarnings("serial")
    class GetPowerProfile extends BitField {
        public Field profileNumber = new Field(this, 8); //get the profile info
    }




}