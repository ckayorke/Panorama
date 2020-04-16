package com.bosch.mtprotocol.general.message.peer;

import com.bosch.mtprotocol.MtMessage;

/**
 * Output message structure for set active peer message
 * as described in command 61
 * 
 * @author tos2si1
 */
public class SetActivePeerOutputMessage implements MtMessage {
	
	private int enumActivePeer;
	private int comActivityStatus;
	private int byteChannelNumber;
	/**
	 * @return the enumActivePeer
	 */
	public int getEnumActivePeer() {
		return enumActivePeer;
	}
	/**
	 * @param enumActivePeer the enumActivePeer to set
	 */
	public void setEnumActivePeer(int enumActivePeer) {
		this.enumActivePeer = enumActivePeer;
	}
	/**
	 * @return the comActivityStatus
	 */
	public int getComActivityStatus() {
		return comActivityStatus;
	}
	/**
	 * @param comActivityStatus the comActivityStatus to set
	 */
	public void setComActivityStatus(int comActivityStatus) {
		this.comActivityStatus = comActivityStatus;
	}
	/**
	 * @return the byteChannelNumber
	 */
	public int getByteChannelNumber() {
		return byteChannelNumber;
	}
	/**
	 * @param byteChannelNumber the byteChannelNumber to set
	 */
	public void setByteChannelNumber(int byteChannelNumber) {
		this.byteChannelNumber = byteChannelNumber;
	}
	/*
	 * @return human readable string description 
	 */
	public String toString() {
		return "SetActivePeerOutputMessage: [EnumActivePeer=" + enumActivePeer 
				+ "; ComActivityStatus=" + comActivityStatus 
				+ "; ByteChannelNumber=" + byteChannelNumber + "]";
	}
}
