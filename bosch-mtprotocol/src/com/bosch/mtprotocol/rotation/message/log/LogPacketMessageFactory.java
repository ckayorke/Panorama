package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Factory for device response to command 106
 * Get Log Packet
 * 
 * @author tos2si1
 */
public class LogPacketMessageFactory implements MtMessageFactory {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			LogPacketInputMessage message = new LogPacketInputMessage();

			// byte 0
			message.setDataType(f.popUint8FromPayloadData());
			
			// byte 1, 2
			message.setPacketNumber(f.popUint16FromPayloadData());
			
			// byte 3 to 7 (log packet 1)
			message.setDataP1(f.popUint8FromPayloadData());
			message.setTimestampP1(f.popUint32FromPayloadData());
			
			// byte 8 to 12 (log packet 2)
			message.setDataP2(f.popUint8FromPayloadData());
			message.setTimestampP2(f.popUint32FromPayloadData());
			
			return message;
		}

		throw new IllegalArgumentException("Can't create LogPacketInputMessage from " + frame);
	}

}
