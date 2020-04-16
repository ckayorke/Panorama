package com.bosch.mtprotocol.rotation.message.eeprom_data;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for input message as 
 * described in command 109
 * Get EEPROM data with offset
 * 
 * @author tos2si1
 */
public class EEPROMDataInputMessage implements MtMessage {

	// data array of concatenated bytes
	private byte[] data;

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	public String toString() {
		return "EEPROMDataInputMessage, data length=" + (data == null ? "0" :Integer.toString(data.length)) + "]";
	}

}
