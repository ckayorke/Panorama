package com.bosch.mtprotocol.general.message.keypad;

import com.bosch.mtprotocol.MtMessage;

/**
 * Keypad pattern message
 * Contains the key pattern as integer
 * Pattern is dependent on device connected
 * 
 * @author tos2si1
 */
public class KeypadPatternMessage implements MtMessage {
	
	private int keypadPattern;

	/**
	 * @return the keypadPattern
	 */
	public int getKeypadPattern() {
		return keypadPattern;
	}

	/**
	 * @param keypadPattern the keypadPattern to set
	 */
	public void setKeypadPattern(int keypadPattern) {
		this.keypadPattern = keypadPattern;
	}
	
	public String toString() {
		return "Keypad pattern " + keypadPattern;
	}

}
