package com.bosch.mtprotocol.glm100C.message.edc;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * @author tos2si1
 *
 */
public class EDCFrameFactory implements MtFrameFactory, MtFrameConstants {

	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof EDCOutputMessage){
			EDCOutputMessage m = (EDCOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 85);

			// payload byte 1 (Header)
			Headers headers = new Headers(); 
			headers.devMode.setValue(m.getDevMode());
			headers.keypadBypass.setValue(m.getKeypadBypass());
			headers.syncControl.setValue(m.getSyncControl());

			// payload byte 2 (RemoteCtrl)
			RemoteCtrl remoteCtrlData = new RemoteCtrl();
			remoteCtrlData.remoteCtrlData.setValue(m.getRemoteCtrlData());

			frame.pushUint8ToData(headers.getByte());
			frame.pushUint8ToData(remoteCtrlData.getByte());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
	
	@SuppressWarnings("serial")
	class Headers extends BitField {
		public Field syncControl = new Field(this, 1); //0: Stop sync, 1: Start sync
		public Field keypadBypass = new Field(this, 1); //0: Bypass inactive, 1: Bypass active
		public Field devMode = new Field(this, 6);
	}
	
	@SuppressWarnings("serial")
	class RemoteCtrl extends BitField {
		public Field remoteCtrlData = new Field(this, 8);
	}
}
