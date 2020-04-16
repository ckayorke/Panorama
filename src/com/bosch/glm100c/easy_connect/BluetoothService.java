package com.bosch.glm100c.easy_connect;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.bosch.glm100c.easy_connect.exc.BluetoothNotSupportedException;
import com.bosch.mtprotocol.glm100C.connection.MtAsyncConnection;
import com.bosch.mtprotocol.glm100C.connection.MtAsyncConnection.MTAsyncConnectionObserver;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BluetoothService extends Service implements MTAsyncConnectionObserver{
	private static final String TAG = "com.bosch.glm100c.easy_connect.BluetoothService";

	public static final String ACTION_DEVICE_LIST_UPDATED = "DEVICE_LIST_UPDATED";
	public static final String ACTION_CONNECTION_STATUS_UPDATE = "CONNECTION_STATUS_UPDATE";

	public static final String EXTRA_DEVICE = "DEVICE";
	public static final String EXTRA_CONNECTION_STATUS = "CONNECTION_STATUS";

	private BluetoothConnection connection;
	private Set<BluetoothDevice> visibleDevices = new HashSet<BluetoothDevice>();
	private Set<BluetoothDevice> lastDiscoveredDevices = new HashSet<BluetoothDevice>();
	
	public String deviceAddress;

	// This is the object that receives interactions from clients.
	private final IBinder mBinder = new LocalBinder();

	/**
	 * @return Currently connected Bluetooth device
	 * @throws BluetoothNotSupportedException 
	 */
	public BluetoothDevice getCurrentDevice() {

		if(deviceAddress != null){
			try {
				return getBluetoothAdapter().getRemoteDevice(deviceAddress);
			} catch (BluetoothNotSupportedException e) {
				Log.e(TAG, "Bluetooth getCurrentDevice() failed", e);
			}
		}
		return null;
	}

	/**
	 * Start Bluetooth scan
	 * @return true if start successful, false otherwise
	 */
	public boolean startDiscovery() {
		try {
			if (isBluetoothSupported() && isBluetoothEnabled() && getBluetoothAdapter().isDiscovering()) {
				getBluetoothAdapter().cancelDiscovery();
			}
			
			return getBluetoothAdapter().startDiscovery();
		} catch (BluetoothNotSupportedException e) {
			Log.e(TAG, "Bluetooth cancel discovery exception", e);
		}

		return false;
	}
	
	/**
	 * Stop Bluetooth scan
	 * @return true if stop successful, false otherwise
	 */
	public boolean stopDiscovery(){
		try {
			if (isBluetoothSupported() && isBluetoothEnabled()){
				return getBluetoothAdapter().cancelDiscovery();
			}
		} catch (BluetoothNotSupportedException e) {
			Log.e(TAG, "Bluetooth cancel discovery exception", e);
		}
		return false;
	}


	/**
	 * Open connection to BluetoothDevice device
	 * @return true if successful, false otherwise
	 * @throws BluetoothException 
	 */
	public boolean connect(BluetoothDevice device) throws BluetoothNotSupportedException{

		deviceAddress = device.getAddress();
		disconnect();
		stopDiscovery();

		Log.d(TAG, "Starting 'just works' connection to " + device.getName());
		connection = new BluetoothConnection(device);
		connection.addObserver(this);
		connection.openConnection();
		return true;
	}

	/**
	 * Close an existing connection
	 */
	public void disconnect(){
		if(connection != null){
			connection.closeConnection();
			connection.removeObserver(this);
		}

	}

	@Override
	public void onConnectionStateChanged(MtAsyncConnection connection) {

		Intent intent = new Intent(ACTION_CONNECTION_STATUS_UPDATE);
		intent.putExtra(EXTRA_CONNECTION_STATUS, connection.getState());
		intent.putExtra(EXTRA_DEVICE, getCurrentDevice().getName());
		sendBroadcast(intent);

		if(connection.getState() == MtAsyncConnection.STATE_NONE || connection.getState() == MtAsyncConnection.STATE_TIMEOUT){
			if(this.connection != null){
				this.connection.removeObserver(this);
				this.connection.closeConnection();
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Starting bluetooth service...");

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcast when device state is changed
		filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.registerReceiver(mReceiver, filter);

		try { // Check if Bluetooth is supported. Throw exception if not.
			getBluetoothAdapter();
		} catch (BluetoothNotSupportedException e) {
			Log.e(TAG, "BluetoothNotSupportedException", e);
		}
		
		return START_STICKY;
	}

	/**
	 * The BroadcastReceiver that listens for discovered devices
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// New Bluetooth device found -> update device list
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, "Device found: " + device.getName());
				if(validateGLM100Name(device) || validateGLM50Name(device) || validatePLRName(device)) {
					visibleDevices.add(device);
					lastDiscoveredDevices.add(device);
					Intent i = new Intent(ACTION_DEVICE_LIST_UPDATED);  
					sendBroadcast(i);
				}
				
			}else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
				// notify
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, "Bond state changed: device=" + device.getName() + " state=" + device.getBondState());

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Log.d(TAG, "Discovery finished");
				visibleDevices = lastDiscoveredDevices;
				lastDiscoveredDevices.clear();
				Intent i = new Intent(ACTION_DEVICE_LIST_UPDATED);  
				sendBroadcast(i);
				
			} else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, "Connected to device=" + device.getName() + " macAddress: " + device.getAddress());

			} else if(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, "Disconnect request from device=" + device.getName());
				handleDisconnect(device);
			} else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, "Disconnected device=" + device.getName() + " macAddress: " + device.getAddress());
				handleDisconnect(device);
			} else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
				Log.d(TAG, "State of Bluetooth adapter changed.");
			}
		}

		private void handleDisconnect(BluetoothDevice device){
			if(deviceAddress != null && deviceAddress.equals(device.getAddress())){
				disconnect();
			}
		}
	};
	
	/**
	 * Get currently discovered Bluetooth devices
	 * @return set of visible Bluetooth devices
	 */
	public Set<BluetoothDevice> getVisibleDevices() {
		return visibleDevices;
	}

	/**
	 * Binder necessary to start and bind BluetoothService
	 * @author tos2si1
	 *
	 */
	public class LocalBinder extends Binder {
		public BluetoothService getService() {
			return BluetoothService.this;
		}
	}

	/**
	 * Will return current connection state
	 * @return connection state as integer from MTAsyncConnection state list
	 */
	public int getConnectionState(){
		if(connection != null){
			return connection.getState();
		}else{
			return MtAsyncConnection.STATE_NONE;
		}
	}

	/**
	 * Will check if service is connected
	 * @return true if service connected, false otherwise
	 */
	public boolean isConnected(){
		return connection != null && connection.getState() == MtAsyncConnection.STATE_CONNECTED;
	}

	/**
	 * Will return the current BluetoothConnection
	 * @return the connection
	 */
	public BluetoothConnection getConnection() {
		return connection;
	}
	
	/**
	 * Gets the system Bluetooth Adapter. Throws exception if adapter not available
	 * @return bluetoothAdapter
	 * @throws BluetoothNotSupportedException
	 */
	protected BluetoothAdapter getBluetoothAdapter() throws BluetoothNotSupportedException {
		BluetoothAdapter bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null) {
			throw new BluetoothNotSupportedException();
		}
		return bluetoothAdapter;
	}
	
	/**
	 * Checks if Bluetooth is enabled (switched on) on the Android device
	 * @return true if adapter is enabled, false otherwise
	 * @throws BluetoothException
	 */
	public boolean isBluetoothEnabled() throws BluetoothNotSupportedException {
		return getBluetoothAdapter().isEnabled();
	}

	/**
	 * Checks if Bluetooth is available on the Android device.
	 * @return true if Bluetooth is supported, false otherwise
	 */
	public boolean isBluetoothSupported(){
		return BluetoothAdapter.getDefaultAdapter() != null;
	}
	
	/**
	 * Validates if the paired device is a GLM 100 device
	 * @param device
	 * @return
	 */
	public static boolean validateGLM100Name(BluetoothDevice device){
		return device.getName() != null 
				&& device.getName().toLowerCase(Locale.getDefault()).contains("bosch")
				&& device.getName().toLowerCase(Locale.getDefault()).contains("glm")
				&& device.getName().toLowerCase(Locale.getDefault()).contains("100");
	}
	
	/**
	 * Validates if the paired device is a GLM 50 device
	 * @param device
	 * @return
	 */
	public static boolean validateGLM50Name(BluetoothDevice device){
		return device.getName() != null 
				&& device.getName().toLowerCase(Locale.getDefault()).contains("bosch")
				&& device.getName().toLowerCase(Locale.getDefault()).contains("glm")
				&& device.getName().toLowerCase(Locale.getDefault()).contains("50");
	}
	
	/**
	 * Validates if the paired device is a PLR device
	 * @param device
	 * @return
	 */
	public static boolean validatePLRName(BluetoothDevice device){
		return device.getName() != null 
				&& device.getName().toLowerCase(Locale.getDefault()).contains("bosch")
				&& device.getName().toLowerCase(Locale.getDefault()).contains("plr")
				&& (device.getName().toLowerCase(Locale.getDefault()).contains("30")
				|| device.getName().toLowerCase(Locale.getDefault()).contains("40")
				|| device.getName().toLowerCase(Locale.getDefault()).contains("50"));
	}

}
