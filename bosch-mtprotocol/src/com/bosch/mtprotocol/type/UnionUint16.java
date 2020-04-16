/**
 * 
 */
package com.bosch.mtprotocol.type;

import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;




//Bitfield class for 16 bit integer (short)
@SuppressWarnings("serial")
public class UnionUint16 extends BitField {
	public Field ui8LSB = new Field(this, 8);
	public Field ui8MSB = new Field(this, 8);
	
	public short getShort() {
		return (short)super.getValue();
	}
}
