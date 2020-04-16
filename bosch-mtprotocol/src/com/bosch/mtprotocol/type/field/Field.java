/**
 * 
 */
package com.bosch.mtprotocol.type.field;

public class Field {

	private static final int MAX_FIELD_LENGTH = 32; //length of int in bits

	private final int position;
	private final int length;
	private long value = 0L; //also OK if public

	/** Get position of field bits in bitfield.
	 * First field starts at 0.
	 * Further fields start at position of last field + length of last field.
	 */
	public int getPosition() {
		return position;
	}

	/** get length of field, that was set in constructor */
	public int getLength() {
		return length;
	}

	public Field(BitField parent, int length) {
		if (length > MAX_FIELD_LENGTH) {
			throw new IllegalArgumentException("Maximum length of field is " + MAX_FIELD_LENGTH + " bits.");
		}
		if (length < 1) {
			throw new IllegalArgumentException("Minimum length of field is 1 bit.");
		}
		this.position = parent.nextFieldPosition;
		this.length = length;
		parent.nextFieldPosition += length;
		parent.add(this);
	}

	public byte getByte() {
		return (byte)value;
	}

	public void setByte(byte byteValue) {
		this.value = (long)byteValue & 0xffL; // convert from byte (binary representation treated as unsigned) to long
	}

	public short getShort() {
		return (short)value;
	}

	public void setShort(short value) {
		this.value = (long)value & 0xffffL; // convert from short (binary representation treated as unsigned) to long
	}

	/** Get value of field as a int. The returned int will be signed negative
	 * if the 32th bit of the field is set.
	 * Bits that are not part of the field are returned as 0.
	 */
	public int getValue() {
		return (int)value;
	}

	/**Set value of field from int. Bits are copied 1:1.
	 * Higher bits that are not part of the field get cut off.
	 * The sign of the int gets interpreted as the 32nd bit of bitfield (two's complement).<br>
	 * <br>
	 * Example with negative byte instead of negative int for clearer view:<br>
	 * 211 == 11010011 in binary<br>
	 * byte -45 == 11010011 in binary (two's complement)
	 */
	public void setValue(int value) {
		this.value = (long)value & 0xffffffffL; // convert from int (binary representation treated as unsigned) to long
	}

	/** Get value of field as a long. The returned long will be signed negative
	 * if the 64th bit of the field is set.
	 * Bits that are not part of the field are returned as 0.
	 */
	public long getLong() {
		return value;
	}

	/**Set value of field from int. Bits are copied 1:1.
	 * Higher bits that are not part of the field get cut off.
	 * The sign of the long gets interpreted as the 64nd bit of bitfield (two's complement).<br>
	 */
	public void setLong(long value) {
		this.value = value;
	}

	/** Get value of field as a IEEE 754 float. 
	 * Field needs to be 32 bits long for the correct float value to be returned.*/
	public float getFloat() {
		return Float.intBitsToFloat((int)value);
	}

	/** Set value of field as a IEEE 754 float.
	 * Field needs to be 32 bits long for the correct float value to be stored.*/
	public void setFloat(float value) {
		this.value =  (long)Float.floatToIntBits(value);
	}

	/** Get value of field as a IEEE 754 double.
	 * Field needs to be 64 bits long for the correct double value to be returned.*/
	public double getDouble() {
		return Double.longBitsToDouble(value);
	}

	/** Set value of field as a IEEE 754 double.
	 * Field needs to be 64 bits long for the correct double value to be stored.*/
	public void setDouble(double value) {
		this.value = Double.doubleToLongBits(value);
	}

	public boolean isSet() {
		return (value != 0);
	}

	/** Get value of field bits left-shifted by the position of the field
	 * (padded with 0s). The value of the bitfield is the shifted values of
	 * all fields ORed together.
	 */	
	public long getShiftedValue() {
		long mask = ~(-1L << length);
		return (value & mask) << position;
	}

	/** Sets value of field from bits left-shifted by the position of the field.
	 * Other bits are masked and don't change the value.
	 */	
	public void setShiftedValue(long shiftedValue) {
		long mask = ~(-1L << length);
		value = (shiftedValue >>> position) & mask;
	}

	/** Get binary String representation of Bitfield */
	public String toString() {
		return "position: " + position + ", length: " + length + ", data: " + Long.toBinaryString(getLong()) + ", shifted data: " +  Long.toBinaryString(getShiftedValue());
	}
}
