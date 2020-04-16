package com.bosch.mtprotocol.general.message.rtc;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Factory for device response to command 15
 * Get RealTimeClock Timestamp
 * 
 * @author tos2si1
 */
public class RTCTimestampMessageFactory implements MtMessageFactory {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();
			
			RTCTimestampMessage message = new RTCTimestampMessage();

			// byte 1, 2, 3, 4
			message.setRtcTimestamp(f.popUint32FromPayloadData());
			return message;
		}

		throw new IllegalArgumentException("Can't create RTCTimestampMessage from " + frame);
	}
}
