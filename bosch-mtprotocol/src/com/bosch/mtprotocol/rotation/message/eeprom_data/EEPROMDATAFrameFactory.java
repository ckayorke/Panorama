package com.bosch.mtprotocol.rotation.message.eeprom_data;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;

/**
 * Factory for app request to command 109
 * Get EEPROM data with offset
 * 
 * @author tos2si1
 */
public class EEPROMDATAFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof EEPROMDataOutputMessage){
			EEPROMDataOutputMessage m = (EEPROMDataOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 109);

			frame.pushUint16ToData((short) m.getOffset());
			frame.pushUint16ToData((short) m.getSize());

			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

}
