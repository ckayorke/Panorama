/**
 * 
 */
package com.bosch.mtprotocol;


/**
 * @author AndrejsC
 *
 */
public interface MtFrameFactory {

	/**
	 * @param message
	 * @return
	 */
	public MtFrame createFrame(MtMessage message);

}
