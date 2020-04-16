package com.bosch.mtprotocol.rotation.message.slope;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;

/**
 * Request for command 103 
 * Get Slope message
 * 
 * @author tos2si1
 */
public class GetLaserSlopeMessage extends SimpleMessage {
	
	public GetLaserSlopeMessage() {
		super((byte) 103);

	}

}
