/**
 * 
 */
package com.bosch.mtprotocol.glm100C.message.sync.list;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.glm100C.message.sync.SyncInputMessage;
import com.bosch.mtprotocol.glm100C.message.sync.SyncMessageFactory;
import com.bosch.mtprotocol.util.CastUtil;

/**
 * @author AndrejsC
 *
 */
public class SyncListMessageFactory implements MtMessageFactory {

	/* (non-Javadoc)
	 com.bosch.mtprotocolprotocol.MtMessageFactory#createcom.bosch.mtprotocoltprotocol.MtFrame)
	 */
	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			f.reset();

			SyncListInputMessage message = new SyncListInputMessage();
			
			// byte 1
			message.setIndexFrom(CastUtil.uByteToInt(f.popUint8FromPayloadData()));
			// byte 2
			message.setIndexTo(CastUtil.uByteToInt(f.popUint8FromPayloadData()));

			// sync containers
			for (int i = message.getIndexFrom(); i <= message.getIndexTo(); i++){
				SyncMessageFactory factory = new SyncMessageFactory();
				SyncInputMessage syncContainer = factory.createSyncInputMessage(f);
				message.getSyncContainers().add(syncContainer);
			}
			
			return message;
		}
		
		
		throw new IllegalArgumentException("Can't create SyncListInputMessage from " + frame);
	}

}
