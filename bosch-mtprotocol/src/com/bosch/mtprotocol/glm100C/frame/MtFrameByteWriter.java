/**
 * 
 */
package com.bosch.mtprotocol.glm100C.frame;

import java.nio.ByteBuffer;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.type.UnionUint16;
import com.bosch.mtprotocol.util.Crc;




/**
 * @author AndrejsC
 *
 */
public class MtFrameByteWriter implements MtFrameConstants{

	private MtFrame frame;

	/**
	 * @param frame bufferSize
	 */
	public MtFrameByteWriter(MtFrame frame) {
		super();
		this.frame = frame;
	}

	public int write(byte[] out){
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(out);
		
		if(frame instanceof MtRequestFrame){
			writeRequest(byteBuffer);
		}else if(frame instanceof MtResponseFrame){
			writeResponse(byteBuffer);
		}
		
		return byteBuffer.position();
	}

	private void writeRequest(ByteBuffer byteBuffer){

		MtRequestFrame mtRequestFrame = (MtRequestFrame) frame;
		
		byteBuffer.put(mtRequestFrame.getFrameMode().getByte());
		byteBuffer.put(mtRequestFrame.getCommand());

		int frameFormat = mtRequestFrame.getFrameMode().ui2RequestFrameFormat.getValue();

		switch (frameFormat) {
		case EN_FRAME_FORMAT_SHORT:
			// Push Crc
			byteBuffer.put(Crc.calcCrc8(getBytes(byteBuffer)));
			break;

		case EN_FRAME_FORMAT_LONG:
			// Push Size
			byteBuffer.put((byte)mtRequestFrame.getPayloadData().length);
			// Push data
			byteBuffer.put(mtRequestFrame.getPayloadData());
			// Push CRC
			byteBuffer.put(Crc.calcCrc8(getBytes(byteBuffer)));
			break;

		case EN_FRAME_FORMAT_EXT:
			// Push Size
			UnionUint16 unPayloadSize = new UnionUint16();
			unPayloadSize.setValue(mtRequestFrame.getPayloadData().length);
			byteBuffer.put(unPayloadSize.ui8LSB.getByte());
			byteBuffer.put(unPayloadSize.ui8MSB.getByte());
			// Push data
			byteBuffer.put(mtRequestFrame.getPayloadData());
			// Push Crc
			byteBuffer.putShort(Crc.calcCrc16(getBytes(byteBuffer)));   
			break;

		default:
			break;
		}
	}

	private void writeResponse(ByteBuffer byteBuffer){
		MtResponseFrame mtResponseFrame = (MtResponseFrame) frame;
		
		byteBuffer.put(mtResponseFrame.getStatusByte().getByte());

		//Add the mode dependent part of the frame
		switch(mtResponseFrame.getFrameFormat()) {    

		case EN_FRAME_FORMAT_SHORT:
			// Push Crc
			byteBuffer.put(Crc.calcCrc8(getBytes(byteBuffer)));
			break;

		case EN_FRAME_FORMAT_LONG:
			// Push Size
			byteBuffer.put((byte)mtResponseFrame.getPayloadData().length);
			// Push data
			byteBuffer.put(mtResponseFrame.getPayloadData());
			// Push CRC
			byteBuffer.put(Crc.calcCrc8(getBytes(byteBuffer)));
			break;

		case EN_FRAME_FORMAT_EXT:
			// Push Command
			byteBuffer.put(mtResponseFrame.getCommand());
			// Push Size
			UnionUint16 unPayloadSize = new UnionUint16();
			unPayloadSize.setValue(mtResponseFrame.getPayloadData().length);
			byteBuffer.put(unPayloadSize.ui8LSB.getByte());
			byteBuffer.put(unPayloadSize.ui8MSB.getByte());
			// Push data
			byteBuffer.put(mtResponseFrame.getPayloadData());
			// Push Crc
			byteBuffer.putShort(Crc.calcCrc16(getBytes(byteBuffer)));  
			break;

		default:
			break;
		}; //end of switch
	}

	public byte[] getBytes(ByteBuffer byteBuffer){

		byte[] b = new byte[byteBuffer.position()];
		byteBuffer.position(0);
		byteBuffer.get(b, 0, b.length);
		return b;
	}

}
