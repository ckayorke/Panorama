package com.bosch.mtprotocol;

/**
 * @author AndrejsC
 *
 */
public interface MtMessageFactory {

	/**
	 * @param frame
	 * @return
	 */
	public MtMessage createMessage(MtFrame frame);
	
}
