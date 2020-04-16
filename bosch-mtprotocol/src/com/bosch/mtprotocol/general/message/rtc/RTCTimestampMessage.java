package com.bosch.mtprotocol.general.message.rtc;

import com.bosch.mtprotocol.MtMessage;

/**
 * Real time clock timestamp message
 * The timestamp is a 4 byte (32 bit) integer, 
 * that contains the seconds since 
 * 01.01.1970 00:00
 * 
 * Create timestamp in Date format calling the 
 * constructor new Date((long) rtcTimestamp * 1000);
 * 
 * @author tos2si1
 */
public class RTCTimestampMessage implements MtMessage {
	
	private int rtcTimestamp;

	/**
	 * @return the rtcTimestamp
	 */
	public int getRtcTimestamp() {
		return rtcTimestamp;
	}

	/**
	 * @param rtcTimestamp the rtcTimestamp to set
	 */
	public void setRtcTimestamp(int rtcTimestamp) {
		this.rtcTimestamp = rtcTimestamp;
	}
	
	public String toString() {
		return "Seconds since epoch " + rtcTimestamp;
	}

}
