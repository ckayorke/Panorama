package com.bosch.mtprotocol.general.message.dev_info;

import java.io.UnsupportedEncodingException;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.util.CastUtil;

/**
 * Factory for parsing response message of general command 6 (Get Device Info)
 * Refer also to MT Protocol documentation
 * 
 * @author tos2si1
 */
public class DevInfoMessageFactory implements MtMessageFactory {
	
	/* (non-Javadoc)
	 * com.bosch.mtprotocolotocol.MtMessageFactory#createMecom.bosch.mtprotocolrotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();
			
			DevInfoMessage message = new DevInfoMessage();

			// byte 0, 1, 2, 3
			String temp = "";
			byte [] dateCodeByte = new byte[4];
			for (int i = 0; i < dateCodeByte.length; i++) {
				dateCodeByte[i] = f.popUint8FromPayloadData();
				temp = temp + dateCodeByte[i];
			}
			try {
				String dateCodeString = new String(dateCodeByte, "ISO-8859-1");
				message.setDateCode(dateCodeString);
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// byte 4, 5, 6, 7
			message.setSerialNumber(f.popUint32FromPayloadData());
			// byte 8, 9
			message.setSwRevision(f.popUint16FromPayloadData());
			// byte 10, 11, 12
			message.setSwVersionMain(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			message.setSwVersionSub(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			message.setSwVersionBug(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			// byte 13, 14, 15
			message.setHwPCBVersion(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			message.setHwPCBVariant(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			message.setHwPCBBug(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			// byte 16 to 28
			byte [] ttnrByte = new byte[13];
			for (int i = 0; i < dateCodeByte.length; i++) {
				ttnrByte[i] = f.popUint8FromPayloadData();
			}
			try {
				String ttnrString = new String(ttnrByte, "ISO-8859-1");
				message.setDateCode(ttnrString);
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return message;
		}

		throw new IllegalArgumentException("Can't create DevInfoMessage from " + frame);
	}

}
