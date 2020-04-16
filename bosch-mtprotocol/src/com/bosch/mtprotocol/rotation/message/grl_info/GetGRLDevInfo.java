package com.bosch.mtprotocol.rotation.message.grl_info;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;

/**
 * Request message to get device information from GRL device
 * GRLDevInfoMessage should come as response
 * 
 * This command is created because the generic Device Info command
 * (Command 6) is longer than 20 bytes and thus not applicable for BLE
 * 
 * Refer also to MT Protocol rotation laser documentation
 * 
 * @author tos2si1
 */
public class GetGRLDevInfo extends SimpleMessage {
	
	public GetGRLDevInfo() {
		super((byte) 101);
	}

}
