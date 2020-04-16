package com.bosch.mtprotocol.rotation.message.calibration;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for app request to command 108
 * Sync Cal Operations
 * 
 * @author tos2si1
 */
public class SyncCalibrationFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof SyncCalibrationOutputMessage){
			SyncCalibrationOutputMessage m = (SyncCalibrationOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 108);
			
			CalOperations calOp = new CalOperations();
			calOp.xCal.setValue(m.getxCal());
			calOp.yCal.setValue(m.getyCal());
			calOp.zCal.setValue(m.getzCal());
			calOp.devCal.setValue(m.getDevCal());
			frame.pushUint8ToData(calOp.getByte());
			
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
	
	@SuppressWarnings("serial")
	class CalOperations extends BitField {
		public Field xCal = new Field(this, 2);
		public Field yCal = new Field(this, 2); 
		public Field zCal = new Field(this, 2);
		public Field devCal = new Field(this, 2);
	}
}
