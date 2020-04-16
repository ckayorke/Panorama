/**
 * 
 */
package com.bosch.mtprotocol.glm100C.frame;

import com.bosch.mtprotocol.type.UnionFrameMode;



/**
 * @author AndrejsC
 *
 */
public class MtRequestFrame extends MtBaseFrame {

	private UnionFrameMode unFrameMode = new UnionFrameMode();
	
	public MtRequestFrame(int bufferSize) {
		super(bufferSize);
	}

	/**
	 * @return the unFrameMode
	 */
	public UnionFrameMode getFrameMode() {
		return unFrameMode;
	}

	/**
	 * @param frameMode the unFrameMode to set
	 */
	public void setFrameMode(int frameMode) {
		unFrameMode.setValue(frameMode);
	}

}
