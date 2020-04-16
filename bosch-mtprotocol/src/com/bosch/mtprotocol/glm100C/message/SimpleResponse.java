package com.bosch.mtprotocol.glm100C.message;

import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.type.UnionStatusByte;

public class SimpleResponse implements MtMessage {
	
	private UnionStatusByte status;
	
	/**
	 * create default simple response with long frame type and status success
	 */
	public SimpleResponse() {
		status = new UnionStatusByte();
		status.ui3ComStatus.setValue(MtFrameConstants.EN_COMM_STATUS_SUCCESS);
		status.ui3DeviceStatus.setValue(0);
		status.ui2FrameType.setValue(MtFrameConstants.EN_FRAME_FORMAT_LONG);
	}
	
	/**
	 * create simple response with long frame type
	 * @param comStatus is communication status
	 */
	public SimpleResponse(int comStatus) {
		status = new UnionStatusByte();
		status.ui3ComStatus.setValue(comStatus);
		status.ui3DeviceStatus.setValue(0);
		status.ui2FrameType.setValue(MtFrameConstants.EN_FRAME_FORMAT_LONG);
	}
	
	public UnionStatusByte getStatusByte() {
		return status;
	}
}
