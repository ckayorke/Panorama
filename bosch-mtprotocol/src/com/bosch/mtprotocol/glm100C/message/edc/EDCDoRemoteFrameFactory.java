package com.bosch.mtprotocol.glm100C.message.edc;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

public class EDCDoRemoteFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof EDCDoRemoteTriggerButtonMessage) {
			EDCDoRemoteTriggerButtonMessage m = (EDCDoRemoteTriggerButtonMessage) message;
			
			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 86);
			
			// payload byte 1 (ButtonNumber)
			ButtonNumber buttonNumber = new ButtonNumber();
			buttonNumber.buttonNumber.setValue(m.getButtonNumber());
			
			frame.pushUint8ToData(buttonNumber.getByte());
			
			return frame;
		}
		
		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
	
	@SuppressWarnings("serial")
	class ButtonNumber extends BitField {
		public Field buttonNumber = new Field(this, 8);
	}
}
