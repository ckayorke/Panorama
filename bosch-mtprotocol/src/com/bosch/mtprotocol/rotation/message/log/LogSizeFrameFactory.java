package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for app request to command 105
 * Get Log Size In Packets
 * 
 * @author tos2si1
 */
public class LogSizeFrameFactory implements MtFrameFactory, MtFrameConstants {

	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {

		if(message instanceof LogSizeOutputMessage){
			LogSizeOutputMessage m = (LogSizeOutputMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 105);

			DataType dType = new DataType();
			dType.dataType.setValue(m.getDataType());
			frame.pushUint8ToData(dType.getByte());

			frame.pushUint32ToData(m.getTimestampLastSync());

			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

	@SuppressWarnings("serial")
	class DataType extends BitField {
		public Field dataType = new Field(this, 8);
	}
}
