package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;



/**
 * Factory for app request to command 106
 * Get Log Packet
 * 
 * @author tos2si1
 */
public class LogPacketFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof LogPacketOutputMessage){


			LogPacketOutputMessage m = (LogPacketOutputMessage) message;
			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 106);
			frame.pushUint8ToData((byte) m.getDataType());
			frame.pushUint16ToData((short) m.getRequiredPacketNumber());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

}
