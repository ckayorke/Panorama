package com.bosch.mtprotocol.glm100C;

import com.bosch.mtprotocol.MtConnection;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.MtProtocol;
import com.bosch.mtprotocol.glm100C.MtTimer.MtTimerListener;
import com.bosch.mtprotocol.glm100C.connection.MtAsyncBLEConnection;
import com.bosch.mtprotocol.glm100C.connection.MtAsyncBLEConnection.MTAsyncBLEConnectionCallbackRecipient;
import com.bosch.mtprotocol.glm100C.event.MtProtocolFatalErrorEvent;
import com.bosch.mtprotocol.glm100C.event.MtProtocolReceiveMessageEvent;
import com.bosch.mtprotocol.glm100C.event.MtProtocolRequestTimeoutEvent;
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.glm100C.frame.MtFrameByteReader;
import com.bosch.mtprotocol.glm100C.frame.MtFrameByteWriter;
import com.bosch.mtprotocol.glm100C.frame.MtRequestFrame;
import com.bosch.mtprotocol.glm100C.message.FrameFactoryImpl;
import com.bosch.mtprotocol.glm100C.message.MessageFactoryImpl;
import com.bosch.mtprotocol.glm100C.state.MtProtocolStateMachine;
import com.bosch.mtprotocol.glm100C.state.MtProtocolStates;
import com.bosch.mtprotocol.util.ArrayUtils;
import com.bosch.mtprotocol.util.statemachine.StateMachine;
import com.bosch.mtprotocol.util.statemachine.exc.StateMachineDescriptorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

public class MtProtocolBLEImpl implements MtProtocol, MtFrameConstants, MtTimerListener, MtProtocolStates, MTAsyncBLEConnectionCallbackRecipient{

	private final static Logger LOG = Logger.getLogger(MtProtocol.class.getName());

	private static final int RX_FIFO_SIZE = 25; // BLE message can contain up to 20 bytes
	private static final int TX_FIFO_SIZE = 25;

	private StateMachine stateMachine;
	private MtAsyncBLEConnection connection;
	private MtFrameByteReader frameReader;
	private MtRequestFrame currentRequest;
	private ArrayList<MtMessage> messages = new ArrayList<>();
	private LinkedBlockingDeque<MtMessage> outgoingDeque = new LinkedBlockingDeque<>();
	private Set<MTProtocolEventObserver> observers = new HashSet<>();
	private int timeout;
	private MtTimer timeoutTimer;

	private MtFrameFactory frameFactory = new FrameFactoryImpl();
	private MtMessageFactory messageFactory = new MessageFactoryImpl();

	private boolean flagIsBLEWriteFinished = true;
	private SendThread sendThread;
	
	@Override
	public void onTimerTick() {
		// handle timeout
		try {
			stateMachine.processEvent(EVENT_SET_TIMEOUT);
			if(frameReader != null){
				synchronized (frameReader) {
					frameReader = null;
				}
			}

			if(stateMachine.getCurrentState() == STATE_MASTER_READY){
				stateMachine.processEvent(EVENT_SWITCH_TO_SLAVE);
			}

			notifyObservers(new MtProtocolRequestTimeoutEvent());
		} catch (StateMachineDescriptorException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(MtConnection connection) {

		LOG.warning("Initializing MT Protocol...");
		stateMachine = new MtProtocolStateMachine();

		if (connection instanceof MtAsyncBLEConnection) {
			this.connection = (MtAsyncBLEConnection) connection;
			LOG.warning("Adding as observer...");
			this.connection.addCallbackRecipient(this);
		} else {
			throw new RuntimeException("Can't initialize MTProtocol", new Throwable());
		}

		frameReader = null;
		currentRequest = null;
		messages.clear();
		outgoingDeque.clear();

		if (sendThread != null) sendThread.interrupt();
		sendThread = new SendThread();
		sendThread.start();

		try {
			stateMachine.processEvent(EVENT_INITIALIZE_SLAVE);
		} catch (StateMachineDescriptorException e) {
			throw new RuntimeException("Can't initialize MTProtocol", e);
		}
	}

	@Override
	public void destroy() {
		this.connection.removeCallbackRecipient(this);
		observers.clear();

		if(sendThread != null){
			sendThread.stopTimeoutTimer();
			sendThread.interrupt();
		}
	}

	@Override
	public void sendMessage(MtMessage message) {
		//LOG.warning("Protocol: sending message " + message.toString() + "; current state is " + stateMachine.getCurrentState());
		if(stateMachine.getCurrentState() == STATE_SLAVE_LISTENING){
			try {
				stateMachine.processEvent(EVENT_SWITCH_TO_MASTER);
			} catch (StateMachineDescriptorException e) {
				e.printStackTrace();
				return;
			}
		}
		LOG.finest("MtProtocol: Adding outgoing message to queue.");
		if (!outgoingDeque.offer(message)) {
			throw new RuntimeException("MtProtocol: Error: outgoing queue is full, message dropped.");
		}
	}

	@Override
	public void addObserver(MTProtocolEventObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(MTProtocolEventObserver observer) {
		observers.remove(observer);
	}
	
	@Override
	public void onBLECharacteristicChanged(byte[] data) {
		int length = data.length;

		startTimeoutTimer();

		LOG.warning("BLE callback in MTProtocol; Bytes in buffer: " + length);
		if(stateMachine.getCurrentState() == STATE_SLAVE_LISTENING){
			// Update state
			try {
				stateMachine.processEvent(EVENT_RECEIVE_START);
			} catch (StateMachineDescriptorException e) {
				e.printStackTrace();
			}
		}

		if(stateMachine.getCurrentState() == STATE_SLAVE_RECEIVING || stateMachine.getCurrentState() == STATE_MASTER_RECEIVING){

			if(frameReader == null){
				frameReader = new MtFrameByteReader(new byte[RX_FIFO_SIZE], RX_FIFO_SIZE);
			}

			synchronized (frameReader) {
				for(int  i = 0; i < length; i++){
					byte b = data[i];
					//LOG.warning("Byte " + i + " = " + String.format("%02X ", b));
					frameReader.append(b);
					if (frameReader.isFrameRcvComplete()) {
						//LOG.warning("Frame with " + (i + 1) + " bytes received; Bytes in buffer: " + length);
						checkMessageComplete();
						if (i < (length - 1)) { // it is not the last byte => another message is following
							LOG.warning("Frame was not last frame in buffer; expect another frame");
							frameReader = null;
							frameReader = new MtFrameByteReader(new byte[RX_FIFO_SIZE], RX_FIFO_SIZE);
						} else {
							LOG.warning("Frame was last frame in buffer; proceed to transaction completion");
						}
						//} else if (i == length - 1) {
						//LOG.warning("Frame receiving done, but frame incomplete");
					}
				}
			}


		}else{
			LOG.warning("MtProtocolImpl: Ignoring received data; Current state: " + stateMachine.getCurrentState());
		}

		checkTransactionCompleted();
	}

	@Override
	public void onBLEWriteFinished(int status) {
		if (status == 0) {
			//if (stateMachine.getCurrentState() != STATE_MASTER_READY)
			sendingCompleted();
		} else {
			// TODO: implement exception case
		}
	}

	@Override
	public void reset(){
		initialize(connection);
	}

	@Override
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}

	/*#####################################*/
	/*########## Private Methods ##########*/
	/*#####################################*/

	private void notifyObservers(MTProtocolEvent event){
		for(MTProtocolEventObserver observer: observers){
			observer.onEvent(event);
		}
	}

	private void checkMessageComplete() {

		LOG.finest("MtProtocol: Frame receive complete");

		int comStatus = frameReader.getComStatus();

		// successful frame receive
		if(comStatus == EN_COMM_STATUS_SUCCESS) {

			// get frame
			MtBaseFrame receivedFrame = (MtBaseFrame) frameReader.getFrame();

			if(stateMachine.getCurrentState() == STATE_MASTER_RECEIVING){
				if(receivedFrame.getCommand() == 0){
					// In case of SHORT response we don't have command in frame
					if(currentRequest != null){
						receivedFrame.setCommand(currentRequest.getCommand());
					}else{
						LOG.warning("Unknown response received! Current request is NULL");
					}

				}
			}

			MtMessage message = null;
			try{
				message = messageFactory.createMessage(receivedFrame);
			}catch(Exception e){
				// do nothing
			}

			messages.add(message);

		}else{
			LOG.warning("MtProtocol: Communication error");
			notifyObservers(new MtProtocolFatalErrorEvent());
			notify();
		}
	}

	private synchronized void checkTransactionCompleted() {
		if (frameReader != null && frameReader.isFrameRcvComplete()) {

			LOG.finest("MtProtocol: Buffer receive complete or timeout");

			stopTimeoutTimer();

			// end of buffer => set correct state machine state
			try {
				stateMachine.processEvent(EVENT_RECEIVE_FINISH);
				if (outgoingDeque.isEmpty() && stateMachine.getCurrentState() == STATE_MASTER_READY) {
					stateMachine.processEvent(EVENT_SWITCH_TO_SLAVE);
				}
			} catch (StateMachineDescriptorException e) {
				e.printStackTrace();
			}

			for (MtMessage message : messages) {
				if (message == null) {
					LOG.finest("Error occurred when turning received frame to message");
					notifyObservers(new MtProtocolFatalErrorEvent());
				} else {
					LOG.finest("Received message: " + message);
					notifyObservers(new MtProtocolReceiveMessageEvent(message));
					if (this.sendThread != null) {
						sendThread.stopTimeoutTimer();
					}
				}

				notify();
			}

			messages.clear();
			frameReader = null;
		} else {
			LOG.warning("MtProtocol: Frame receive not complete");
		}
	}

	private synchronized void sendingCompleted() {
		flagIsBLEWriteFinished = true;
		notify();
	}

	private void startTimeoutTimer(){
		stopTimeoutTimer();
		timeoutTimer = new MtTimer(timeout);
		timeoutTimer.setListener(MtProtocolBLEImpl.this);
		timeoutTimer.start();

	}

	private void stopTimeoutTimer(){
		if(timeoutTimer != null){
			timeoutTimer.setListener(null);
			timeoutTimer.stop();
			timeoutTimer = null;
		}
	}

	/*#####################################*/
	/*########## Private Classes ##########*/
	/*#####################################*/

	private class SendThread extends Thread{

		private MtTimer timeoutTimer;

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			LOG.finest("MtProtocol sendThread: Queue sending thread started.");
			while (!sendThread.isInterrupted()) {
				MtMessage outMsg;
				try {
					LOG.finest("MtProtocol sendThread: Getting message from queue.");
					//outMsg = outgoingQueue.take();
					outMsg = outgoingDeque.takeFirst();
					LOG.finest("MtProtocol sendThread: Got message from queue.");
				} catch (InterruptedException e) {
					return; //die if interrupted
				}

				synchronized (this) {
					while (stateMachine.getCurrentState() != STATE_MASTER_READY) {
						try {
							LOG.finest("MtProtocol sendThread: Waiting for state READY. Current state: " + stateMachine.getCurrentState());
							wait();
						} catch (InterruptedException e) {
							return; //die if interrupted
						}
					}
				}

				try {
					stateMachine.processEvent(EVENT_SEND_START);
				} catch (StateMachineDescriptorException e) {
					e.printStackTrace();
					return;
				}

				synchronized (this) {
					LOG.finest("MtProtocol sendThread: Starting message transmission.");

					startTimeoutTimer();

					MtBaseFrame frame = (MtBaseFrame) frameFactory.createFrame(outMsg);

					MtFrameByteWriter writer = new MtFrameByteWriter(frame);
					byte[] buffer = new byte[TX_FIFO_SIZE];
					int length = writer.write(buffer);

					byte[] frameData = ArrayUtils.copy(buffer, length);
					frame.setRawData(frameData);

					for(int i = 0; i < frameData.length; i++) {
						LOG.finest(i + ":  " + frameData[i]);
					}

					if (frame instanceof MtRequestFrame) { // save current request to compare with response
						currentRequest = (MtRequestFrame) frame;
					} else { // response frame - dismiss last saved request
						currentRequest = null;
					}

					try {
						connection.write(frameData);
						flagIsBLEWriteFinished = false;
					} catch (IOException e) {
						LOG.warning("Could not write data to characteristic...");
						e.printStackTrace();
						//	setSendError();
						outgoingDeque.offerFirst(outMsg);
						try {
							Thread.sleep(200);
						} catch (InterruptedException ex1) {
							ex1.printStackTrace();
							return;
						}
						try {
							stateMachine.processEvent(EVENT_SET_TIMEOUT);
						} catch (StateMachineDescriptorException ex2) {
							ex2.printStackTrace();
							return;
						}
						continue;
					}

					stopTimeoutTimer();
					startTimeoutTimer();

					try {
						stateMachine.processEvent(EVENT_SEND_FINISH);
					} catch (StateMachineDescriptorException e) {
						e.printStackTrace();
					}

					LOG.finest("MtProtocol sendThread: Finished message transmission.");
				}
			}
			LOG.finest("MtProtocol sendThread: Queue sending thread stopping.");
		}

		private synchronized void startTimeoutTimer(){
			stopTimeoutTimer();
			timeoutTimer = new MtTimer(timeout);
			timeoutTimer.setListener(MtProtocolBLEImpl.this);
			timeoutTimer.start();

		}

		private synchronized void stopTimeoutTimer(){
			if(timeoutTimer != null){
				timeoutTimer.setListener(null);
				timeoutTimer.stop();
				timeoutTimer = null;
			}
		}
	}
}
