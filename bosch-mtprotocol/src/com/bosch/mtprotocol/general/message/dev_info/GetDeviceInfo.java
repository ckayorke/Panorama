package com.bosch.mtprotocol.general.message.dev_info;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;

/**
 * Request message to get device information
 * DevInfoMessage should come as response
 * 
 * This command is generic and applicable for all
 * devices using MT Protocol
 * 
 * Refer also to MT Protocol documentation
 * 
 * @author tos2si1
 */
public class GetDeviceInfo extends SimpleMessage {
	
	public GetDeviceInfo() {
		super((byte) 6);
	}

}
