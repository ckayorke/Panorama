package com.bosch.mtprotocol.rotation.message.access_lock;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for app request to command 104
 * Set Access Lock
 * 
 * @author tos2si1
 */
public class AccessLockFrameFactory implements MtFrameFactory, MtFrameConstants {

	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof AccessLockOutputMessage){
			AccessLockOutputMessage m = (AccessLockOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 104);
			
			AccessLockByte accessLock = new AccessLockByte();
			accessLock.accessLock.setValue(m.getAccessLock());
			frame.pushUint8ToData(accessLock.getByte());
			
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
	
	@SuppressWarnings("serial")
	class AccessLockByte extends BitField {
		public Field accessLock = new Field(this, 8);
	}
}
