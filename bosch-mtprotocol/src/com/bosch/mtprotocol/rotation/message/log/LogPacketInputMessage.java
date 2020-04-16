package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for input message as 
 * described in command 106
 * Get Packet
 * 
 * @author tos2si1
 */
public class LogPacketInputMessage implements MtMessage {
	
	public static final int TYPE_OF_DATA_TEMPERATURE = 0;
	public static final int TYPE_OF_DATA_POWER_ON_OFF = 1;
	
	private int dataType;
	private int packetNumber;
	
	private int dataP1;
	private int timestampP1;
	private int dataP2; 
	private int timestampP2;
	
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
	 * @return the packetNumber
	 */
	public int getPacketNumber() {
		return packetNumber;
	}
	/**
	 * @param packetNumber the packetNumber to set
	 */
	public void setPacketNumber(int packetNumber) {
		this.packetNumber = packetNumber;
	}
	
	/**
	 * @return the dataP1
	 */
	public int getDataP1() {
		return dataP1;
	}
	/**
	 * @param dataP1 the dataP1 to set
	 */
	public void setDataP1(int dataP1) {
		this.dataP1 = dataP1;
	}
	/**
	 * @return the timestampP1
	 */
	public int getTimestampP1() {
		return timestampP1;
	}
	/**
	 * @param timestampP1 the timestampP1 to set
	 */
	public void setTimestampP1(int timestampP1) {
		this.timestampP1 = timestampP1;
	}
	/**
	 * @return the dataP2
	 */
	public int getDataP2() {
		return dataP2;
	}
	/**
	 * @param dataP2 the dataP2 to set
	 */
	public void setDataP2(int dataP2) {
		this.dataP2 = dataP2;
	}
	/**
	 * @return the timestampP2
	 */
	public int getTimestampP2() {
		return timestampP2;
	}
	/**
	 * @param timestampP2 the timestampP2 to set
	 */
	public void setTimestampP2(int timestampP2) {
		this.timestampP2 = timestampP2;
	}
	public String toString() {
		return "LogPacketInputMessage: [dataType=" + dataType + "; packetNumber=" + packetNumber +
				"; dataP1=" + dataP1 + "; timestampP1=" + timestampP1 +
				"; dataP2=" + dataP2 + "; timestampP2=" + timestampP2 +"]";
	}

}
