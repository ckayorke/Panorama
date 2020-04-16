/**
 * 
 */
package com.bosch.mtprotocol.thermo.message.imgdata;

import java.util.Arrays;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;

/**
 * @author tos2si1
 *
 */
public class ImgDataMessageFactory implements MtMessageFactory {
	
	public final static int BASIC_PAYLOAD_SIZE = 4;
	private byte [] payloadFull;

	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			
			//payload with correct position (set to last pushed byte)
			payloadFull = f.getPayloadData();
			f.reset();
			
			return createImgDataInputMessage(f);
		}

		throw new IllegalArgumentException("Can't create SyncInputMessage from " + frame);
	}

	
	public ImgDataInputMessage createImgDataInputMessage(MtBaseFrame f) {

		ImgDataInputMessage message = new ImgDataInputMessage();
		
		// byte 1 + 2  packageIndex
		message.setPackageIndex(f.popUint16FromPayloadData());
		
		// byte 3 + 4 measID
		message.setMeasID(f.popUint16FromPayloadData());	
		
		//remaining bytes
		if (payloadFull.length > BASIC_PAYLOAD_SIZE) {
			byte[] payloadCut = Arrays.copyOfRange(payloadFull, BASIC_PAYLOAD_SIZE, payloadFull.length);
			message.setImgData(payloadCut);
		}else if(payloadFull.length == BASIC_PAYLOAD_SIZE){
			message.setLastPackage(true);
		}
		
		return message;
	}

}
