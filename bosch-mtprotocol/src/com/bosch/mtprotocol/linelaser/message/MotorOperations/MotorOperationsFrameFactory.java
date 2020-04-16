package com.bosch.mtprotocol.linelaser.message.MotorOperations;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Request frame factory for message 72 (rotation base motor operation) for line lasers
 *
 * Created by acn8kor on 4/15/2016.
 */
public class MotorOperationsFrameFactory implements MtFrameFactory, MtFrameConstants {
    @Override
    public MtFrame createFrame(MtMessage message) {

        if(message instanceof MotorOperationsOutputMessage){
            MotorOperationsOutputMessage m= (MotorOperationsOutputMessage) message;

            MtRequestFrame frame = new MtRequestFrame(255);
            frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
            frame.setCommand((byte) 72);

            MotorOperations motorOperations = new MotorOperations();
            motorOperations.motorSpeed.setValue(m.getMotorSpeed());
            motorOperations.singleStep.setValue(m.getSingleStep());
            motorOperations.motorDirection.setValue(m.getMotorDirection());
            motorOperations.reserveBits.setValue(m.getReservedBits());
            motorOperations.turnOnMotor.setValue(m.getTurnOnMotor());
            frame.pushUint8ToData(motorOperations.getByte());

            frame.pushUint16ToData((short) m.getDegreeSpeed());

            return frame;
        }

        throw new IllegalArgumentException("Can't create MtFrame from " + message);
    }

    @SuppressWarnings("serial")
    class MotorOperations extends BitField {
        public Field motorSpeed = new Field(this, 1);
        public Field singleStep = new Field(this, 1);
        public Field motorDirection = new Field(this, 1);
        public Field reserveBits = new Field(this, 4);
        public Field turnOnMotor=new Field(this,1);
    }
}
