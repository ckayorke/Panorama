package com.bosch.mtprotocol.glm100C.message.single;

import com.bosch.mtprotocol.MtMessage;

/**
 * Message structure for output message as 
 * described in LRF command 64
 * Get single distance
 * 
 * @author tos2si1
 */
public class SingleDistOutputMessage implements MtMessage{
	
	private int refEdge;
	private int measTime;
	private int measType;
	
	/**
	 * @return the refEdge
	 */
	public int getRefEdge() {
		return refEdge;
	}
	/**
	 * @param refEdge the refEdge to set
	 */
	public void setRefEdge(int refEdge) {
		this.refEdge = refEdge;
	}
	/**
	 * @return the measTime
	 */
	public int getMeasTime() {
		return measTime;
	}
	/**
	 * @param measTime the measTime to set
	 */
	public void setMeasTime(int measTime) {
		this.measTime = measTime;
	}
	/**
	 * @return the measType
	 */
	public int getMeasType() {
		return measType;
	}
	/**
	 * @param measType the measType to set
	 */
	public void setMeasType(int measType) {
		this.measType = measType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SingleDistOutputMessage [referenceEdge = " + refEdge + 
				"; measurementTime = " + measTime + "; measurementType = " + measType + "]";
	}
}
