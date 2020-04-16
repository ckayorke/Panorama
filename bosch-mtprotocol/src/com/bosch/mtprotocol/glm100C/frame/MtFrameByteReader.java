/**
 * 
 */
package com.bosch.mtprotocol.glm100C.frame;

//import java.util.logging.Logger;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.glm100C.MtFrameConstants;
import com.bosch.mtprotocol.type.UnionFrameMode;
import com.bosch.mtprotocol.type.UnionStatusByte;
import com.bosch.mtprotocol.type.UnionUint16;
import com.bosch.mtprotocol.util.CastUtil;
import com.bosch.mtprotocol.util.Crc;




/**
 * @author AndrejsC
 *
 */
public class MtFrameByteReader implements MtFrameConstants{

	//private final static Logger LOG = Logger.getLogger(MtFrameByteReader.class.getName());
	
	enum RxState{
		STATE_INIT,
		STATE_MODE,
		STATE_STATUS,
		STATE_CMD,
		STATE_SIZE_LSB,
		STATE_SIZE_MSB,
		STATE_DATA,
		STATE_CRC_LSB,
		STATE_CRC_BYTE2,
		STATE_CRC_BYTE3,
		STATE_CRC_MSB,
		STATE_ERROR
	}
	
	private RxState rxState;
	private MtFrame frame;
	
	private UnionStatusByte unStatusByte = new UnionStatusByte();
	private UnionFrameMode unFrameMode = new UnionFrameMode();
	private byte ui8Command;
	
	// for extended packages with 32 bit CRC
	private byte ui8CrcLsb; // holds the first CRC byte incoming (LSB)
	private byte ui8CrcB2; // holds the second CRC byte incoming
	private byte ui8CrcB3; // holds the third CRC byte incoming

	private int rxFrameFormat;

	private boolean boMsgRcvComplete;

	// Checksum														
	//private int byteCnt;
	private int ui32RxCrc;

	// Size																
	private UnionUint16 unNumRxPayloadBytes = new UnionUint16();
	private final int ui16RxFifoSize;
	private final byte[] arrui8RxFifo;
	// Positions													 
	private int ui16RxFifoWritePos;

	private int frameType;
	
	public MtFrameByteReader(byte[] bbRxFifo, int ui16RxFifoSize) {
		this.ui16RxFifoSize = ui16RxFifoSize;
		this.arrui8RxFifo = bbRxFifo;
		
		ui16RxFifoWritePos = 0;
		ui8Command = 0;
		rxState = RxState.STATE_INIT;
		//byteCnt = 0;
		ui32RxCrc = Crc.EN_CRC32_INITIAL_VALUE;
	}
	
	public void append(byte ui8ReceivedByte){
		
		boolean boRcvMsgComplete = false;

		//LOG.finest(rxState + " byte:" + Byte.toString(ui8ReceivedByte));

		if(rxState == RxState.STATE_INIT){
			frameType = (ui8ReceivedByte & 0xFF) >>> 6;
			//LOG.warning("State Init, byte 1 = " + Byte.toString(ui8ReceivedByte) + " frame type = " + frameType); 
			if(frameType == EN_FRAME_TYPE_REQUEST){
				rxState = RxState.STATE_MODE;
			}else if(frameType == EN_FRAME_TYPE_RESPONSE){
				rxState = RxState.STATE_STATUS;
			}else{
				rxState = RxState.STATE_ERROR;
			}
		}

		switch(rxState){

		// MODE (from Requester)
		case STATE_MODE:
			// set frame mode
			setFrameMode(ui8ReceivedByte);

			// select next state
			if(unFrameMode.ui2FrameType.getValue() == EN_FRAME_TYPE_REQUEST){
				//LOG.warning("State MODE: " + byteCnt);
				rxState = RxState.STATE_CMD;
			}else{ //Unexpected response received -> wait for last byte and provoke timeout        
				rxState = RxState.STATE_ERROR; 
			}
			break;

			// STATUS (from Responder)
		case STATE_STATUS:

			unStatusByte.setValue(ui8ReceivedByte);

			//For some comm errors, slave will return a long frame instead of the requested one.
			if((unStatusByte.ui3ComStatus.getValue() & 0x03) > EN_COMM_STATUS_SUCCESS){ //SUCCESS < status <= Checksum error
				//Save frame mode for repetition of request
				rxFrameFormat = EN_FRAME_FORMAT_LONG;
			}

			//select next state
			if(unStatusByte.ui2FrameType.getValue() == EN_FRAME_TYPE_RESPONSE)
			{
				switch(rxFrameFormat){
				default:
				case EN_FRAME_FORMAT_SHORT:
					//LOG.warning("State STATUS (SHORT): " + byteCnt);
					rxState = RxState.STATE_CRC_LSB;
					break;
				case EN_FRAME_FORMAT_LONG:
					//LOG.warning("State STATUS (LONG): " + byteCnt);
					rxState = RxState.STATE_SIZE_LSB;
					break;
				case EN_FRAME_FORMAT_EXT:
					//LOG.warning("State STATUS (EXTENDED): " + byteCnt);
					rxState = RxState.STATE_CMD;
					break;
				};
			}
			else{
				//Unexpected request received -> wait for last byte and provoke timeout
				rxState = RxState.STATE_ERROR;
			}
			break;

			// COMMAND (Request & EXTENDED Response)
		case STATE_CMD:
			ui8Command = ui8ReceivedByte;

			//select next state
			//LOG.warning("State CMD: " + byteCnt);
			if(rxFrameFormat == EN_FRAME_FORMAT_SHORT){
				rxState = RxState.STATE_CRC_LSB;
			}else{
				// Also used for Response frame EXTENDED
				rxState = RxState.STATE_SIZE_LSB;  
			}
			break;

			// SIZE LSB
		case STATE_SIZE_LSB:
			unNumRxPayloadBytes.ui8LSB.setValue(ui8ReceivedByte);

			//LOG.warning("Payload should be: " + unNumRxPayloadBytes.getValue() + " bytes");
			//select next state
			//LOG.warning("State SIZE_LSB: " + byteCnt);
			if(rxFrameFormat == EN_FRAME_FORMAT_EXT){
				rxState = RxState.STATE_SIZE_MSB;
			}
			else{ 
				//Short/Long frame
				if(unNumRxPayloadBytes.getValue() == 0)
					rxState = RxState.STATE_CRC_LSB;
				else 
					rxState = RxState.STATE_DATA;
			}
			break;

			// SIZE MSB (EXTENDED only)
		case STATE_SIZE_MSB:
			unNumRxPayloadBytes.ui8MSB.setValue(ui8ReceivedByte);

			//select next state
			//LOG.warning("State SIZE_MSB: " + byteCnt);
			//LOG.warning("Payload should be: " + unNumRxPayloadBytes.getValue() + " bytes");
			if(unNumRxPayloadBytes.getValue() == 0){
				rxState = RxState.STATE_CRC_LSB;
			}else{
				rxState = RxState.STATE_DATA;
			}
			break;

			// DATA
		case STATE_DATA:     
			//collect all announced data. If not enough data bytes: no request interpretation -> timeout
			if(ui16RxFifoWritePos < unNumRxPayloadBytes.getValue()) {  
				//protect against buffer overrun
				if(ui16RxFifoWritePos < ui16RxFifoSize) {
					arrui8RxFifo[ui16RxFifoWritePos++] = ui8ReceivedByte;
					//select next state
					if(ui16RxFifoWritePos == unNumRxPayloadBytes.getValue()) {
						//LOG.warning("State DATA: " + byteCnt);
						rxState = RxState.STATE_CRC_LSB;
					}
				}else {
					// Too much data for the RX buffer. MtInputMessage damaged or Rx buffer too small.
					// Switch to error state and wait for last byte.
					unStatusByte.ui3ComStatus.setValue(EN_COMM_STATUS_PARAM_OR_DATA_ERROR);
					rxState = RxState.STATE_ERROR;
				}
			}
			break;

			// CRC LSB
		case STATE_CRC_LSB:

			//select next state
			if(rxFrameFormat == EN_FRAME_FORMAT_EXT){
				ui8CrcLsb = ui8ReceivedByte;
				rxState = RxState.STATE_CRC_BYTE2;
			}else{
				ui32RxCrc = CastUtil.uByteToInt(Crc.calcCrc8(ui8ReceivedByte, (byte)ui32RxCrc));
				//byteCnt++;
				boRcvMsgComplete = true;
			}
			break;

			// CRC Byte 2 (EXTENDED only)
		case STATE_CRC_BYTE2:
			// select next state
			ui8CrcB2 = ui8ReceivedByte;
			rxState = RxState.STATE_CRC_BYTE3;
			break;
			
			// CRC Byte 3 (EXTENDED only)
		case STATE_CRC_BYTE3:
			// select next state
			ui8CrcB3 = ui8ReceivedByte;
			rxState = RxState.STATE_CRC_MSB;
			break;

			// CRC MSB (EXTENDED only)
		case STATE_CRC_MSB:
			//For CRC32 calculation, higher bytes has to be stuffed into the function before
			//LSB to get CRC32 == 0 for valid CRC result (endianess issue)! 
			ui32RxCrc = Crc.calcCrc32(ui8ReceivedByte, ui32RxCrc);
			ui32RxCrc = Crc.calcCrc32(ui8CrcB3, ui32RxCrc);
			ui32RxCrc = Crc.calcCrc32(ui8CrcB2, ui32RxCrc);
			ui32RxCrc = Crc.calcCrc32(ui8CrcLsb, ui32RxCrc); 
			
			//byteCnt += 4;
			//LOG.warning("State CRC_MSB: " + byteCnt);
			boRcvMsgComplete = true;
			break;

			// DEFAULT/ERROR
		default:
		case STATE_ERROR:
			//Something went wrong with the frame.
			//Wait until last byte of message is received. 
			//Then a Byte-Timeout will occur and the status byte can be analyzed for error handling.
			break;
		}


		// ==============================
		// Validate message if complete
		// ==============================

		if(boRcvMsgComplete){
			// Calculated CRC must be 0 after received CRC was added to the CRC calculation
			//LOG.warning("CRC value after all: " + ui32RxCrc);
			//LOG.warning("Bytes received: " + byteCnt);
			//byteCnt = 0;
			if(ui32RxCrc == 0){
				boMsgRcvComplete = true;
				rxState = RxState.STATE_INIT; // if more than one packages in buffer
			}else{
				//CRC error occurred.
				unStatusByte.ui3ComStatus.setValue(EN_COMM_STATUS_CHECKSUM_ERROR); 
				// Wait for timeout to receive the rest of the message (e.g. if mode 
				// byte was corrupted and SHORT frame was expected instead of LONG frame).
				// Otherwise the remaining bytes may be interpreted as part of a new message.
				rxState = RxState.STATE_ERROR;
			}
		} else{
			//Calculate checksum (calculated after switch, because Frame Mode has to be set first)
			if(rxFrameFormat == EN_FRAME_FORMAT_EXT){
				if(rxState != RxState.STATE_CRC_BYTE2 && rxState != RxState.STATE_CRC_BYTE3 
						&& rxState != RxState.STATE_CRC_MSB) {
					ui32RxCrc = Crc.calcCrc32(ui8ReceivedByte, ui32RxCrc);
					//byteCnt++;
				}
			}
			else {
				// Frame format is not EXTENDED
				ui32RxCrc = CastUtil.uByteToInt(Crc.calcCrc8(ui8ReceivedByte, (byte)ui32RxCrc));
				//byteCnt++;
			}
		}
		
	}
	
	/** 
	 * Sets the frame mode and adjusts the TX buffer offsets. 
	 * Must be called before pushing the first data into TX buffer!<br>
	 * Master only!
	 */
	private void setFrameMode(int mode) {
		unFrameMode.setValue(mode);        
		if(frameType == EN_FRAME_TYPE_RESPONSE) { //Response
			rxFrameFormat = unFrameMode.ui2ResponseFrameFormat.getValue();
		} else { //Request
			rxFrameFormat = unFrameMode.ui2RequestFrameFormat.getValue();
		} 
	}
	
	public synchronized boolean isFrameRcvComplete() {
		return boMsgRcvComplete;
	}
	
	public synchronized void clearFrameRcvComplete() {
		boMsgRcvComplete = false;
	}

	/** Getter for com status part of status byte */
	public int getComStatus() {
		return unStatusByte.ui3ComStatus.getValue();
	}

	public MtFrame getFrame(){
		if(frame != null){
			return frame;
		}
		if(isFrameRcvComplete()){
			if(frameType == EN_FRAME_TYPE_REQUEST){
				MtRequestFrame mtRequestFrame = new MtRequestFrame(ui16RxFifoSize);
				mtRequestFrame.setCommand(ui8Command);
				mtRequestFrame.setFrameMode(unFrameMode.getValue());
				for(int i = 0; i < unNumRxPayloadBytes.getValue(); i++){
					mtRequestFrame.pushUint8ToData(arrui8RxFifo[i]);
				}
				frame = mtRequestFrame;
			}else if(frameType == EN_FRAME_TYPE_RESPONSE){
				MtResponseFrame mtResponseFrame = new MtResponseFrame(ui16RxFifoSize);
				mtResponseFrame.setCommand(ui8Command);
				mtResponseFrame.setStatusByte(unStatusByte);
				mtResponseFrame.setFrameFormat(rxFrameFormat);
				for(int i = 0; i < unNumRxPayloadBytes.getValue(); i++){
					mtResponseFrame.pushUint8ToData(arrui8RxFifo[i]);
				}
				frame = mtResponseFrame;
			}
		}
		return frame;
	}
}
