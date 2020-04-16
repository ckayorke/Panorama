package com.bosch.mtprotocol.glm100C.message.edc;

import com.bosch.mtprotocol.MtMessage;

public class EDCDoRemoteTriggerButtonMessage implements MtMessage {
	// buttonNumber values
	public static final int EN_BUTTON_MEASURE = 0;
	
	// button number
	private int buttonNumber;

	/**
	 * @return the buttonNumber
	 */
	public int getButtonNumber() {
		return buttonNumber;
	}

	/**
	 * @param buttonNumber the buttonNumber to set
	 */
	public void setButtonNumber(int buttonNumber) {
		this.buttonNumber = buttonNumber;
	}
	
	public String toString() {
		return "EDCRemoteTriggerButtonMessage [buttonNumber = " + buttonNumber + "]";
	}
}
