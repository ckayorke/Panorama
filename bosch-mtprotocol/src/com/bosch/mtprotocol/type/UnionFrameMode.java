/**
 * 
 */
package com.bosch.mtprotocol.type;

import com.bosch.mtprotocol.type.field.BitField;
import com.bosch.mtprotocol.type.field.Field;



//Bitfield class for Mode-Byte of the frame. Used together with enumFrameModes. 
//Also adjust offsets in enumFrameModes, when changing position of fields!
@SuppressWarnings("serial")
public class UnionFrameMode extends BitField {
	
	public Field ui2ResponseFrameFormat = new Field(this, 2);
	public Field ui2RequestFrameFormat = new Field(this, 2);
	public Field ui2Reserved = new Field(this, 2);
	public Field ui2FrameType = new Field(this, 2);
	
}
