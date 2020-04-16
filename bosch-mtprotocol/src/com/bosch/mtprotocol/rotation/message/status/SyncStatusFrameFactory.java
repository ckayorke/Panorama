package com.bosch.mtprotocol.rotation.message.status;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

public class SyncStatusFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof SyncStatusOutputMessage){
			SyncStatusOutputMessage m = (SyncStatusOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 100);

			// payload byte 1 (Header)
			ConStatus conStatus = new ConStatus(); 
			conStatus.syncControl.setValue(m.getSyncControl());
			conStatus.reserved.setValue(0); // set reserved bits to 0

			frame.pushUint8ToData(conStatus.getByte());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
	
	@SuppressWarnings("serial")
	class ConStatus extends BitField {
		public Field syncControl = new Field(this, 1); //0: Stop sync, 1: Start sync
		public Field reserved = new Field(this, 7); // reserved; set to 0
	}

}
