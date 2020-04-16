/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.settings;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;



/**
 * @author AndrejsC
 *
 */
public class SettingsFrameFactory implements MtFrameFactory, MtFrameConstants {

	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtFrameFactory#createcom.bosch.mtprotocolrotocol.MtMessage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {
		
		if(message instanceof SettingsMessage){
			SettingsMessage m = (SettingsMessage) message;

			MtRequestFrame frame = new MtRequestFrame(255);
			frame.setFrameMode(EN_FRAME_MODE_LONG_REQ_LONG_RESP);
			frame.setCommand((byte) 84);
			frame.pushUint8ToData((byte) m.getSpiritLevelEnabled());
			frame.pushUint8ToData((byte) m.getDispRotationEnabled());
			frame.pushUint8ToData((byte) m.getSpeakerEnabled());
			frame.pushUint8ToData((byte) m.getLaserPointerEnabled());
			frame.pushUint8ToData((byte) m.getBacklightMode());
			frame.pushUint8ToData((byte) m.getAngleUnit());
			frame.pushUint8ToData((byte) m.getMeasurementUnit());
			return frame;
		}

		throw new IllegalArgumentException("Can't create MtFrame from " + message);
	}

}
