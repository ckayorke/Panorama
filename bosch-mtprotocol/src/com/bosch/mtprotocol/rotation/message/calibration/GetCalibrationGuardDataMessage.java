package com.bosch.mtprotocol.rotation.message.calibration;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;

/**
 * Request for command 107 
 * Get Calibration Guard Data
 * 
 * @author tos2si1
 */
public class GetCalibrationGuardDataMessage extends SimpleMessage {
	
	public GetCalibrationGuardDataMessage() {
		super((byte) 107);
	}
}
