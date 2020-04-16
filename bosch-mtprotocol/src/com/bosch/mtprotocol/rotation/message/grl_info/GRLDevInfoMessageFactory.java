package com.bosch.mtprotocol.rotation.message.grl_info;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * Factory for parsing response message of GRL command 101 (Get Device Info)
 * Refer also to MT Protocol documentation
 * 
 * @author tos2si1
 */
public class GRLDevInfoMessageFactory implements MtMessageFactory {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();
			
			GRLDevInfoMessage message = new GRLDevInfoMessage();

			// byte 0, 1
			message.setManufacturingYear(f.popUint16FromPayloadData());
			// byte 2
			message.setDevVariant(f.popUint8FromPayloadData());
			// byte 3, 4
			message.setSwRevision(f.popUint16FromPayloadData());
			
			return message;
		}

		throw new IllegalArgumentException("Can't create GRLDevInfoMessage from " + frame);
	}

}
