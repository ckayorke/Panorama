package com.bosch.glm100c.easy_connect;

import java.util.List;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GLMDeviceArrayAdapter extends ArrayAdapter<GLMDevice> {
	
	private MainActivity main;
	
	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public GLMDeviceArrayAdapter(Context context, int resource, int textViewResourceId, List<GLMDevice> objects) {
		super(context, resource, textViewResourceId, objects);
		this.main = (MainActivity) context;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		GLMDevice item = getItem(position);

		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_device, null);
			holder = new ViewHolder((ImageView) convertView.findViewById(R.id.checkbox_device), (TextView) convertView.findViewById(R.id.device_name));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.deviceName.setText(item.getName());

		BluetoothService btService = main.getBluetoothService();
		
		if(btService != null) {
			BluetoothDevice currentDevice = btService.getCurrentDevice();

			if(currentDevice != null && currentDevice.getAddress().equals(item.getMacAddress())){
				if(btService.isConnected()){
					holder.checkbox.setImageResource(R.drawable.ic_check_active);
				}else{
					holder.checkbox.setImageResource(R.drawable.ic_check_disabled);
				}
				return convertView;
			}
		}

		holder.checkbox.setImageResource(R.drawable.ic_check_normal);
		return convertView;
	}

	private static class ViewHolder {

		public ImageView checkbox;
		public TextView deviceName;

		public ViewHolder(ImageView checkbox, TextView deviceName) {
			super();
			this.checkbox = checkbox;
			this.deviceName = deviceName;
		}
	}
}
