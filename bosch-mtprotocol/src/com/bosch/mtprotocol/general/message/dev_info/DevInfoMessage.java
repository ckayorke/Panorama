package com.bosch.mtprotocol.general.message.dev_info;

import com.bosch.mtprotocol.MtMessage;

/**
 * Response message of generic command 6 (Get Device Info)
 * Contains 29 bytes like described below
 * 
 * DevInfoMessage[size = 29]
 * 
 * DevInfoMessage[0,1,2,3] = Date Code as month based 3 byte ASCII + EOS
 * DevInfoMessage[4,5,6,7] = Serial Number as 32 bit integer
 * DevInfoMessage[8,9] = SW revision as 16 bit integer 
 * DevInfoMessage[10,11,12] = SW version (see below)
 * DevInfoMessage[13,14,15] = HW version (see below)
 * DevInfoMessage[16 to 28] = BOSCH Part Number (TTNr) as 12 byte ASCII + EOS
 * 
 * SW and HW version are 3 byte in decimal format, where byte1=main,
 * byte2=sub, byte3=bug,
 * Hint: 0xFF.FF.FF is invalid version. 
 * HW Version: main = main PCB version, sub = main PCB variant (if available)
 *  
 * @author tos2si1
 */
public class DevInfoMessage implements MtMessage {
	
	private String dateCode;
	private int serialNumber;
	private int swRevision;
	private int swVersionMain;
	private int swVersionSub;
	private int swVersionBug;
	private int hwPCBVersion;
	private int hwPCBVariant;
	private int hwPCBBug;
	private String partNumberTTNr;
	
	/**
	 * @return the dateCode
	 */
	public String getDateCode() {
		return dateCode;
	}

	/**
	 * @param dateCode the dateCode to set
	 */
	public void setDateCode(String dateCode) {
		this.dateCode = dateCode;
	}

	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
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

	/**
	 * @return the swVersionMain
	 */
	public int getSwVersionMain() {
		return swVersionMain;
	}

	/**
	 * @param swVersionMain the swVersionMain to set
	 */
	public void setSwVersionMain(int swVersionMain) {
		this.swVersionMain = swVersionMain;
	}

	/**
	 * @return the swVersionSub
	 */
	public int getSwVersionSub() {
		return swVersionSub;
	}

	/**
	 * @param swVersionSub the swVersionSub to set
	 */
	public void setSwVersionSub(int swVersionSub) {
		this.swVersionSub = swVersionSub;
	}

	/**
	 * @return the swVersionBug
	 */
	public int getSwVersionBug() {
		return swVersionBug;
	}

	/**
	 * @param swVersionBug the swVersionBug to set
	 */
	public void setSwVersionBug(int swVersionBug) {
		this.swVersionBug = swVersionBug;
	}

	/**
	 * @return the hwPCBVersion
	 */
	public int getHwPCBVersion() {
		return hwPCBVersion;
	}

	/**
	 * @param hwPCBVersion the hwPCBVersion to set
	 */
	public void setHwPCBVersion(int hwPCBVersion) {
		this.hwPCBVersion = hwPCBVersion;
	}

	/**
	 * @return the hwPCBVariant
	 */
	public int getHwPCBVariant() {
		return hwPCBVariant;
	}

	/**
	 * @param hwPCBVariant the hwPCBVariant to set
	 */
	public void setHwPCBVariant(int hwPCBVariant) {
		this.hwPCBVariant = hwPCBVariant;
	}

	/**
	 * @return the hwPCBBug
	 */
	public int getHwPCBBug() {
		return hwPCBBug;
	}

	/**
	 * @param hwPCBBug the hwPCBBug to set
	 */
	public void setHwPCBBug(int hwPCBBug) {
		this.hwPCBBug = hwPCBBug;
	}

	/**
	 * @return the partNumberTTNr
	 */
	public String getPartNumberTTNr() {
		return partNumberTTNr;
	}

	/**
	 * @param partNumberTTNr the partNumberTTNr to set
	 */
	public void setPartNumberTTNr(String partNumberTTNr) {
		this.partNumberTTNr = partNumberTTNr;
	}

	public String toString() {
		return "Date Code = " + dateCode + "; Serial Number = " + serialNumber + "; SW Revision = " + swRevision 
				+ "; SW Version = " + swVersionMain + "." + swVersionSub + "." + swVersionBug
				+ "; HW Version = " + hwPCBVersion + "." + hwPCBVariant + "." + hwPCBBug 
				+ "; TTNr = " + partNumberTTNr;
	}
}
