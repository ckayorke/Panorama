package com.bosch.mtprotocol.glm100C.frame;

import java.nio.ByteBuffer;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.type.UnionFloat;
import com.bosch.mtprotocol.type.UnionUint16;
import com.bosch.mtprotocol.type.UnionUint32;
import com.bosch.mtprotocol.type.field.Field;
import com.bosch.mtprotocol.util.CastUtil;




public abstract class MtBaseFrame implements MtFrame, MtFrameConstants {

	private byte ui8Command;
	private ByteBuffer payloadData;
	
	private byte[] rawData;
	
	/**
	 * 
	 */
	public MtBaseFrame(int bufferSize) {
		payloadData = ByteBuffer.allocate(bufferSize);
	}

	/**
	 * @return the ui8Command
	 */
	public byte getCommand() {
		return ui8Command;
	}

	/**
	 * @param ui8Command the ui8Command to set
	 */
	public void setCommand(byte ui8Command) {
		this.ui8Command = ui8Command;
	}
	
	/**
	 * @return the payloadData
	 */
	public byte[] getPayloadData() {
		byte[] b = new byte[payloadData.position()];
		payloadData.position(0);
		payloadData.get(b, 0, b.length);
		return b;
	}
	
	public void pushUint8ToData(byte ui8Value){
		payloadData.put(ui8Value);
	}
	
	public void pushUint16ToData(short ui16Value){
		UnionUint16 unData = new UnionUint16();
		unData.setShort(ui16Value);
		for (Field f : unData) {
			payloadData.put(f.getByte());
		}
	}
	
	public void pushUint32ToData(int ui32Value) {
		UnionUint32 unData = new UnionUint32();
		unData.setValue(ui32Value);
		for (Field f : unData) {
			payloadData.put(f.getByte());
		}
	}
	
	public void reset(){
		payloadData.position(0);
	}
	
	public byte popUint8FromPayloadData() {
		return payloadData.get();
	}
	
	public float popFloatFromPayloadData() {
		UnionFloat unData = new UnionFloat();
		for (Field f : unData) {
			f.setValue(payloadData.get());
		}
		return(unData.getFloat());
	}
	
	public int popUint32FromPayloadData(){
		UnionUint32 unData = new UnionUint32();
		for (Field f : unData) {
			f.setValue(payloadData.get());
		}
		return(unData.getValue());
	}
	
	public int popUint16FromPayloadData() {
		/* Old implementation with manual reverse of byte order */
		/*byte byteLS = payloadData.get(); // necessary to put incoming bytes in right order
		byte byteMS = payloadData.get();
		int u16Int = 0;
		u16Int = CastUtil.uByteToInt(byteMS);
		u16Int <<= 8;
		u16Int |= CastUtil.uByteToInt(byteLS);
		return u16Int;*/

		/* New implementation with union use */
		UnionUint16 unData = new UnionUint16();
		for (Field f : unData) {
			f.setValue(payloadData.get());
		}
		return (unData.getShort());
	}

	/**
	 * @return the rawData
	 */
	public byte[] getRawData() {
		if(rawData == null){
			return new byte[0];
		}
		return rawData;
	}

	/**
	 * @param rawData the rawData to set
	 */
	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}
	
}
