/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message;

import com.bosch.mtprotocol.MtMessage;

/**
 * @author acernikovs
 *
 */
public class SimpleMessage implements MtMessage {

	private byte ui8Command;
	
	/**
	 * @param command code
	 */
	public SimpleMessage(byte command) {
		super();
		this.ui8Command = command;
	}

	/**
	 * @return
	 */
	public byte getCommand() {
		return ui8Command;
	}

	/**
	 * @param ui8Command
	 */
	public void setCommand(byte ui8Command) {
		this.ui8Command = ui8Command;
	}
	
	
}
