/**
 * 
 */
package com.bosch.mtprotocol.glm100C.frame;

import com.bosch.mtprotocol.type.UnionStatusByte;



/**
 * @author AndrejsC
 *
 */
public class MtResponseFrame extends MtBaseFrame {

	private UnionStatusByte unStatusByte = new UnionStatusByte();
	private int frameFormat;
	
	public MtResponseFrame(int bufferSize) {
		super(bufferSize);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the frameFormat
	 */
	public int getFrameFormat() {
		return frameFormat;
	}

	/**
	 * @param frameFormat the frameFormat to set
	 */
	public void setFrameFormat(int frameFormat) {
		this.frameFormat = frameFormat;
	}

	/**
	 * @return the unStatusByte
	 */
	public UnionStatusByte getStatusByte() {
		return unStatusByte;
	}

	/**
	 * @param statusByte the statusByte to set
	 */
	public void setStatusByte(UnionStatusByte statusByte) {
		this.unStatusByte = statusByte;
	}

}
