/**
 * 
 */
package com.bosch.mtprotocol.util;

/** 
 * Class for casting numeric variables to larger sizes while
 * treating the variable as unsigned
 * 
 */
public class CastUtil {

	public static short uByteToShort(byte b) {
		return (short)((int)b & 0xff);
	}
	
	public static int uByteToInt(byte b) {
		return (int)uByteToShort(b);
	}
	
	public static long uByteToLong(byte b) {
		return (long)uByteToShort(b);
	}
	
	public static int uShortToInt(short s) {
		return (int)((int)s & 0xffff);
	}
	
	public static long uShortToLong(short s) {
		return (long)uShortToInt(s);
	}
	
	public static long uIntToLong(int i) {
		return (long)((long)i & 0xffffffff);
	}
	
}
