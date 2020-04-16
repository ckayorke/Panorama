package com.bosch.mtprotocol.glm100C.message;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;



public class SimpleMessageFactory implements MtMessageFactory {

	/* (non-Javadoc)
	 * @see de.bde.bosch.mtprotocolssageFactory#createMessage(de.bode.bosch.mtprotocolme)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			return new SimpleMessage(f.getCommand());
		}
		
		throw new IllegalArgumentException("Can't create SimpleMessage from " + frame);
	}

}
