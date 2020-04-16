/**
 * 
 */
package com.bosch.mtprotocol.type.field;


import java.util.ArrayList;

/** Class designed as replacement for C/C++ Unions when those are used for accessing
 * the same variable as multiple groups (fields) of bits and as a single primitive datatype.<br>
 * <br>
 * Example:<br>
 * <br>
 * 	C, C++:
 * <pre>
 *{@code
 *typedef union
 *{
 *  UINT8 ui8Value;
 *  struct
 *  {
 *    UINT8 ui2ResponseFrameFormat  : 2;
 *    UINT8 ui2RequestFrameFormat   : 2;
 *    UINT8 ui2Reserved             : 2;
 *    UINT8 ui2FrameType            : 2;
 *  }Fields;
 *}unionFrameMode;
 *}
 * </pre>
 * 
 *	Java:
 *<pre>
 *{@code
 *public class UnionFrameMode extends BitField {
 *  public Field ui2ResponseFrameFormat = new Field(this, 2);
 *  public Field ui2RequestFrameFormat = new Field(this, 2);
 *  public Field ui2Reserved = new Field(this, 2);
 *  public Field ui2FrameType = new Field(this, 2);
 *}
 *
 *UnionFrameMode myUnionFrameMode = new UnionFrameMode();
 *}
 *</pre>
 *	Field length is set when calling the constructor.
 *
 * @author Alexander Metz
 *
 */

public abstract class BitField extends ArrayList<Field> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6083581312837298413L;
	protected int nextFieldPosition = 0;

	public BitField() {}
	
	/** new BitField
	 * 
	 * @param byteValue initial value
	 */
	public BitField(byte byteValue) {
		setByte(byteValue);
	}
	
	/** new BitField
	 * 
	 * @param value initial value
	 */
	public BitField(int value) {
		setValue(value);
	}
	
	/** Get value of bitfield as a byte. The returned byte may be signed negative
	 * if the 8th bit of the bitfield is set. Higher bits get cut off.
	 */
	public byte getByte() {
		return (byte)getValue();
	}

	/** Set value of bitfield from byte. Bits are copied 1:1.
	 * Bits that are not used by any fields get cut off.
	 * The sign of the byte gets interpreted as the 8th bit of bitfield (two's complement).<br>
	 * <br>
	 * Example with negative byte:<br>
	 * 211 == 11010011 in binary<br>
	 * byte -45 == 11010011 in binary (two's complement)
	 */
	public void setByte(byte byteValue) {
		long value = (long)byteValue & 0xffL; // convert from byte (binary representation treated as unsigned) to long
		setLong(value);
	}
	
	public short getShort() {
		return (short)getValue();
	}

	public void setShort(short shortValue) {
		long value = (long)shortValue & 0xffffL; // convert from short (binary representation treated as unsigned) to long
		setLong(value);
	}
	
	/** Get value of bitfield as a int. The returned int will be signed negative
	 * if the 32th bit of the bitfield is set. Higher bits get cut off.
	 */
	public int getValue() {
		return (int)getLong();
	}
	
	/**Set value of bitfield from int. Bits are copied 1:1.
	 * Bits that are not used by any fields get cut off.
	 * The sign of the int gets interpreted as the 32nd bit of bitfield (two's complement).<br>
	 * <br>
	 * Example with negative byte instead of negative int for clearer view:<br>
	 * 211 == 11010011 in binary<br>
	 * byte -45 == 11010011 in binary (two's complement)
	 */
	public void setValue(int intValue) {
		long value = (long)intValue & 0xffffffffL; // convert from int (binary representation treated as unsigned) to long
		setLong(value);
	}
	
	public long getLong() {
		long value = 0L;
		for (Field field : this) {
			value |= field.getShiftedValue();
		}
		return value;
	}
	
	public void setLong(long value) {
		for (Field field : this) {
			field.setShiftedValue(value);
		}
	}
	
	/** Get binary String representation of bitfield */
	public String toString() {
		return Integer.toBinaryString(getValue());
	}
	
}
