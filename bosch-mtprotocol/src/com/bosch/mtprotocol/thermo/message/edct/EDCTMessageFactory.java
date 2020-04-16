package com.bosch.mtprotocol.thermo.message.edct;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

public class EDCTMessageFactory implements MtMessageFactory {

	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			return createEDCTInputMessage(f);
		}

		throw new IllegalArgumentException("Can't create SyncInputMessage from " + frame);
	}

	public EDCTInputMessage createEDCTInputMessage(MtBaseFrame f){

		EDCTInputMessage message = new EDCTInputMessage();

		// byte 1
		DevModeRef devModeRef = new DevModeRef();
		devModeRef.setByte(f.popUint8FromPayloadData());

		message.setPacketNum(devModeRef.packNumber.getValue());
		message.setDevMode(devModeRef.devMode.getValue());
		
		message.setReserved(devModeRef.reserved.getValue());

		// byte 2
		DevStatus devStatus = new DevStatus(); 
		devStatus.setByte(f.popUint8FromPayloadData());

		message.setAlarms(devStatus.alarms.getValue());
		message.setWarnAmbTemp(devStatus.warnAmbTemp.getValue());
		message.setWarnHumidity(devStatus.warnHumidity.getValue());
		message.setWarnDewPoint(devStatus.warnDewPoint.getValue());

		// byte 3 + 4
		message.setMeasID(f.popUint16FromPayloadData());

		// bytes 5,6,7,8
		message.setResult(f.popFloatFromPayloadData());

		// bytes 9,10,11,12
		message.setComp1(f.popFloatFromPayloadData());

		// bytes 13,14,15,16
		message.setComp2(f.popFloatFromPayloadData());

		return message;
	}

	@SuppressWarnings("serial")
	class DevModeRef extends BitField {
		public Field packNumber = new Field(this, 3);
		public Field devMode = new Field(this, 2);
		public Field reserved = new Field(this, 3); // unused; always set to 0
	}

	@SuppressWarnings("serial")
	class DevStatus extends BitField {
		public Field alarms = new Field(this, 2);
		public Field warnAmbTemp = new Field(this, 1);
		public Field warnHumidity = new Field(this, 1);
		public Field warnDewPoint = new Field(this, 1);
		public Field reserved = new Field(this, 3); // unused; always set to 0
	}

}
