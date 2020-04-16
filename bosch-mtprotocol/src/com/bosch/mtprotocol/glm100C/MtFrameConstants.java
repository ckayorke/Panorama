package com.bosch.mtprotocol.glm100C;

/**
 * @author AndrejsC
 *
 */
public interface MtFrameConstants {

	enum DeviceRole{
		ROLE_MASTER,
		ROLE_SLAVE
	}
	
	enum RxState{
		STATE_INIT,
		STATE_MODE,
		STATE_STATUS,
		STATE_CMD,
		STATE_SIZE_LSB,
		STATE_SIZE_MSB,
		STATE_DATA,
		STATE_CRC_LSB,
		STATE_CRC_MSB,
		STATE_ERROR
	}

	// sizes of protocol datatypes in bytes
	public static final int SIZE_UINT8 = 1;
	public static final int SIZE_UINT16 = 2;
	public static final int SIZE_UINT32 = 4;
	public static final int SIZE_FLOAT = 4;	
	
	//Specifies if frame is a request or a response
	public static final int EN_FRAME_TYPE_REQUEST  = 0x03;
	public static final int EN_FRAME_TYPE_RESPONSE = 0x00;
	
	//Specifies the format of the frame (SHORT, LONG or EXTENDED)
	public static final int EN_FRAME_FORMAT_LONG     = 0; //0b00
	public static final int EN_FRAME_FORMAT_SHORT    = 1; //0b01      
	public static final int EN_FRAME_FORMAT_EXT      = 2; //0b10
	public static final int EN_FRAME_FORMAT_RESERVED = 3; //0b11 //Also implemented to avoid invalid array access when 2Bit-Field has wrong value
	
	// Enumeration for ui3ComStatus field of status word
	public static final int EN_COMM_STATUS_SUCCESS = 0;
	public static final int EN_COMM_STATUS_TIMEOUT = 1;
	public static final int EN_COMM_STATUS_MODE_NOT_SUPPORTED_OR_INVALID = 2;
	public static final int EN_COMM_STATUS_CHECKSUM_ERROR = 3;
	public static final int EN_COMM_STATUS_CMD_UNKONWN = 4;
	public static final int EN_COMM_STATUS_ACCESS_DENIED = 5;
	public static final int EN_COMM_STATUS_PARAM_OR_DATA_ERROR = 6;
	
	//Specifies the format of the request and response frame for one message exchange.
	//Used for assignments to unionFrameMode; which abstracts the Mode-Byte of the frame.
	//Bit 1..0	Response frame format
	//Bit 3..2	Request frame format
	//Bit 5..4	reserved
	//Bit 7..6	Frame type: 00=response; 11=request; others=invalid. For master always TYPE_REQUEST
	//Field partitions:                                               REQUEST FORMAT          |    RESPONSE FORMAT    |     FRAME_TYPE_REQUEST
	public static final int EN_FRAME_MODE_SHORT_REQ_SHORT_RESP = (EN_FRAME_FORMAT_SHORT << 2) | EN_FRAME_FORMAT_SHORT | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_SHORT_REQ_LONG_RESP  = (EN_FRAME_FORMAT_SHORT << 2) | EN_FRAME_FORMAT_LONG  | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_SHORT_REQ_EXT_RESP   = (EN_FRAME_FORMAT_SHORT << 2) | EN_FRAME_FORMAT_EXT   | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_LONG_REQ_SHORT_RESP  = (EN_FRAME_FORMAT_LONG  << 2) | EN_FRAME_FORMAT_SHORT | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_LONG_REQ_LONG_RESP   = (EN_FRAME_FORMAT_LONG  << 2) | EN_FRAME_FORMAT_LONG  | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_LONG_REQ_EXT_RESP    = (EN_FRAME_FORMAT_LONG  << 2) | EN_FRAME_FORMAT_EXT   | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_EXT_REQ_SHORT_RESP   = (EN_FRAME_FORMAT_EXT   << 2) | EN_FRAME_FORMAT_SHORT | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_EXT_REQ_LONG_RESP    = (EN_FRAME_FORMAT_EXT   << 2) | EN_FRAME_FORMAT_LONG  | (EN_FRAME_TYPE_REQUEST << 6);
	public static final int EN_FRAME_MODE_EXT_REQ_EXT_RESP     = (EN_FRAME_FORMAT_EXT   << 2) | EN_FRAME_FORMAT_EXT   | (EN_FRAME_TYPE_REQUEST << 6);
	
	//=== Request  ===
	public static final int EN_TX_FIFO_OFFSET_REQ_MODE     = 0;
	public static final int EN_TX_FIFO_OFFSET_REQ_CMD      = 1;
	public static final int EN_TX_FIFO_OFFSET_REQ_SIZE_LSB = 2;
	public static final int EN_TX_FIFO_OFFSET_REQ_SIZE_MSB = 3;
	
	//=== Response ===
	public static final int EN_TX_FIFO_OFFSET_RESP_STATUS       = 0;
	public static final int EN_TX_FIFO_OFFSET_RESP_SIZE_LONG    = 1;
	public static final int EN_TX_FIFO_OFFSET_RESP_CMD_EXT      = 1;
	public static final int EN_TX_FIFO_OFFSET_RESP_SIZE_LSB_EXT = 2;
	public static final int EN_TX_FIFO_OFFSET_RESP_SIZE_MSB_EXT = 3;
}
