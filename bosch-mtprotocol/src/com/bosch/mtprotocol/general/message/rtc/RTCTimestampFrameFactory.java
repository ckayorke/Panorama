package com.bosch.mtprotocol.general.message.rtc;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;

/**
 * Factory for app request to command 16
 * Set RealTimeClock Timestamp
 * 
 * @author tos2si1
 */
public class RTCTimestampFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof RTCTimestampMessage){
			RTCTimestampMessage m = (RTCTimestampMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 16);
			frame.pushUint32ToData(m.getRtcTimestamp());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
}
