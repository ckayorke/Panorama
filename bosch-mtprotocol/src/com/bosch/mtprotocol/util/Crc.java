/**
 * 
 */
package com.bosch.mtprotocol.util;


/** Java port of C++ class Crc.cpp.
 *
 * @author C. Amann(original C++), A. Metz(Java port)
 * 
 */
public class Crc {
	
//	//====================================================================
//	//  Global Enumerations
//	//====================================================================
//	//Use these values to initialize your temporary variables where the CRC is stored. 
//	//All Bytes must be of the same value, to ensure that e.g. a UINT16 temporary can be initialized 
//	//and used for CRC8 as well as CRC16 without corrupting it when casted to UINT8 for use with CalcCrc8.
//	enum { EN_CRC8_INITIAL_VALUE  = (UINT8)  0xAA }; //<- Adjust only this value!
//	enum { EN_CRC16_INITIAL_VALUE = (UINT16) (EN_CRC8_INITIAL_VALUE | (EN_CRC8_INITIAL_VALUE << 8)) };
//	enum { EN_CRC32_INITIAL_VALUE = (UINT32) (EN_CRC16_INITIAL_VALUE | ((UINT32) EN_CRC16_INITIAL_VALUE << 16)) };
	
//	private static int CRC_INITIAL_VALUE_TEMP = 0xAA; //<- Adjust only this value!
//	public static byte EN_CRC8_INITIAL_VALUE  = (byte)CRC_INITIAL_VALUE_TEMP; 
//	public static short EN_CRC16_INITIAL_VALUE = (short)(CRC_INITIAL_VALUE_TEMP | (CRC_INITIAL_VALUE_TEMP << 8));
//	public static int EN_CRC32_INITIAL_VALUE = (int)(CRC_INITIAL_VALUE_TEMP | (CRC_INITIAL_VALUE_TEMP << 8) | (CRC_INITIAL_VALUE_TEMP << 16) | (CRC_INITIAL_VALUE_TEMP << 24));

	static {
		setCRCInitialValue(0xAA); //<- Adjust only this value!
	}

	public static byte EN_CRC8_INITIAL_VALUE; 
	public static short EN_CRC16_INITIAL_VALUE;
	public static int EN_CRC32_INITIAL_VALUE;
	
	public static void setCRCInitialValue(int initialValue) {
		EN_CRC8_INITIAL_VALUE  = (byte)initialValue; 
		EN_CRC16_INITIAL_VALUE = (short)(initialValue | (initialValue << 8));
		EN_CRC32_INITIAL_VALUE = (int)(initialValue | (initialValue << 8) | (initialValue << 16) | (initialValue << 24));
	}
	
//	//====================================================================
//	//  Global Functions: Calc
//	//====================================================================
//	UINT8 CalcCrc8(UINT8  f_ui8Data, UINT8 f_ui8InitialValue = EN_CRC8_INITIAL_VALUE, UINT8 f_ui8Polynomial = 0xA6);  //Single value
	public static byte calcCrc8(byte data, byte initialValue) {
		return calcCrc8(data, initialValue, (byte)0xA6);
	}
	public static byte calcCrc8(byte data) {
		return calcCrc8(data, EN_CRC8_INITIAL_VALUE);
	}
	
//	UINT8 CalcCrc8(const UINT8* f_pui8Data, UINT16 f_ui16NumElements, UINT8 f_ui8InitialValue = EN_CRC8_INITIAL_VALUE, UINT8 f_ui8Polynomial = 0xA6); //Array
	public static byte calcCrc8(byte[] data, byte initialValue) {
		return calcCrc8(data, initialValue, (byte)0xA6);
	}
	public static byte calcCrc8(byte[] data) {
		return calcCrc8(data, EN_CRC8_INITIAL_VALUE);
	}

//	UINT16 CalcCrc16(UINT8  f_ui8Data, UINT16 f_ui16InitialValue = EN_CRC16_INITIAL_VALUE, UINT16 f_ui16Polynomial = 0xBAAD); //Single value
	public static short calcCrc16(byte data, short initialValue) {
		return calcCrc16(data, initialValue, (short)0xBAAD);
	}
	public static short calcCrc16(byte data) {
		return calcCrc16(data, EN_CRC16_INITIAL_VALUE);
	}
	
//	UINT16 CalcCrc16(const UINT8* f_pui8Data, UINT16 f_ui16NumBytes, UINT16 f_ui16InitialValue = EN_CRC16_INITIAL_VALUE, UINT16 f_ui16Polynomial = 0xBAAD);  //Array
	public static short calcCrc16(byte[] data, short initialValue) {
		return calcCrc16(data, initialValue, (short)0xBAAD);
	}
	public static short calcCrc16(byte[] data) {
		return calcCrc16(data, EN_CRC16_INITIAL_VALUE);
	}	
	
//	UINT32 CalcCrc32(UINT8  f_ui8Data, UINT32 f_ui32InitialValue = EN_CRC32_INITIAL_VALUE, UINT32 f_ui32Polynomial = 	0x04C11DB7); //Single value
	public static int calcCrc32(byte data, int initialValue) {
		return calcCrc32(data, initialValue, (int)0x04C11DB7);
	}
	public static int calcCrc32(byte data) {
		return calcCrc32(data, EN_CRC32_INITIAL_VALUE);
	}
	
//	UINT32 CalcCrc32(const UINT8* f_pui8Data, UINT32 f_ui32NumBytes, UINT32 f_ui32InitialValue = EN_CRC32_INITIAL_VALUE, UINT32 f_ui32Polynomial = 	0x04C11DB7); //Array
	public static int calcCrc32(byte[] data, int initialValue) {
		return calcCrc32(data, initialValue, (int)0x04C11DB7);
	}
	public static int calcCrc32(byte[] data) {
		return calcCrc32(data, EN_CRC32_INITIAL_VALUE);
	}	
	
//	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	//  INCLUDES
//	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	#include "Types.hpp"
//	#include "Crc.hpp"
//
//	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	//  COMPILE SWITCHES
//	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//
//	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	//  GLOBAL FUNCTION DEFINITIONS
//	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	//====================================================================
//	//  Global Functions: Calc
//	//====================================================================
//	//--------------------------------------------------------------------
//	/// \author: Amann                                                       
//	/// \date  29.09.2011                                             
//	/// \brief Calculate the CRC8 checksum for the given value.
//	///  No reflection, inversion, reversion or final XOR used. Just plain CRC8. 
//	/// \param f_ui8Data Value to calculate the CRC for.
//	/// \param f_ui8InitialValue The initial value for the CRC (e.g. previous calculated value).
//	/// \param f_ui8Polynomial The polynomial to use for the CRC.
//	//--------------------------------------------------------------------
//	UINT8 CalcCrc8(UINT8 f_ui8Data, UINT8 f_ui8InitialValue, UINT8 f_ui8Polynomial)
//	{
//		for (UINT8 i=0; i<8; i++)
//		{
//			if (((f_ui8InitialValue & 0x80) != 0) != ((f_ui8Data >> (7-i)) & 1))
//				f_ui8InitialValue = (f_ui8InitialValue << 1) ^ f_ui8Polynomial;
//			else
//				f_ui8InitialValue <<= 1;
//		}
//		return f_ui8InitialValue;
//	}
	/** Calculate the CRC8 checksum for the given value.<br>
	 * No reflection, inversion, reversion or final XOR used. Just plain CRC8.
	 * 
	 * @param f_ui8Data          Value to calculate the CRC for.
	 * @param f_ui8InitialValue  The initial value for the CRC (e.g. previous calculated value).
	 * @param f_ui8Polynomial    The polynomial to use for the CRC.
	 * 
	 */
	public static byte calcCrc8(byte f_ui8Data, byte f_ui8InitialValue, byte f_ui8Polynomial)
	{
		for (int i=0; i<8; i++)
		{
			if (((f_ui8InitialValue & 0x80) != 0) != (((f_ui8Data >>> (7-i)) & 1) == 1))
				f_ui8InitialValue = (byte)((f_ui8InitialValue << 1) ^ f_ui8Polynomial);
			else
				f_ui8InitialValue <<= 1;
		}
		return f_ui8InitialValue;
	}

//	//--------------------------------------------------------------------
//	/// \author: Amann                                                       
//	/// \date  29.09.2011                                             
//	/// \brief Calculate the CRC8 checksum for the given array.
//	///  No reflection, inversion, reversion or final XOR used. Just plain CRC8. 
//	/// \param f_ui8Data Pointer to the array to calculate the CRC for.
//	/// \param f_ui16NumBytes	Size of array in bytes.
//	/// \param f_ui8InitialValue The initial value for the CRC (e.g. previous calculated value).
//	/// \param f_ui8Polynomial The polynomial to use for the CRC.
//	//--------------------------------------------------------------------
//	UINT8 CalcCrc8(const UINT8* f_pui8Data, UINT16 f_ui16NumElements, UINT8 f_ui8InitialValue, UINT8 f_ui8Polynomial)
//	{
//		const UINT8* pui8Data = f_pui8Data;
//		const UINT8* pui8End = f_pui8Data + f_ui16NumElements;
//		while(pui8Data < pui8End)
//		{	
//			f_ui8InitialValue = CalcCrc8(*pui8Data++, f_ui8InitialValue, f_ui8Polynomial);
//		}
//		return f_ui8InitialValue;
//	}
	/** Calculate the CRC8 checksum for the given array.<br>
	 * No reflection, inversion, reversion or final XOR used. Just plain CRC8.
	 * 
	 * @param data          Byte array calculate the CRC for.
	 * @param initialValue  The initial value for the CRC (e.g. previous calculated value).
	 * @param polynomial    The polynomial to use for the CRC.
	 * 
	 */
	public static byte calcCrc8(byte[] data, byte initialValue, byte polynomial)
	{
		for (byte b : data)
		{	
			initialValue = calcCrc8(b, initialValue, polynomial);
		}
		return initialValue;
	}

//	//--------------------------------------------------------------------
//	/// \author: Amann                                                       
//	/// \date  29.09.2011                                             
//	/// \brief Calculate the CRC16 checksum for the given value.
//	///  No reflection, inversion, reversion or final XOR used. Just plain CRC16. 
//	/// \param f_ui8Data Value to calculate the CRC for.
//	/// \param f_ui16NumBytes	Size of array in bytes.
//	/// \param f_ui16InitialValue The initial value for the CRC (e.g. previous calculated value).
//	/// \param f_ui16Polynomial The polynomial to use for the CRC.
//	//--------------------------------------------------------------------
//	UINT16 CalcCrc16(UINT8 f_ui8Data, UINT16 f_ui16InitialValue, UINT16 f_ui16Polynomial)
//	{	
//		for (UINT8 i=0; i<8; ++i)
//		{
//			if (((f_ui16InitialValue & 0x8000) != 0) != ((f_ui8Data >> (7-i)) & 1))
//				f_ui16InitialValue = (f_ui16InitialValue << 1) ^ f_ui16Polynomial;
//			else
//				f_ui16InitialValue <<= 1;
//		}
//		return f_ui16InitialValue;
//	}
	/** Calculate the CRC16 checksum for the given value.<br>
	 * No reflection, inversion, reversion or final XOR used. Just plain CRC16.
	 * 
	 * @param data          Value to calculate the CRC for.
	 * @param initialValue  The initial value for the CRC (e.g. previous calculated value).
	 * @param polynomial    The polynomial to use for the CRC.
	 * 
	 */
	public static short calcCrc16(byte data, short initialValue, short polynomial)
	{	
		for (int i=0; i<8; ++i)
		{
			if (((initialValue & 0x8000) != 0) != (((data >>> (7-i)) & 1) == 1))
				initialValue = (short)((initialValue << 1) ^ polynomial);
			else
				initialValue <<= 1;
		}
		return initialValue;
	}

//	//--------------------------------------------------------------------
//	/// \author: Amann                                                       
//	/// \date  29.09.2011                                             
//	/// \brief Calculate the CRC16 checksum for the given array.
//	///  No reflection, inversion, reversion or final XOR used. Just plain CRC16. 
//	/// \param f_pui8Data Pointer to the array to calculate the CRC for.
//	/// \param f_ui16NumBytes	Size of array in bytes.
//	/// \param f_ui16InitialValue The initial value for the CRC (e.g. previous calculated value).
//	/// \param f_ui16Polynomial The polynomial to use for the CRC.
//	//--------------------------------------------------------------------
//	UINT16 CalcCrc16(const UINT8* f_pui8Data, UINT16 f_ui16NumBytes, UINT16 f_ui16InitialValue, UINT16 f_ui16Polynomial)
//	{	
//		const UINT8* pui8Data = f_pui8Data;
//		const UINT8* pui8End = f_pui8Data + f_ui16NumBytes;
//		while(pui8Data < pui8End)
//		{
//			f_ui16InitialValue = CalcCrc16(*pui8Data++, f_ui16InitialValue, f_ui16Polynomial);
//		}
//		return f_ui16InitialValue;
//	}
	/** Calculate the CRC16 checksum for the given array.<br>
	 * No reflection, inversion, reversion or final XOR used. Just plain CRC16.
	 * 
	 * @param data          Byte array to calculate the CRC for.
	 * @param initialValue  The initial value for the CRC (e.g. previous calculated value).
	 * @param polynomial    The polynomial to use for the CRC.
	 * 
	 */
	public static short calcCrc16(byte[] data, short initialValue, short polynomial)
	{	
		for (byte b : data)
		{	
			initialValue = calcCrc16(b, initialValue, polynomial);
		}
		return initialValue;
	}
//
//	//--------------------------------------------------------------------
//	/// \author: Amann                                                       
//	/// \date  29.09.2011                                             
//	/// \brief Calculate the CRC32 checksum for the given value.
//	///  No reflection, inversion, reversion or final XOR used. Just plain CRC32. 
//	/// \param f_ui8Data Value to calculate the CRC for.
//	/// \param f_ui32InitialValue The initial value for the CRC (e.g. previous calculated value).
//	/// \param f_ui32Polynomial The polynomial to use for the CRC.
//	//--------------------------------------------------------------------
//	UINT32 CalcCrc32(UINT8 f_ui8Data, UINT32 f_ui32InitialValue, UINT32 f_ui32Polynomial)
//	{	
//		for (UINT8 i=0; i<8; ++i)
//		{
//			if (((f_ui32InitialValue & 0x80000000) != 0) != ((f_ui8Data >> (7-i)) & 1))
//				f_ui32InitialValue = (f_ui32InitialValue << 1) ^ f_ui32Polynomial;
//			else
//				f_ui32InitialValue <<= 1;
//		}
//		return f_ui32InitialValue;
//	}
	/** Calculate the CRC32 checksum for the given value.<br>
	 * No reflection, inversion, reversion or final XOR used. Just plain CRC32.
	 * 
	 * @param data                Value to calculate the CRC for.
	 * @param initialValue  The initial value for the CRC (e.g. previous calculated value).
	 * @param polynomial    The polynomial to use for the CRC.
	 * 
	 */
	public static int calcCrc32(byte data, int initialValue, int polynomial)
	{	
		for (int i=0; i<8; ++i)
		{
			if (((initialValue & 0x80000000) != 0) != (((data >> (7-i)) & 1) == 1))
				initialValue = (int)(initialValue << 1) ^ polynomial;
			else
				initialValue <<= 1;
		}
		return initialValue;
	}
//
//	//--------------------------------------------------------------------
//	/// \author: Amann                                                       
//	/// \date  29.09.2011                                             
//	/// \brief Calculate the CRC32 checksum for the given array.
//	///  No reflection, inversion, reversion or final XOR used. Just plain CRC32. 
//	/// \param f_pui8Data Pointer to the array to calculate the CRC for.
//	/// \param f_ui32NumBytes	Size of array in bytes.
//	/// \param f_ui32InitialValue The initial value for the CRC (e.g. previous calculated value).
//	/// \param f_ui32Polynomial The polynomial to use for the CRC.
//	//--------------------------------------------------------------------
//	UINT32 CalcCrc32(const UINT8* f_pui8Data, UINT32 f_ui32NumBytes, UINT32 f_ui32InitialValue, UINT32 f_ui32Polynomial)
//	{	
//		const UINT8* pui8Data = f_pui8Data;
//		const UINT8* pui8End = f_pui8Data + f_ui32NumBytes;
//		while(pui8Data < pui8End)
//		{
//			f_ui32InitialValue = CalcCrc32(*pui8Data++, f_ui32InitialValue, f_ui32Polynomial);
//		}
//		return f_ui32InitialValue;
//	}
	/** Calculate the CRC32 checksum for the given array.<br>
	 * No reflection, inversion, reversion or final XOR used. Just plain CRC32.
	 * 
	 * @param data          Byte array to calculate the CRC for.
	 * @param initialValue  The initial value for the CRC (e.g. previous calculated value).
	 * @param polynomial    The polynomial to use for the CRC.
	 * 
	 */
	public static int calcCrc32(byte[] data, int initialValue, int polynomial)
	{	
		for (byte b : data)
		{	
			initialValue = calcCrc32(b, initialValue, polynomial);
		}
		return initialValue;
	}
}
