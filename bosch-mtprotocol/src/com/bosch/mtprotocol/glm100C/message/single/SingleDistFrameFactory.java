package com.bosch.mtprotocol.glm100C.message.single;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for app request to LRF command 64
 * Get single distance measurement
 * 
 * @author tos2si1
 */
public class SingleDistFrameFactory implements MtFrameFactory, MtFrameConstants {
	
	/* (non-Javadoc)
	 * @see de.boscde.bosch.mtprotocolFactory#createFrame(de.boschde.bosch.mtprotocole)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof SingleDistOutputMessage){
			SingleDistOutputMessage m = (SingleDistOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 64);

			// payload byte 1 (ModeHeader1)
			Parameters parameters = new Parameters(); 
			parameters.refEdge.setValue(m.getRefEdge());
			parameters.measTime.setValue(m.getMeasTime());
			parameters.measType.setValue(m.getMeasType());
			parameters.reserved.setValue(0);
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

	@SuppressWarnings("serial")
	class Parameters extends BitField {
		public Field refEdge = new Field(this, 2);
		public Field reserved = new Field(this, 3); // set to 0
		public Field measTime = new Field(this, 1);
		public Field measType = new Field(this, 2);
	}

}
