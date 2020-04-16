/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.sync.list;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;


/**
 * @author AndrejsC
 *
 */
public class SyncListFrameFactory implements MtFrameFactory, MtFrameConstants{

	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof SyncListOutputMessage){
			SyncListOutputMessage m = (SyncListOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 81);
			if(isValidMessage(m)){
				frame.pushUint8ToData((byte) m.getIndexFrom());
				frame.pushUint8ToData((byte) m.getIndexTo());
				return frame;
			}
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

	private boolean isValidMessage(SyncListOutputMessage message){
		// indexFrom >= 0; indexTo >= 0
		// indexFrom <= 255 indexTo <= 255
		// indexTo >= indexFrom
		return (message.getIndexTo() >= 0 && message.getIndexTo() <= 255)
				&& message.getIndexFrom() >= 0 && message.getIndexTo() >= message.getIndexFrom();
	}
}
