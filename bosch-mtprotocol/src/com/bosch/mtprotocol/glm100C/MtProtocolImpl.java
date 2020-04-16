/**
 * 
 */
package com.bosch.mtprotocol.glm100C;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.bosch.mtprotocol.MtConnection;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.MtProtocol;
import com.bosch.mtprotocol.glm100C.MtTimer.MtTimerListener;
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




/**
 * @author AndrejsC
 *
 */
public class MtProtocolImpl implements MtProtocol, MtFrameConstants, MtTimerListener, MtProtocolStates{
	
	private final static Logger LOG = Logger.getLogger(MtProtocol.class.getName());
	
	private static final int RX_FIFO_SIZE = 2500; // for big extended packages
	private static final int TX_FIFO_SIZE = 255;

	private StateMachine stateMachine;
	private Set<MTProtocolEventObserver> observers = new HashSet<MtProtocol.MTProtocolEventObserver>();

	private MtFrameFactory frameFactory = new FrameFactoryImpl();
	private MtMessageFactory messageFactory = new MessageFactoryImpl();

	private LinkedBlockingQueue<MtMessage> outgoingQueue = new LinkedBlockingQueue<MtMessage>();
	private MtRequestFrame currentRequest;

	private SendThread sendThread;
	private ReceiveThread receiveThread;

	private MtConnection connection;
	private MtFrameByteReader frameReader;
	
	private ArrayList<MtMessage> messages = new ArrayList<MtMessage>();

	private int timeout;

	@Override
	public void reset(){
		initialize(connection);
	}
	
	public void initialize(MtConnection connection){
		
		stateMachine = new MtProtocolStateMachine();

		this.connection = connection;

		frameReader = null;
		currentRequest = null;
		messages.clear();
		outgoingQueue.clear();

		if (sendThread != null) sendThread.interrupt();
		sendThread = new SendThread();
		sendThread.start();

		if (receiveThread != null) receiveThread.interrupt();
		receiveThread = new ReceiveThread();
		receiveThread.setPriority(Thread.MAX_PRIORITY);
		receiveThread.start();

		try {
			stateMachine.processEvent(EVENT_INITIALIZE_SLAVE);
		} catch (StateMachineDescriptorException e) {
			throw new RuntimeException("Can't initialize MTProtocol", e);
		}
	}

	/** Add outgoing message object to queue. */
	public synchronized void sendMessage(MtMessage message) {
		if(stateMachine.getCurrentState() == STATE_SLAVE_LISTENING){
			try {
				stateMachine.processEvent(EVENT_SWITCH_TO_MASTER);
			} catch (StateMachineDescriptorException e) {
				e.printStackTrace();
				return;
			}
		}
		LOG.finest("MtProtocol: Adding outgoing message to queue.");
		if (outgoingQueue.offer(message) == false) {
			throw new RuntimeException("MtProtocol: Error: outgoing queue is full, message dropped.");
		}
	}


	@Override
	public synchronized void onTimerTick() {

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


	/** Set MtProtocol to slave mode. Starts listening for incoming message. */
	public synchronized boolean setSlave() {
		try {
			stateMachine.processEvent(EVENT_SWITCH_TO_SLAVE);
		} catch (StateMachineDescriptorException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** Set MtProtocol to master mode. Waits for messages to be sent with sendMessage(...) or sendRequest(...). */
	public synchronized boolean setMaster() {
		try {
			stateMachine.processEvent(EVENT_SWITCH_TO_MASTER);
		} catch (StateMachineDescriptorException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	private synchronized void checkTransactionCompleted() {
		if (frameReader != null && frameReader.isFrameRcvComplete()) {
			
			LOG.finest("MtProtocol: Buffer receive complete or timeout");
			
			if(receiveThread != null){
				receiveThread.stopTimeoutTimer();
			}
			
			// end of buffer => set correct state machine state
			try {
				stateMachine.processEvent(EVENT_RECEIVE_FINISH);
				if(outgoingQueue.isEmpty() && stateMachine.getCurrentState() == STATE_MASTER_READY){
					stateMachine.processEvent(EVENT_SWITCH_TO_SLAVE);
				}
			} catch (StateMachineDescriptorException e) {
				e.printStackTrace();
			}
			
			for (MtMessage message : messages) {
				if(message == null){
					LOG.finest("Erorr occured when turning received frame to message");
					notifyObservers(new MtProtocolFatalErrorEvent());
				}else{
					LOG.finest("Received message: " + message);
					notifyObservers(new MtProtocolReceiveMessageEvent(message));
				}

				notify();
			}

			messages.clear();
			frameReader = null;
		} else {
			LOG.warning("MtProtocol: Frame receive not complete");
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

	private void notifyObservers(MTProtocolEvent event){
		for(MTProtocolEventObserver observer: observers){
			observer.onEvent(event);
		}
	}

	public void destroy(){
		observers.clear();
		
		// Stop Tx and Rx threads
		if(sendThread != null){
			sendThread.stopTimeoutTimer();
			sendThread.interrupt();
		}
		if(receiveThread != null){
			receiveThread.stopTimeoutTimer();
			receiveThread.interrupt();
		}
	}

	@Override
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}

	/* (non-Javadoc)
	 * @see de.bosch.mtprotocoldecom.bosch.mtprotocolr(com.bosch.mtprotocol.de.bosch.mtprotocollEventObserver)
	 */
	@Override
	public void addObserver(MTProtocolEventObserver observer) {
		observers.add(observer);
	}

	/* (ncom.bosch.mtprotocolee com.bosch.mtprotocol.Mdcom.bosch.mtprotocolver(com.bosch.mtprotocol.Mtde.bosch.mtprotocolventObserver)
	 */
	@Override
	public void removeObserver(MTProtocolEventObserver observer) {
		observers.remove(observer);
	}

	private class SendThread extends Thread{

		private MtTimer timeoutTimer;
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			LOG.finest("MtProtocol sendThread: Queue sending thread started.");
			while (!sendThread.isInterrupted()) {
				MtMessage outMsg = null;
				try {
					LOG.finest("MtProtocol sendThread: Getting message from queue.");
					outMsg = outgoingQueue.take();
					LOG.finest("MtProtocol sendThread: Got message from queue.");
				} catch (InterruptedException e) {
					return; //die if interrupted
				}

				synchronized (this) {
					while (stateMachine.getCurrentState() != STATE_MASTER_READY) {
						try {
							LOG.finest("MtProtocol sendThread: Waiting for state READY.");
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
					} catch (IOException e) {
						e.printStackTrace();
						//	setSendError();
					}
					
					stopTimeoutTimer();
					
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
		
		public synchronized void startTimeoutTimer(){
			stopTimeoutTimer();
			timeoutTimer = new MtTimer(timeout);
			timeoutTimer.setListener(MtProtocolImpl.this);
			timeoutTimer.start();
			
		}
		
		public synchronized void stopTimeoutTimer(){
			if(timeoutTimer != null){
				timeoutTimer.setListener(null);
				timeoutTimer.stop();
				timeoutTimer = null;
			}
		}
	}

	private class ReceiveThread extends Thread{

		private MtTimer timeoutTimer;
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			while(!isInterrupted()){

				if(connection == null || !connection.isOpen()){
					continue;
				}

				byte[] buffer = new byte[RX_FIFO_SIZE];
				int length = 0;
				try {
					length = connection.read(buffer);
					//LOG.warning("Byte 1 (mode) is " + String.format("%02x", buffer[0]));
					//LOG.warning("Byte 2 (command) is " + String.format("%02x", buffer[1]));
					//LOG.warning("Buffer length = " + length);
				} catch (IOException e) {
					e.printStackTrace();
					connection.closeConnection();
					return;
				}

				if(length == 0) { // TODO: resolve
					continue;
				}

				startTimeoutTimer();

				if(length != 0){
					//LOG.warning("Bytes in buffer: " + length);
					if(stateMachine.getCurrentState() == STATE_SLAVE_LISTENING){
						// Update state
						try {
							stateMachine.processEvent(EVENT_RECEIVE_START);
						} catch (StateMachineDescriptorException e) {
							e.printStackTrace();
						}
					}

					if(stateMachine.getCurrentState() == STATE_SLAVE_RECEIVING
							|| stateMachine.getCurrentState() == STATE_MASTER_RECEIVING){

						if(frameReader == null){
							frameReader = new MtFrameByteReader(new byte[RX_FIFO_SIZE], RX_FIFO_SIZE);
						}

						synchronized (frameReader) {
							for(int  i = 0; i < length; i++){
								byte b = buffer[i];
								//LOG.warning("Byte " + i + " = " + String.format("%02X ", b));
								frameReader.append(b);
								if (frameReader.isFrameRcvComplete()) {
									//LOG.warning("Frame with " + (i + 1) + " bytes received; Bytes in buffer: " + length);
									checkMessageComplete();
									if (i < (length - 1)) { // it is not the last byte => another message is following
										//LOG.warning("Frame was not last frame in buffer; expect another frame");
										frameReader = null;
										frameReader = new MtFrameByteReader(new byte[RX_FIFO_SIZE], RX_FIFO_SIZE);
									} else {
										//LOG.warning("Frame was last frame in buffer; proceed to transaction completion");
									}
								//} else if (i == length - 1) {
									//LOG.warning("Frame receiving done, but frame incomplete");
								}
							}
						}


					}else{
						LOG.warning("MtProtocolImpl: Ignoring received data; Current state: " + stateMachine.getCurrentState());
					}
				}

				checkTransactionCompleted();
			}
		}
		
		public synchronized void startTimeoutTimer(){
			stopTimeoutTimer();
			timeoutTimer = new MtTimer(timeout);
			timeoutTimer.setListener(MtProtocolImpl.this);
			timeoutTimer.start();
			
		}
		
		public synchronized void stopTimeoutTimer(){
			if(timeoutTimer != null){
				timeoutTimer.setListener(null);
				timeoutTimer.stop();
				timeoutTimer = null;
			}
		}
	}
}
