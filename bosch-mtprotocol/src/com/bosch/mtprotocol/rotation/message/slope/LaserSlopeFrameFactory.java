package com.bosch.mtprotocol.rotation.message.slope;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for app request to command 102
 * Set Slope
 * 
 * @author tos2si1
 */
public class LaserSlopeFrameFactory implements MtFrameFactory, MtFrameConstants {

	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof LaserSlopeMessage){
			LaserSlopeMessage m = (LaserSlopeMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 102);

			// byte 1
			SlopeMode slopeMode = new SlopeMode();
			slopeMode.levellingMode.setValue(m.getLevellingMode());
			slopeMode.xValidity.setValue(m.getxValidity());
			slopeMode.yValidity.setValue(m.getyValidity());

			slopeMode.reserved.setValue(0); // reserved; set to 0
			frame.pushUint8ToData(slopeMode.getByte());

			DataByte dataByte = new DataByte();
			
			// byte 2
			dataByte.data.setValue(m.getxData());
			frame.pushUint8ToData(dataByte.getByte());

			// byte 3
			dataByte.data.setValue(m.getyData());
			frame.pushUint8ToData(dataByte.getByte());

			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

	@SuppressWarnings("serial")
	class SlopeMode extends BitField {
		public Field levellingMode = new Field(this, 1);
		public Field xValidity = new Field(this, 1);
		public Field yValidity = new Field(this, 1);
		public Field reserved = new Field(this, 5);
	}

	@SuppressWarnings("serial")
	class DataByte extends BitField {
		public Field data = new Field(this, 8);
	}
}
