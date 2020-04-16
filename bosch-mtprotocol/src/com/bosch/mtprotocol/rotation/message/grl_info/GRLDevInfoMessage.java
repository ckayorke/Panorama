package com.bosch.mtprotocol.rotation.message.grl_info;

import com.bosch.mtprotocol.MtMessage;

/**
 * Response message of rotation laser command 101 (Get Device Info)
 * Contains 6 bytes like described below
 * 
 * GRLDevInfoMessage[size = 6]
 * 
 * GRLDevInfoMessage[0,1] = Manufacturing year (e.g. "2015")
 * GRLDevInfoMessage[2] = Device variant (H/HV)
 * GRLDevInfoMessage[3,4] = SW revision as 16 bit integer 
 * GRLDevInfoMessage[5] = Reserved (set to 0)
 *  
 * @author tos2si1
 */
public class GRLDevInfoMessage implements MtMessage {
	
	public final static int DEV_VARIANT_H = 0;
	public final static int DEV_VARIANT_HV = 1;
	
	private int manufacturingYear;
	private int devVariant;
	private int swRevision;
	
	/**
	 * @return the manufacturingYear
	 */
	public int getManufacturingYear() {
		return manufacturingYear;
	}
	/**
	 * @param manufacturingYear the manufacturingYear to set
	 */
	public void setManufacturingYear(int manufacturingYear) {
		this.manufacturingYear = manufacturingYear;
	}
	/**
	 * @return the devVariant
	 */
	public int getDevVariant() {
		return devVariant;
	}
	/**
	 * @param devVariant the devVariant to set
	 */
	public void setDevVariant(int devVariant) {
		this.devVariant = devVariant;
	}
	/**
	 * @return the swRevision
	 */
	public int getSwRevision() {
		return swRevision;
	}
	/**
	 * @param swRevision the swRevision to set
	 */
	public void setSwRevision(int swRevision) {
		this.swRevision = swRevision;
	}
	
	public String toString() {
		return "GRLDevInfoMessage: [manufacturingYear=" + manufacturingYear 
				+ "; devVariant=" + devVariant + "; swRevision=" + swRevision + "]";
	}
	
}
