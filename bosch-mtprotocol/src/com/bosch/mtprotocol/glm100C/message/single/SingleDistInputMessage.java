package com.bosch.mtprotocol.glm100C.message.single;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for input message as 
 * described in LRF command 64
 * Get single distance measurement
 * 
 * @author tos2si1
 */
public class SingleDistInputMessage implements MtMessage {
	
	private int distance;

	/**
	 * @return the distance as raw integer
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	/**
	 * @return the distance as float in meter
	 */
	public float getDistanceInMeter() {
		return (float) (distance * 50) / 1000000;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SingleDistInputMessage [distance=" + distance + "]";
	}
}
