package com.bosch.glm100c.easy_connect;

import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtProtocol.MTProtocolEvent;
import com.bosch.mtprotocol.MtProtocol.MTProtocolEventObserver;
import com.bosch.mtprotocol.glm100C.MtProtocolImpl;
import com.bosch.mtprotocol.glm100C.event.MtProtocolFatalErrorEvent;
import com.bosch.mtprotocol.glm100C.event.MtProtocolReceiveMessageEvent;
import com.bosch.mtprotocol.glm100C.event.MtProtocolRequestTimeoutEvent;
import com.bosch.mtprotocol.glm100C.message.edc.EDCInputMessage;
import com.bosch.mtprotocol.glm100C.message.edc.EDCOutputMessage;
import com.bosch.mtprotocol.glm100C.message.laser.LaserOffMessage;
import com.bosch.mtprotocol.glm100C.message.laser.LaserOnMessage;
import com.bosch.mtprotocol.glm100C.message.sync.SyncInputMessage;
import com.bosch.mtprotocol.glm100C.message.sync.SyncOutputMessage;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class GLMDeviceController implements MTProtocolEventObserver {
	private static final String TAG = "com.bosch.glm100C.GLMDeviceController";

	public static final String ACTION_ERROR = "ERROR";

	public static final String ACTION_SYNC_CONTAINER_RECEIVED = "SYNC_CONTAINER_RECEIVED";

	public static final String EXTRA_MEASUREMENT = "MEASUREMENT";

	private Context context;

	private MtProtocolImpl protocol;

	private BluetoothDevice bluetoothDevice;

	private boolean initSyncRequest;

	private boolean ready;

	public GLMDeviceController(Context cvontext) {
		super();
		this.context = cvontext;
	}

	public void turnLaserOn(){
		if(isReady()){
			ready = false;
			protocol.sendMessage(new LaserOnMessage());
		}
	}

	public void turnLaserOff(){
		if(isReady()){
			ready = false;
			protocol.sendMessage(new LaserOffMessage());
		}
	}

	/**
	 * Starts sync mode between app and GLM device
	 * When sync mode is started the GLM device will send every event to the app
	 */
	public void turnAutoSyncOn(){
		if(isReady()){
			ready = false;
			if(BluetoothService.validateGLM100Name(bluetoothDevice)) {
				// GLM 100 device
				SyncOutputMessage requestDoSync = new SyncOutputMessage();
				requestDoSync.setSyncControl(SyncOutputMessage.MODE_AUTOSYNC_CONTROL_ON);
				protocol.sendMessage(requestDoSync);
				Log.w(TAG, "Sync started GLM 100...");
			} else if(BluetoothService.validateGLM50Name(bluetoothDevice) || BluetoothService.validatePLRName(bluetoothDevice)) {
				// GLM 50 device or PLR device
				final EDCOutputMessage requestEDCSync = new EDCOutputMessage();
				requestEDCSync.setSyncControl(EDCOutputMessage.MODE_AUTOSYNC_CONTROL_ON);
				requestEDCSync.setDevMode(EDCOutputMessage.READ_ONLY_MODE);
				protocol.sendMessage(requestEDCSync);
				if (BluetoothService.validateGLM50Name(bluetoothDevice)) {
					Log.d(TAG, "Sync started GLM 50...");
				} else {
					Log.d(TAG, "Sync started PLR...");
				}
			}
		}
	}

	/**
	 * Stops the sync mode
	 * GLM device will not send any events to the app, after this command is send
	 */
	public void turnAutoSyncOff(){
		if(isReady()){
			ready = false;
			if(BluetoothService.validateGLM100Name(bluetoothDevice)) {
				// GLM 100 device
				SyncOutputMessage requestDoSync = new SyncOutputMessage();
				requestDoSync.setSyncControl(SyncOutputMessage.MODE_AUTOSYNC_CONTROL_OFF);
				protocol.sendMessage(requestDoSync);
			} else if(BluetoothService.validateGLM50Name(bluetoothDevice) || BluetoothService.validatePLRName(bluetoothDevice)) {
				// GLM 50 device or PLR device
				final EDCOutputMessage requestEDCSync = new EDCOutputMessage();
				requestEDCSync.setSyncControl(EDCOutputMessage.MODE_AUTOSYNC_CONTROL_OFF);
				requestEDCSync.setDevMode(EDCOutputMessage.READ_ONLY_MODE);
				protocol.sendMessage(requestEDCSync);
			}
		}
	}

	@Override
	public void onEvent(MTProtocolEvent event) {

		ready = true;

		if(event instanceof MtProtocolFatalErrorEvent){

			// fatal error
			Log.d(TAG, "Received MtProtocolFatalErrorEvent");
			protocol.reset();
			context.sendBroadcast(new Intent(ACTION_ERROR));

		} else if(event instanceof MtProtocolReceiveMessageEvent) {
			MtMessage message = ((MtProtocolReceiveMessageEvent) event).getMessage();
			if(message instanceof SyncInputMessage) {
				SyncInputMessage syncMessage = (SyncInputMessage) message;

				if(initSyncRequest) { // Ignore first response
					initSyncRequest = false;
					return;
				}
				Log.d(TAG, "SyncInputMessageReceived: " + syncMessage.toString());
				if(syncMessage.getMode() == SyncInputMessage.MEAS_MODE_SINGLE && syncMessage.getLaserOn() == 0) {
					// Handle only distance measurements
					broadcastMeasurement(ACTION_SYNC_CONTAINER_RECEIVED, syncMessage.getResult());
				}
			} else if(message instanceof EDCInputMessage) {

				if(initSyncRequest) { // Ignore first response
					initSyncRequest = false;
					return;
				}
				Log.d(TAG, "Received EDC: " + message.toString());
				EDCInputMessage edcMessage = (EDCInputMessage) message;
				Log.d(TAG, "EDCInputMessageReceived: " + edcMessage.toString());
				if(edcMessage.getDevMode() == EDCInputMessage.MODE_SINGLE_DISTANCE) {
					// Handle only distance measurements
					broadcastMeasurement(ACTION_SYNC_CONTAINER_RECEIVED, edcMessage.getResult());
				}
			} else {

				Log.d(TAG, "Received Unknown message");
			}
		} else if(event instanceof MtProtocolRequestTimeoutEvent){
			Log.d(TAG, "Received MtProtocolRequestTimeoutEvent");
			context.sendBroadcast(new Intent(ACTION_ERROR));
		} else {
			Log.e(TAG, "Received unknown event");
		}
		initSyncRequest = false;
	}

	private void broadcastMeasurement(String action, float value)
	{
		Intent i = new Intent(action);
		i.putExtra(EXTRA_MEASUREMENT, value);
		context.sendBroadcast(i);
	}

	/**
	 * Delivers information if MT Protocol is ready for communication
	 * @return TRUE, if protocol is ready and FALSE otherwise
	 */
	public boolean isReady(){
		return protocol != null && ready;
	}

	/**
	 * Initializes the device controller. Must be called once before using the controller
	 * @param connection Existing BluetoothConnection connected to BluetoothDevice
	 * @param bluetoothDevice The connected BluetoothDevice
	 */
	protected void init(BluetoothService btService){

		destroy();

		this.bluetoothDevice = btService.getCurrentDevice();

		MtProtocolImpl protocol = new MtProtocolImpl();
		protocol.addObserver(this);
		protocol.setTimeout(5000);
		protocol.initialize(btService.getConnection());
		this.protocol = protocol;

		ready = true;

		initSyncRequest = true;
		turnAutoSyncOn();
	}

	/**
	 * Destroys the device controller. Always call after BluetoothConnection is lost
	 */
	protected void destroy(){

		if(protocol != null){
			protocol.removeObserver(this);
			protocol.destroy();
			protocol = null;
		}
	}

	/**
	 * Return the current Bluetooth device
	 * @return connected Bluetooth device if any; null otherwise
	 */
	public BluetoothDevice getBTDevice() {
		return bluetoothDevice;
	}
}
