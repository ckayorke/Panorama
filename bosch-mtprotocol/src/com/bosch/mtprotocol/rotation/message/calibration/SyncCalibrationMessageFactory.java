package com.bosch.mtprotocol.rotation.message.calibration;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for device response to command 108
 * Sync Cal Operations
 * 
 * @author tos2si1
 */
public class SyncCalibrationMessageFactory implements MtMessageFactory {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();
			
			SyncCalibrationInputMessage message = new SyncCalibrationInputMessage();

			// byte 1
			CalOperations calOp = new CalOperations();
			calOp.setByte(f.popUint8FromPayloadData());
			message.setxCal(calOp.xCal.getValue());
			message.setyCal(calOp.yCal.getValue());
			message.setzCal(calOp.zCal.getValue());
			message.setDevCal(calOp.devCal.getValue());
			
			return message;
		}

		throw new IllegalArgumentException("Can't create SyncCalibrationInputMessage from " + frame);
	}
	
	@SuppressWarnings("serial")
	class CalOperations extends BitField {
		public Field xCal = new Field(this, 2);
		public Field yCal = new Field(this, 2); 
		public Field zCal = new Field(this, 2);
		public Field devCal = new Field(this, 2);
	}

}
