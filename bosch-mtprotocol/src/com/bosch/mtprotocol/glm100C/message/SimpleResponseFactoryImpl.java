package com.bosch.mtprotocol.glm100C.message;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtResponseFrame;

public class SimpleResponseFactoryImpl implements MtFrameFactory, MtFrameConstants {

	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if (message instanceof SimpleResponse) {
			SimpleResponse mes = (SimpleResponse) message;
			MtResponseFrame frame = new MtResponseFrame(0);
			frame.setFrameFormat(EN_FRAME_FORMAT_LONG);
			frame.setStatusByte(mes.getStatusByte());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}
}
