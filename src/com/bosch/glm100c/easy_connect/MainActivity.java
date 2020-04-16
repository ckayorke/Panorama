package com.bosch.glm100c.easy_connect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bosch.glm100c.easy_connect.BluetoothService.LocalBinder;
import com.bosch.glm100c.easy_connect.exc.BluetoothNotSupportedException;
import com.bosch.mtprotocol.glm100C.connection.MtAsyncConnection;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "com.bosch.glm100c.easy_connect.MainActivity";
	
	private ListView deviceListView;
	private GLMDeviceArrayAdapter deviceArrayAdapter;
	private List<GLMDevice> devices = new ArrayList<GLMDevice>();
	private BluetoothService btService;
	private GLMDeviceController deviceController;
	private TextView measTextView;
	private TextView devTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Bind Bluetooth service
		Intent serviceIntent = new Intent(this, BluetoothService.class);
		startService(serviceIntent);
		
		Intent mIntent = new Intent(this, BluetoothService.class);
		bindService(mIntent, mConnection, BIND_AUTO_CREATE);
		
		setContentView(R.layout.activity_main);

		deviceArrayAdapter = new GLMDeviceArrayAdapter(this, R.layout.item_device, 0, devices);
		deviceListView = (ListView) findViewById(R.id.device_list_view);
		deviceListView.setAdapter(deviceArrayAdapter);
		deviceListView.setOnItemClickListener(this);
		measTextView = (TextView) findViewById(R.id.measurement_text_view);
		devTextView = (TextView) findViewById(R.id.device_text_view);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

		// If app is connecting - do nothing
		if(btService.getConnectionState() == MtAsyncConnection.STATE_CONNECTING) {
			Log.w(TAG, "App is connecting, no connection started");
			return;
		}

		try {
			GLMDevice device = deviceArrayAdapter.getItem(position);
			String deviceAddress = btService.deviceAddress;
			Log.d(TAG, "Selected device " + device.getName() + "; MAC = " + deviceAddress);
			if(btService.getConnectionState() == MtAsyncConnection.STATE_CONNECTED && device.getMacAddress().equals(deviceAddress)) {
				btService.deviceAddress = null;
				btService.disconnect();
				refreshDeviceList();
				Log.d(TAG, "App already connected to " + device.getName() + " so only disconnect");
				return;
			}

			Set<BluetoothDevice> visibleDevices = btService.getVisibleDevices();
			
			for(BluetoothDevice bluetoothDevice: visibleDevices){
				if(bluetoothDevice.getAddress().equals(device.getMacAddress())){
					Log.d(TAG, "Start connection to " + bluetoothDevice.getName() + "; MAC = " + bluetoothDevice.getAddress());
					btService.connect(bluetoothDevice);
					break;
				}
			}
			
		} catch (BluetoothNotSupportedException e) {
			Log.e(TAG,"BluetoothNotSupportedException",e);
		}

	}

	/**
	 * Binds the Bluetooth service to the activity
	 */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			btService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder mLocalBinder = (LocalBinder) service;
			btService = mLocalBinder.getService();
		}
	};
	
	/**
	 * Initializes the GLMDeviceController class, that will handle messages from and to GLM device
	 */
	private void setupDeviceController(){
		if(btService.isConnected()){
			if(deviceController == null){
				deviceController = new GLMDeviceController(btService);
				deviceController.init(btService);
			}else{
				destroyDeviceController();
				setupDeviceController();
			}
		}else{
			destroyDeviceController();
		}
	}

	/**
	 * Destroys the GLMDeviceController, when it is not needed anymore
	 */
	private void destroyDeviceController(){
		if(deviceController != null){
			deviceController.destroy();
			deviceController = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {

		super.onPause();

		unregisterReceiver(mReceiver);
		
		// stop Bluetooth scan
		Log.w(TAG, "Device activity on pause: stop discovery");
		if(btService != null) {
			btService.stopDiscovery();
		}
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		setTitle(R.string.app_name);

		refreshDeviceList();

		// register receivers
		IntentFilter filter = new IntentFilter(BluetoothService.ACTION_DEVICE_LIST_UPDATED);
		this.registerReceiver(mReceiver, filter);
		filter = new IntentFilter(BluetoothService.ACTION_CONNECTION_STATUS_UPDATE);
		this.registerReceiver(mReceiver, filter);
		filter = new IntentFilter(GLMDeviceController.ACTION_SYNC_CONTAINER_RECEIVED);
		this.registerReceiver(mReceiver, filter);
		
		// start Bluetooth scan
		Log.w(TAG, "Device activity on resume: start discovery");
		if(btService != null) {
			btService.startDiscovery();
		}
	}

	/**
	 *  The BroadcastReceiver that listens for discovered devices and new measurements
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent != null && BluetoothService.ACTION_CONNECTION_STATUS_UPDATE.equals(intent.getAction())) {
				
				// Device was connected or disconnected - handle device list accordingly
				refreshDeviceList();
				if(deviceArrayAdapter != null){
					deviceArrayAdapter.notifyDataSetChanged();
				}
				// If device was connected -> start GLMDeviceController to handle communication
				int connectionStatus = intent.getIntExtra(BluetoothService.EXTRA_CONNECTION_STATUS, MtAsyncConnection.STATE_NONE);
				if(connectionStatus == MtAsyncConnection.STATE_CONNECTED ){
					setupDeviceController();
					String deviceName = intent.getStringExtra(BluetoothService.EXTRA_DEVICE);
					devTextView.setText(getResources().getString(R.string.connected_to) + deviceName);
				} else {
					destroyDeviceController();
					devTextView.setText(getResources().getString(R.string.no_device_connected));
				}
				
			} else if(intent != null && BluetoothService.ACTION_DEVICE_LIST_UPDATED.equals(intent.getAction())) {
				
				// Device list changed
				refreshDeviceList();
				
			} else if(intent != null && GLMDeviceController.ACTION_SYNC_CONTAINER_RECEIVED.equals(intent.getAction())) {
				
				// Measurement received
				if(!intent.getExtras().isEmpty()) {
					float measurement = intent.getFloatExtra(GLMDeviceController.EXTRA_MEASUREMENT, 0);
					measTextView.setText(Float.toString(measurement) + getResources().getString(R.string.meter));
				}
				
			} else {
				
				// Received intent is null or not known -> ignore
				Log.w(TAG, "Unkown intent or intent is null: ignore");
				
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh:
			refreshDeviceList();
			if (btService != null) {
				btService.startDiscovery();
			}
			return true;
		case R.id.bluetooth_settings:
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			startActivity(settingsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Used to refresh the device list shown. If Bluetooth scanning is not enabled, the list will be empty
	 */
	private synchronized void refreshDeviceList(){

		devices.clear();

		Set<BluetoothDevice> visibleDevices = new HashSet<BluetoothDevice>();
		if(btService != null) {
			visibleDevices.addAll(btService.getVisibleDevices());
		}
		
		for(BluetoothDevice pairedDevice: visibleDevices){
			GLMDevice device = new GLMDevice();
			device.setName(pairedDevice.getName());
			device.setMacAddress(pairedDevice.getAddress());
			devices.add(device);
		}


		Collections.sort(devices, new Comparator<GLMDevice>() {

			@Override
			public int compare(GLMDevice lhs, GLMDevice rhs) {
				int result = lhs.getName().compareToIgnoreCase(rhs.getName());
				return result;
			}
		});

		deviceArrayAdapter.notifyDataSetChanged();
	}
	
	public BluetoothService getBluetoothService() {
		return btService;
	}
}
