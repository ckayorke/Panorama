package com.bosch.mtprotocol.thermo.message.imginfo;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.util.CastUtil;

public class ImgInfoMessageFactory implements MtMessageFactory {
	
	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			return createImgInfoInputMessage(f);
		}

		throw new IllegalArgumentException("Can't create SyncInputMessage from " + frame);
	}

	public ImgInfoInputMessage createImgInfoInputMessage(MtBaseFrame f){

		ImgInfoInputMessage message = new ImgInfoInputMessage();

		// byte 1 + 2
		byte pixelXByteLS = f.popUint8FromPayloadData(); // necessary to put incoming bytes in right order
		byte pixelXByteMS = f.popUint8FromPayloadData();
		int pixX = 0;
		pixX = CastUtil.uByteToInt(pixelXByteMS);
		pixX <<= 8;
		pixX |= CastUtil.uByteToInt(pixelXByteLS);
		message.setPixelsX(pixX);
		
		// byte 3 + 4
		byte pixelYByteLS = f.popUint8FromPayloadData(); // necessary to put incoming bytes in right order
		byte pixelYByteMS = f.popUint8FromPayloadData();
		int pixY = 0;
		pixY = CastUtil.uByteToInt(pixelYByteMS);
		pixY <<= 8;
		pixY |= CastUtil.uByteToInt(pixelYByteLS);
		message.setPixelsY(pixY);
		
		// byte 5, 6, 7, 8
		message.setImgSize(f.popUint32FromPayloadData());
		
		// byte 9 + 10
		byte maxSizeByteLS = f.popUint8FromPayloadData(); // necessary to put incoming bytes in right order
		byte maxSizeByteMS = f.popUint8FromPayloadData();
		int maxSize = 0;
		maxSize = CastUtil.uByteToInt(maxSizeByteMS);
		maxSize <<= 8;
		maxSize |= CastUtil.uByteToInt(maxSizeByteLS);
		message.setMaxSizeDataBlock(maxSize);
		
		// byte 11 + 12 
		byte byteLS = f.popUint8FromPayloadData(); // necessary to put incoming bytes in right order
		byte byteMS = f.popUint8FromPayloadData();
		int mID = 0;
		mID = CastUtil.uByteToInt(byteMS);
		mID <<= 8;
		mID |= CastUtil.uByteToInt(byteLS);
		message.setMeasID(mID);

		return message;
	}
}
