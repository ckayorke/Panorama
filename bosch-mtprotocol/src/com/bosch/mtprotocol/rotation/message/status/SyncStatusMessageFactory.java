package com.bosch.mtprotocol.rotation.message.status;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;

/**
 * Factory for device request/response to command 100
 * Synchronize Status
 * 
 * @author tos2si1
 */
public class SyncStatusMessageFactory implements MtMessageFactory {

	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			return createSyncStatusInputMessage(f);
		}

		throw new IllegalArgumentException("Can't create SyncStatusInputMessage from " + frame);
	}
	
	public SyncStatusInputMessage createSyncStatusInputMessage(MtBaseFrame f) {

		SyncStatusInputMessage message = new SyncStatusInputMessage();

		// byte 0
		ConStatus conStatus = new ConStatus();
		conStatus.setByte(f.popUint8FromPayloadData());
		message.setSyncStatus(conStatus.syncControl.getValue());
		
		// byte 1
		message.setBatterySOC(f.popUint8FromPayloadData());

		// byte 2
		message.setSystemMode(f.popUint8FromPayloadData());
		
		// byte 3
		message.setOrientation(f.popUint8FromPayloadData());
		
		// byte 4
		message.setSlopeStatus(f.popUint8FromPayloadData());
		
		// byte 5
		message.setAdsStatus(f.popUint8FromPayloadData());
		
		// byte 6
		CalibrationStatus calStatus = new CalibrationStatus();
		calStatus.setByte(f.popUint8FromPayloadData());
		message.setCalTimeActive(calStatus.calTimeActive.getValue());
		message.setCalShockActive(calStatus.calShockActive.getValue());
		message.setCalTempActive(calStatus.calTempActive.getValue());

		// byte 7
		SpindleStatus spindleStatus = new SpindleStatus();
		spindleStatus.setByte(f.popUint8FromPayloadData());
		message.setSpindleStatusRPM(spindleStatus.rpm.getValue());
		message.setSpindleStatusMode(spindleStatus.mode.getValue());

		// bytes 8, 9
		ErrorData1 error1 = new ErrorData1();
		error1.setByte(f.popUint8FromPayloadData());
		message.setHighOperatingTemperature(error1.highOperatingTempError.getValue());
		message.setSystemError(error1.systemError.getValue());
		message.setLevellingTimeout(error1.levellingTimeoutError.getValue());
		message.setySlopeOutsideRange(error1.ySlopeOutsideRangeError.getValue());
		message.setxSlopeOutsideRange(error1.xSlopeOutsideRangeError.getValue());
		message.setzAxisOutsideLevellingRange(error1.zAxisOutsideLevRangeError.getValue());
		message.setyAxisOutsideLevellingRange(error1.yAxisOutsideLevRangeError.getValue());
		message.setxAxisOutsideLevellingRange(error1.xAxisOutsideLevRangeError.getValue());
		
		f.popFloatFromPayloadData(); // byte 9 reserved, set to 0

		// bytes 10
		SystemError sysError = new SystemError();
		sysError.setByte(f.popUint8FromPayloadData());
		message.setBluetoothError(sysError.bluetoothError.getValue());
		message.setSpindleError(sysError.spindleError.getValue());
		message.setVialBroken(sysError.vialBroken.getValue());

		// bytes 11, 12
		LEDStatus1 led1 = new LEDStatus1();
		led1.setByte(f.popUint8FromPayloadData());
		LEDStatus2 led2 = new LEDStatus2();
		led2.setByte(f.popUint8FromPayloadData());
		
		message.setLedBatteryCritical(led1.ledBatteryCritical.getValue());
		if (message.getLedBatteryCritical() > 0) {
			message.setLedBatteryCritical(message.getLedBatteryCritical() + led2.ledBatteryCriticalStatic.getValue());
		}
		
		message.setLedPowerGreen(led1.ledPowerGreen.getValue());
		if (message.getLedPowerGreen() > 0) {
			message.setLedPowerGreen(message.getLedPowerGreen() + led2.ledPowerGreenStatic.getValue());
		}
		
		message.setLedPowerRed(led1.ledPowerRed.getValue());
		if (message.getLedPowerRed() > 0) {
			message.setLedPowerRed(message.getLedPowerRed() + led2.ledPowerRedStatic.getValue());
		}
		
		message.setLedADSRed(led1.ledADSRed.getValue());
		if (message.getLedADSRed() > 0) {
			message.setLedADSRed(message.getLedADSRed() + led2.ledADSRedStatic.getValue());
		}
		
		message.setLedADSGreen(led1.ledADSGreen.getValue());
		if (message.getLedADSGreen() > 0) {
			message.setLedADSGreen(message.getLedADSGreen() + led2.ledADSGreenStatic.getValue());
		}
		
		message.setLedCalGuard(led1.ledCalGuard.getValue());
		if (message.getLedCalGuard() > 0) {
			message.setLedCalGuard(message.getLedCalGuard() + led2.ledCalGuardStatic.getValue());
		}
		
		message.setLedCalibration(led1.ledCalibration.getValue());
		if (message.getLedCalibration() > 0) {
			message.setLedCalibration(message.getLedCalibration() + led2.ledCalibrationStatic.getValue());
		}
		
		message.setLedBluetooth(led1.ledBluetooth.getValue());
		if (message.getLedBluetooth() > 0) {
			message.setLedBluetooth(message.getLedBluetooth() + led2.ledBluetoothStatic.getValue());
		}
		
		// byte 13
		message.setAccessLock(f.popUint8FromPayloadData()); // only bit 0 relevant
		
		// bytes 14 & 15 are reserved
		f.popUint8FromPayloadData();
		f.popUint8FromPayloadData();

		return message;
	}
	
	@SuppressWarnings("serial")
	class ConStatus extends BitField {
		public Field syncControl = new Field(this, 1); //0: Stop sync, 1: Start sync
		public Field reserved = new Field(this, 7); // reserved; set to 0
	}
	
	@SuppressWarnings("serial")
	class CalibrationStatus extends BitField {
		public Field calTimeActive = new Field(this, 1);
		public Field calShockActive = new Field(this, 1);
		public Field calTempActive = new Field(this, 1); 
		public Field reserved = new Field(this, 5); // currently reserved, set to 0
	}
	
	@SuppressWarnings("serial")
	class SpindleStatus extends BitField {
		public Field mode = new Field(this, 2); // bit[0:1]
		public Field rpm = new Field(this, 2); // bit[2:3]
		public Field reserved = new Field(this, 4); // bit[4:7] reserved, set to 0
	}

	@SuppressWarnings("serial")
	class ErrorData1 extends BitField {
		public Field xAxisOutsideLevRangeError = new Field(this, 1);
		public Field yAxisOutsideLevRangeError = new Field(this, 1);
		public Field zAxisOutsideLevRangeError = new Field(this, 1);
		public Field xSlopeOutsideRangeError = new Field(this, 1);
		public Field ySlopeOutsideRangeError = new Field(this, 1);
		public Field levellingTimeoutError = new Field(this, 1);
		public Field systemError = new Field(this, 1);
		public Field highOperatingTempError = new Field(this, 1);	
	}
	
	@SuppressWarnings("serial")
	class SystemError extends BitField {
		public Field vialBroken = new Field(this, 1);
		public Field spindleError = new Field(this, 1);
		public Field bluetoothError = new Field(this, 1); 
		public Field reserved = new Field(this, 5); // currently reserved, set to 0
	}
	
	@SuppressWarnings("serial")
	class LEDStatus1 extends BitField {
		public Field ledBatteryCritical = new Field(this, 1);
		public Field ledPowerGreen = new Field(this, 1);
		public Field ledPowerRed = new Field(this, 1);
		public Field ledADSRed = new Field(this, 1);
		public Field ledADSGreen = new Field(this, 1);
		public Field ledCalGuard = new Field(this, 1);
		public Field ledCalibration = new Field(this, 1);
		public Field ledBluetooth = new Field(this, 1);
	}
	
	@SuppressWarnings("serial")
	class LEDStatus2 extends BitField {
		public Field ledBatteryCriticalStatic = new Field(this, 1);
		public Field ledPowerGreenStatic = new Field(this, 1);
		public Field ledPowerRedStatic = new Field(this, 1);
		public Field ledADSRedStatic = new Field(this, 1);
		public Field ledADSGreenStatic = new Field(this, 1);
		public Field ledCalGuardStatic = new Field(this, 1);
		public Field ledCalibrationStatic = new Field(this, 1);
		public Field ledBluetoothStatic = new Field(this, 1);
	}
}
