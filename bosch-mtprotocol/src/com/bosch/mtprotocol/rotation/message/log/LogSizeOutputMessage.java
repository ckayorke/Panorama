package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for output message as 
 * described in commands 105
 * Get Log Size inPackets
 * 
 * @author tos2si1
 */
public class LogSizeOutputMessage implements MtMessage {
	
	public static final int TYPE_OF_DATA_TEMPERATURE = 0;
	public static final int TYPE_OF_DATA_POWER_ON_OFF = 1;
	
	private int dataType;
	private int timestampLastSync;
	
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
	 * @return the timestampLastSync
	 */
	public int getTimestampLastSync() {
		return timestampLastSync;
	}
	/**
	 * @param timestampLastSync the timestampLastSync to set
	 */
	public void setTimestampLastSync(int timestampLastSync) {
		this.timestampLastSync = timestampLastSync;
	}
	 public String toString() {
		 return "LogSizeOutputMessage: [dataType=" + dataType + "; timestampLastSync=" + timestampLastSync + "]";
	 }

}
