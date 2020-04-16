/**
 * 
 */
package com.bosch.mtprotocol.thermo.message.imgdata;


import com.bosch.mtprotocol.MtMessage;

/**
 * @author tos2si1
 *
 */
public class ImgDataInputMessage implements MtMessage, Comparable<Object> {

	// consequent package index
	private int packageIndex;

	// unique ID for every measurement
	// if two packets have the same ID, they are parts of same measurement
	private int measID; 

	//defines the last datapackage of the transmitted image
	private boolean lastPackage = false;

	public boolean isLastPackage() {
		return lastPackage;
	}

	public void setLastPackage(boolean lastPackage) {
		this.lastPackage = lastPackage;
	}
	
	// image data array of concatenated bytes
	private byte[] imgData;

	public String toString() {
		return "ImgDataMessage [measID=" + measID
				+ ", package index=" + packageIndex
				+ ", imageData length=" + (imgData == null ? "0" :Integer.toString(imgData.length)) + "]";
	}

	/**
	 * @return the packageIndex
	 */
	public int getPackageIndex() {
		return packageIndex;
	}

	/**
	 * @param packageIndex the packageIndex to set
	 */
	public void setPackageIndex(int packageIndex) {
		this.packageIndex = packageIndex;
	}

	/**
	 * @return the measID
	 */
	public int getMeasID() {
		return measID;
	}

	/**
	 * @param measID the measID to set
	 */
	public void setMeasID(int measID) {
		this.measID = measID;
	}

	/**
	 * @return the imgData
	 */
	public byte[] getImgData() {
		return imgData;
	}

	/**
	 * @param imgData the imgData to set
	 */
	public void setImgData(byte[] imgData) {
		this.imgData = imgData;
	}

	@Override
	public int compareTo(Object imgMsg) {
		int comparege = ((ImgDataInputMessage) imgMsg).getPackageIndex();
		return packageIndex-comparege;
	}
}
