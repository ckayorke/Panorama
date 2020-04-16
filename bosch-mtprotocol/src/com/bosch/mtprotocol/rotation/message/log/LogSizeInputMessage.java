package com.bosch.mtprotocol.rotation.message.log;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for input message as 
 * described in command 105
 * Get Log Size in Packets
 * 
 * @author tos2si1
 */
public class LogSizeInputMessage implements MtMessage {
	
	private int calibStoredSize;

	/**
	 * @return the calibStoredSize
	 */
	public int getCalibStoredSize() {
		return calibStoredSize;
	}

	/**
	 * @param calibStoredSize the calibStoredSize to set
	 */
	public void setCalibStoredSize(int calibStoredSize) {
		this.calibStoredSize = calibStoredSize;
	}
	
	public String toString() {
		return "LogSizeInputMessage: [calibStoredSize=" + calibStoredSize + "]";
	}

}
