/**
 * 
 */
package com.bosch.mtprotocol.glm100C.event;

import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtProtocol.MTProtocolEvent;



/**
 * @author AndrejsC
 *
 */
public class MtProtocolReceiveMessageEvent implements MTProtocolEvent {

	private MtMessage message;

	/**
	 * @param message
	 */
	public MtProtocolReceiveMessageEvent(MtMessage message) {
		super();
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public MtMessage getMessage() {
		return message;
	}

}
