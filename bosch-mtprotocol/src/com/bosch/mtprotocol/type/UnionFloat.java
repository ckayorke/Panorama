/**
 * 
 */
package com.bosch.mtprotocol.type;

import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;



/**
 * @author AndrejsC
 *
 */
@SuppressWarnings("serial")
public class UnionFloat extends BitField {

	public Field ui8Byte0 = new Field(this, 8);
	public Field ui8Byte1 = new Field(this, 8);
	public Field ui8Byte2 = new Field(this, 8);
	public Field ui8Byte3 = new Field(this, 8);
	
	public float getFloat() {
		return Float.intBitsToFloat(super.getValue());
	}
	
	public void setFloat(float floatValue) {
		super.setValue(Float.floatToIntBits(floatValue));
	}

}