package com.bosch.mtprotocol.thermo.message.imginfo;

import com.bosch.mtprotocol.MtMessage;

public class ImgInfoInputMessage implements MtMessage {
	// number of pixels X & Y 
	private int pixelsX;
	private int pixelsY;

	// size of JPEG image in bytes
	private int imgSize;
	
	// max size of data block
	private int maxSizeDataBlock;

	// unique ID for every measurement
	// if two packets have the same ID, they are parts of same measurement
	private int measID; 
	
	public String toString() {
		return "SyncInputMessage [measID=" + measID
				+ ", pixelsX=" + pixelsX + ", pixelsY=" + pixelsY 
				+ ", Image Size=" + imgSize + ", Max Size Data Block=" + maxSizeDataBlock + "]";
	}

	/**
	 * @return the pixelsX
	 */
	public int getPixelsX() {
		return pixelsX;
	}

	/**
	 * @param pixelsX the pixelsX to set
	 */
	public void setPixelsX(int pixelsX) {
		this.pixelsX = pixelsX;
	}

	/**
	 * @return the pixelsY
	 */
	public int getPixelsY() {
		return pixelsY;
	}

	/**
	 * @param pixelsY the pixelsY to set
	 */
	public void setPixelsY(int pixelsY) {
		this.pixelsY = pixelsY;
	}

	/**
	 * @return the imgSize
	 */
	public int getImgSize() {
		return imgSize;
	}

	/**
	 * @param imgSize the imgSize to set
	 */
	public void setImgSize(int imgSize) {
		this.imgSize = imgSize;
	}

	/**
	 * @return the maxSizeDataBlock
	 */
	public int getMaxSizeDataBlock() {
		return maxSizeDataBlock;
	}

	/**
	 * @param maxSizeDataBlock the maxSizeDataBlock to set
	 */
	public void setMaxSizeDataBlock(int maxSizeDataBlock) {
		this.maxSizeDataBlock = maxSizeDataBlock;
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
}
