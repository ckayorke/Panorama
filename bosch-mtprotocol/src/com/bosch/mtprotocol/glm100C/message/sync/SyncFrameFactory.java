/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.sync;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;


public class SyncFrameFactory implements MtFrameFactory, MtFrameConstants {

	/* (non-Javadoc)
	 * @see de.boscde.bosch.mtprotocolFactory#createFrame(de.boschde.bosch.mtprotocole)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof SyncOutputMessage){
			SyncOutputMessage m = (SyncOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 80);

			// payload byte 1 (ModeHeader1)
			Headers headers = new Headers(); 
			headers.modeHeader.setValue(m.getMode());
			headers.signalOperation.setValue(m.getSignalOperation());
			headers.syncControl.setValue(m.getSyncControl());
			headers.switchMode.setValue(m.getSwitchMode());

			// payload byte 2 (References)
			References references = new References();
			references.distReference.setValue(m.getDistReference());
			references.angleReference.setValue(m.getAngleReference());

			frame.pushUint8ToData(headers.getByte());
			frame.pushUint8ToData(references.getByte());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

	@SuppressWarnings("serial")
	class Headers extends BitField {
		public Field modeHeader = new Field(this, 5); //0: GetSync, 1: SingleDist etc
		public Field signalOperation = new Field(this, 1); //start/stop operation
		public Field syncControl = new Field(this, 1);
		public Field switchMode = new Field(this, 1);

	}

	@SuppressWarnings("serial")
	class References extends BitField {
		public Field distReference = new Field(this, 3);
		public Field angleReference = new Field(this, 3);
	}
}
