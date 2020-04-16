package com.bosch.mtprotocol.rotation.message.eeprom_data;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for output message as 
 * described in command 109
 * Get EEPROM data with offset
 * 
 * @author tos2si1
 */
public class EEPROMDataOutputMessage implements MtMessage {
	
	private int offset;
	private int size;
	
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EEPROMDataOutputMessage [offset = " + offset + "; size = " + size + "]";
	}
}
