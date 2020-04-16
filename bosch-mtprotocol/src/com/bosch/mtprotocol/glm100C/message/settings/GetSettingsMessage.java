/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.settings;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;



/**
 * @author AndrejsC
 *
 */
public class GetSettingsMessage extends SimpleMessage {

	public GetSettingsMessage() {
		super((byte) 83);
	}

}
