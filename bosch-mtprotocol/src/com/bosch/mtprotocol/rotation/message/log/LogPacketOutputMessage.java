package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for output message as 
 * described in commands 106
 * Get Packet
 * 
 * @author tos2si1
 */
public class LogPacketOutputMessage implements MtMessage {
	
	public static final int TYPE_OF_DATA_TEMPERATURE = 0;
	public static final int TYPE_OF_DATA_POWER_ON_OFF = 1;
	public static final int CALIBRATION_LOGS = 2;
	
	private int dataType;
	private int requiredPacketNumber;
	
	/**
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the requiredPacketNumber
	 */
	public int getRequiredPacketNumber() {
		return requiredPacketNumber;
	}
	/**
	 * @param requiredPacketNumber the requiredPacketNumber to set
	 */
	public void setRequiredPacketNumber(int requiredPacketNumber) {
		this.requiredPacketNumber = requiredPacketNumber;
	}
	
	public String toString() {
		return "LogPacketOutputMessage: [dataType=" + dataType + "; requiredPacketNumber=" + requiredPacketNumber + "]";
	}

}
