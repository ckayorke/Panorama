package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Factory for device response to command 105
 * Get Log Size in Packets
 * 
 * @author tos2si1
 */
public class LogSizeMessageFactory implements MtMessageFactory {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			LogSizeInputMessage message = new LogSizeInputMessage();

			// byte 1, 2
			message.setCalibStoredSize(f.popUint16FromPayloadData());
			
			return message;
		}

		throw new IllegalArgumentException("Can't create LogSizeInputMessage from " + frame);
	}

}
