/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.laser;

import com.bosch.mtprotocol.glm100C.message.SimpleMessage;



/**
 * @author AndrejsC
 *
 */
public class LaserOffMessage extends SimpleMessage {

	public LaserOffMessage() {
		super((byte) 66);
	}

}
