/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.sync;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;
import com.bosch.mtprotocol.util.CastUtil;



/**
 * @author acernikovs
 *
 */
public class SyncMessageFactory implements MtMessageFactory {

	/* (non-Javadoc)
	 * @see de.boscde.bosch.mtprotocolgeFactory#createMessage(de.boschde.bosch.mtprotocol
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			return createSyncInputMessage(f);
		}

		throw new IllegalArgumentException("Can't create SyncInputMessage from " + frame);
	}
	
	public SyncInputMessage createSyncInputMessage(MtBaseFrame f){
		
		SyncInputMessage message = new SyncInputMessage();

		// byte 1
		ModeHeaderResp modeHeaderResp = new ModeHeaderResp();
		modeHeaderResp.setByte(f.popUint8FromPayloadData());

		message.setMode(modeHeaderResp.modeResponse.getValue());
		message.setCalcIndicator(modeHeaderResp.calcIndicator.getValue());

		// byte 2
		References references = new References();
		references.setByte(f.popUint8FromPayloadData());
		
		message.setDistReference(references.distReference.getValue());
		message.setAngleReference(references.angleReference.getValue());
		message.setConfigUnits(references.deviceConfig.getValue());

		// byte 3
		message.setSoc(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
		// byte 4
		message.setTemperature(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
		// bytes 5,6,7,8
		message.setDistance1(f.popFloatFromPayloadData());
		// bytes 9,10,11,12
		message.setDistance2(f.popFloatFromPayloadData());
		// bytes 13,14,15,16
		message.setDistance3(f.popFloatFromPayloadData());
		// bytes 17,18,29,20
		message.setResult(f.popFloatFromPayloadData());
		// bytes 21,22,23,24
		message.setAngle(f.popFloatFromPayloadData());
		// bytes 25,26,27,28
		message.setTimestamp(f.popUint32FromPayloadData());

		// byte 29
		ErrorsAndLaser errorsAndLaser = new ErrorsAndLaser();
		errorsAndLaser.setByte(f.popUint8FromPayloadData());

		message.setLaserOn(errorsAndLaser.laserOnOff.getValue());
		message.setErrors(errorsAndLaser.errors.getValue());
		
		// byte 30
		message.setMeasListIndex(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
		
		// reserved: byte 31, 32, 33
		f.popUint8FromPayloadData();
		f.popUint8FromPayloadData();
		f.popUint8FromPayloadData();
		
		return message;
	}

	@SuppressWarnings("serial")
	class ModeHeaderResp extends BitField {
		public Field modeResponse = new Field(this, 5);
		public Field calcIndicator = new Field(this, 3);
	}

	@SuppressWarnings("serial")
	class References extends BitField {
		public Field distReference = new Field(this, 3);
		public Field angleReference = new Field(this, 3);
		public Field deviceConfig = new Field(this, 1);
	}

	@SuppressWarnings("serial")
	class ErrorsAndLaser extends BitField {
		public Field laserOnOff = new Field(this, 1);
		public Field errors = new Field(this, 7);			
	}
}
