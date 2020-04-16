package com.bosch.glm100c.easy_connect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.bosch.glm100c.easy_connect.bluetooth.BLEService;
import com.bosch.glm100c.easy_connect.bluetooth.MTBluetoothDevice;
import com.bosch.glm100c.easy_connect.exc.BluetoothNotSupportedException;
import com.bosch.mtprotocol.glm100C.connection.MtAsyncConnection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "MainActivity";
	public static final int REQUEST_CODE_ASK_PERMISSIONS_LOCATION = 41;
	private GLMDeviceArrayAdapter deviceArrayAdapter;
	private List<MTBluetoothDevice> devices = new ArrayList<>();
	//private BLEService btService;
	//private GLMDeviceController deviceController;
	//private TextView measTextView;
	private TextView devTextView;
	private EditText et1;
	private Button manipulate;
	private ArrayList<String> measures = new ArrayList<String>();
	private ArrayList<TextView> measureLabels = new ArrayList<TextView>();
	private DataService helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		helper = DataService.getInstance(this);
		String title = helper.Project.Address + ", " + helper.Project.City + ", " + helper.Project.State;
		getActionBar().setTitle(title);
		TextView info = findViewById(R.id.InfoMain1);
		info.setText("Level: " + helper.Room.LevelName + ",  Room: " + helper.Room.Name);
		Intent serviceIntent = new Intent(this, BLEService.class);
		startService(serviceIntent);
		Intent mIntent = new Intent(this, BLEService.class);
		bindService(mIntent, mConnection, BIND_AUTO_CREATE);

		deviceArrayAdapter = new GLMDeviceArrayAdapter(this, R.layout.item_device, 0, devices);
		ListView deviceListView = findViewById(R.id.device_list_view);
		deviceListView.setAdapter(deviceArrayAdapter);
		deviceListView.setOnItemClickListener(this);
		//measTextView = findViewById(R.id.measurement_text_view1);
		devTextView = findViewById(R.id.device_text_view);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
		);
        measures.clear();
		et1 = findViewById(R.id.wall1);
		et1.setVisibility(View.INVISIBLE);

		TextView tx1 = findViewById(R.id.measure1);
		TextView tx2 = findViewById(R.id.measure2);
		TextView tx3 = findViewById(R.id.measure3);
		TextView tx4 = findViewById(R.id.measure4);
		TextView tx5 = findViewById(R.id.measure5);
		TextView tx6 = findViewById(R.id.measure6);
		TextView tx7 = findViewById(R.id.measure7);
		TextView tx8 = findViewById(R.id.measure8);
		TextView tx9 = findViewById(R.id.measure9);
		TextView tx10 = findViewById(R.id.measure10);
		measureLabels.add(tx1);
		measureLabels.add(tx2);
		measureLabels.add(tx3);
		measureLabels.add(tx4);
		measureLabels.add(tx5);
		measureLabels.add(tx6);
		measureLabels.add(tx7);
		measureLabels.add(tx8);
		measureLabels.add(tx9);
		measureLabels.add(tx10);
		manipulate = findViewById(R.id.manipulate);
		manipulate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});
		if(measures.size()< 4) {
			manipulate.setEnabled(false);
		}

		Button CancelBtn = findViewById(R.id.CancelBtn);
		CancelBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});

	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		if(helper.btService.getConnectionState() == MtAsyncConnection.STATE_CONNECTING) {
			Log.w(TAG, "App is connecting, no connection started");
			return;
		}
		try {
			MTBluetoothDevice device = deviceArrayAdapter.getItem(position);
			assert device != null;
			Log.d(TAG, "Selected device " + device.getDisplayName() + "; MAC = " + device.getDevice().getAddress());
			if(helper.btService.getConnectionState() == MtAsyncConnection.STATE_CONNECTED && helper.btService.getCurrentDevice().getDevice().getAddress() != null
					&& device.getDevice().getAddress().equals(helper.btService.getCurrentDevice().getDevice().getAddress())) {
				helper.btService.disconnect();
				refreshDeviceList();
				Log.d(TAG, "App already connected to " + device.getDisplayName() + " so only disconnect");
				return;
			}
			helper.btService.connect(device);
		}
		catch (BluetoothNotSupportedException e) {
			Log.e(TAG,"BluetoothNotSupportedException",e);
		}
	}
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			helper.btService = null;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			BLEService.BLELocalBinder mLocalBinder = (BLEService.BLELocalBinder) service;
			helper.btService = mLocalBinder.getService();
		}
	};
	private void setupDeviceController(){
		if (helper.btService.isConnected()){
			if (helper.deviceController == null){
				helper.deviceController = new GLMDeviceController(helper.btService);
				helper.deviceController.init(helper.btService.getConnection(), helper.btService.getCurrentDevice());
			}
			else {
				destroyDeviceController();
				setupDeviceController();
			}
		}
		else {
			destroyDeviceController();
		}
	}
	private void destroyDeviceController(){
		if(helper.deviceController != null){
			helper.deviceController.destroy();
			helper.deviceController = null;
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		Log.w(TAG, "Device activity on pause: cancel discovery");
		if(helper.btService != null) {
			helper.btService.cancelDiscovery();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		//setTitle(R.string.app_name);
		refreshDeviceList();
		IntentFilter filter = new IntentFilter(BLEService.ACTION_DEVICE_LIST_UPDATED);
		this.registerReceiver(mReceiver, filter);
		filter = new IntentFilter(BLEService.ACTION_CONNECTION_STATUS_UPDATE);
		this.registerReceiver(mReceiver, filter);
		filter = new IntentFilter(GLMDeviceController.ACTION_SYNC_CONTAINER_RECEIVED);
		this.registerReceiver(mReceiver, filter);
		if (Build.VERSION.SDK_INT >= 23) {
			int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
			if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
				requestLocationPermission();
				return;
			}
		}
		if (helper.btService != null && helper.btService.enableBluetooth(this)) {
			Log.w(TAG, "Device activity on resume: start discovery");

			if(helper.btService.getConnectionState() == MtAsyncConnection.STATE_CONNECTED ){
				String deviceName = helper.btService.getCurrentDevice().getDisplayName();
				devTextView.setText(getResources().getString(R.string.connected_to) + " " +deviceName);
				et1.setVisibility(View.VISIBLE);
				et1.requestFocus();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
					et1.setShowSoftInputOnFocus(false);
				}
				else {
					et1.setTextIsSelectable(true);
				}
				return;
			}
			startDiscovery();
		}
	}
	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BLEService.REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				startDiscovery();
			} else {
				Toast.makeText(this,getString(R.string.bluetooth_on_denied), Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS_LOCATION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (helper.btService != null && helper.btService.enableBluetooth(this)) {
						Log.w(TAG, "Device activity on permission result: start discovery");
						startDiscovery();
					}
				}
				else {
					Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	@TargetApi(23)
	private void requestLocationPermission() {
		if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
			showPermissionMessageOKCancel(getString(R.string.request_location_permission),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS_LOCATION);
						}
					});
			return;
		}
		requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS_LOCATION);
	}
	private void showPermissionMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(this)
				.setMessage(message)
				.setPositiveButton(getString(R.string.ok), okListener)
				.setNegativeButton(getString(R.string.cancel), null)
				.create()
				.show();
	}
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@SuppressLint("SetTextI18n")
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null && BLEService.ACTION_CONNECTION_STATUS_UPDATE.equals(intent.getAction())) {
				refreshDeviceList();
				if(deviceArrayAdapter != null){
					deviceArrayAdapter.notifyDataSetChanged();
				}
				int connectionStatus = intent.getIntExtra(BLEService.EXTRA_CONNECTION_STATUS, MtAsyncConnection.STATE_NONE);
				if(connectionStatus == MtAsyncConnection.STATE_CONNECTED ){
					setupDeviceController();
					String deviceName = intent.getStringExtra(BLEService.EXTRA_DEVICE);
					devTextView.setText(getResources().getString(R.string.connected_to) + " " + deviceName);
                    et1.setVisibility(View.VISIBLE);
                    et1.requestFocus();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                        et1.setShowSoftInputOnFocus(false);
                    }
                    else {
                        et1.setTextIsSelectable(true);
                    }
				}
				else {
					destroyDeviceController();
					devTextView.setText(getResources().getString(R.string.no_device_connected));
				}
			}
			else if(intent != null && BLEService.ACTION_DEVICE_LIST_UPDATED.equals(intent.getAction())) {
				refreshDeviceList();
				
			}
			else if(intent != null && GLMDeviceController.ACTION_SYNC_CONTAINER_RECEIVED.equals(intent.getAction())) {
				if(!Objects.requireNonNull(intent.getExtras()).isEmpty()) {
					float measurement = intent.getFloatExtra(GLMDeviceController.EXTRA_MEASUREMENT, 0);
					//measTextView.setText(Float.toString(measurement) + getResources().getString(R.string.meter));
                    updateView(Float.toString(measurement));
				}
			}
			else {
				Log.w(TAG, "Unkown intent or intent is null: ignore");
			}
		}
	};
    private void updateView(String measure){
        double m = Double.parseDouble(measure);
        m = m * 3.28084;
        int feet = (int)m;
        double inch = m - feet;
        String inche = String.format("%.2f", inch * 12);
        measures.add(measure);
        if(measures.size()< 4) {
			et1.setVisibility(View.VISIBLE);
			et1.requestFocus();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
				et1.setShowSoftInputOnFocus(false);
			}
			else {
				et1.setTextIsSelectable(true);
			}
			if(measures.size()==1){
				et1.setHint("Confirm Length Measurement");
				measureLabels.get(measures.size() -1).setText( "Length Measurement: " +feet + "' " + inche + "''");
				measureLabels.get(measures.size() -1).setVisibility(View.VISIBLE);
			}
			else if(measures.size()==2){
				et1.setHint("Measure Width Of Room");
				measureLabels.get(measures.size() -1).setText( "Length Measurement Confirmed: " +feet + "' " + inche + "''");
				measureLabels.get(measures.size() -1).setVisibility(View.VISIBLE);
			}
			else if(measures.size()==3){
				et1.setHint("Confirm Width Measurement");
				measureLabels.get(measures.size() -1).setText( "Width Measurement: " +feet + "' " + inche + "''");
				measureLabels.get(measures.size() -1).setVisibility(View.VISIBLE);
			}
			if(measures.size()==1){
				helper.Room.RoomLength= measure;
			}
			else{
				helper.Room.RoomLength= helper.Room.RoomLength + ", " +measure;
			}
			helper.UpdateRoom(helper.Room);
		}
        else{
        	et1.setVisibility(View.INVISIBLE);
        	if(measures.size()==4){
				measureLabels.get(measures.size() -1).setText( "Width Measurement Confirmed: " +feet + "' " + inche + "''");
				measureLabels.get(measures.size() -1).setVisibility(View.VISIBLE);
			}
        	else {
				measureLabels.get(measures.size() - 1).setText("Additional Measurement " + (measures.size() -4) + ": " + feet + "' " + inche + "''");
				measureLabels.get(measures.size() - 1).setVisibility(View.VISIBLE);
			}

			helper.Room.RoomLength= helper.Room.RoomLength + ", " +measure;
		    helper.UpdateRoom(helper.Room);
			if(measures.size()> 3) {
				manipulate.setEnabled(true);
			}
			if(measures.size()>9){
				loopMeasureEnd();
			}
			else {
				loopMeasure();
			}
		}
    }
	private void loopMeasureEnd(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setTitle("COMPLEX ROOM");
		builder.setMessage("The Room Is Too Complex. If There Are Additional Walls, Try To Make 2 Rooms.");
		AlertDialog dialog = builder.create();
		dialog.show();
		colorAlertDialogTitle(dialog);
	}
	private void loopMeasure(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				et1.setVisibility(View.VISIBLE);
				et1.requestFocus();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
					et1.setShowSoftInputOnFocus(false);
				}
				else {
					et1.setTextIsSelectable(true);
				}
				et1.setHint("Optional: Additional Measurement " + (measures.size() - 3));
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});
		builder.setTitle("Measurement");
		builder.setMessage("Are there any more wall to measure?");
		AlertDialog dialog = builder.create();
		dialog.show();
		colorAlertDialogTitle(dialog);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			refreshDeviceList();
			startDiscovery();
			return true;
		case R.id.bluetooth_settings:
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			startActivity(settingsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private synchronized void refreshDeviceList(){
		devices.clear();
		if(helper.btService != null) {
			devices.addAll(helper.btService.getVisibleDevices());
		}
		Collections.sort(devices, new Comparator<MTBluetoothDevice>() {

			@Override
			public int compare(MTBluetoothDevice lhs, MTBluetoothDevice rhs) {
				return lhs.getDisplayName().compareToIgnoreCase(rhs.getDisplayName());
			}
		});

		deviceArrayAdapter.notifyDataSetChanged();
	}
	private void startDiscovery() {
		if (helper.btService != null) {
			try {
				helper.btService.startDiscovery();
			} catch (BluetoothNotSupportedException be) {
				Log.e(TAG, "Bluetooth not supported");
				be.printStackTrace();
			}
		}
	}
	public BLEService getBluetoothService() {
		return helper.btService;
	}
	public  void colorAlertDialogTitle(AlertDialog dialog) {
		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		if (dividerId != 0) {
			View divider = dialog.findViewById(dividerId);
			divider.setBackgroundColor(getResources().getColor(R.color.primaryDark));
		}
		int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
		if (textViewId != 0) {
			TextView tv = (TextView) dialog.findViewById(textViewId);
			tv.setTextColor(getResources().getColor(R.color.primaryDark));
		}
	}
}
