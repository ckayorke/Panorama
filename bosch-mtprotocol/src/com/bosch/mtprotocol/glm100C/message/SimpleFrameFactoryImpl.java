package com.bosch.mtprotocol.glm100C.message;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;



public class SimpleFrameFactoryImpl implements MtFrameFactory, MtFrameConstants{

	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof SimpleMessage){
			SimpleMessage simpleMessage = (SimpleMessage) message;
			MtRequestFrame frame = new MtRequestFrame(0);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand(simpleMessage.getCommand());
			return frame;
		}
		
		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

}
