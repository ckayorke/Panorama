package com.bosch.glm100c.easy_connect;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WifiActivity extends Activity {
    private WifiManager mainWifiObj;
    private WifiScanReceiver wifiReciever;
    private ListView list;
    private ArrayList<String> wifis = new ArrayList<String>();
    private ArrayList<String> filtered = new ArrayList<String>();
    private ArrayAdapter adapter;
    private TextView wif_text_view;
    private WifiActivity _WifiActivity;
    private WifiConfiguration cameraWifi;
    private int camConnected =0;
    private DataService helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        getActionBar().setTitle("Pairing 360 Camera");
        helper = DataService.getInstance(this);
        wifiReciever = new WifiScanReceiver();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{

                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},100);
            }
            else{
                UIComplete();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            UIComplete();
        }
        else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},100);

                }
            }
        }
    }
    private void UIComplete(){
        mainWifiObj = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wif_text_view = findViewById(R.id.wif_text_view);
        _WifiActivity = this;
        list= findViewById(R.id.wifi_list);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.label, filtered);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ssid = ((TextView) view).getText().toString().trim();
                if(ssid.length()<1){
                    return;
                }
                connectToWifi(ssid);
            }
        });

        Button GoToRoomEdit = findViewById(R.id.GoToRoomEdit);
        GoToRoomEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(helper.toProjects == 1){
                    Intent intent = new Intent(getApplicationContext(), ProjectCommandsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }


            }
        });
        Button GoToImageList = findViewById(R.id.GoToImageList);
        GoToImageList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(camConnected ==0){
                    Toast.makeText(getApplicationContext(), "No Camera Connected!", Toast.LENGTH_SHORT).show();
                }
                else {
                    navigateToImageList();
                }
            }
        });
    }
    public void navigateToImageList(){
        Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        return;
    }
    protected void onPause() {
        unregisterReceiver(wifiReciever);
        if (cameraWifi !=null) {
            mainWifiObj.removeNetwork(cameraWifi.networkId);
            mainWifiObj.saveConfiguration();
        }
        super.onPause();
    }
    protected void onResume() {
        isMobileData();
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if (mainWifiObj.isWifiEnabled()==false) {
                    mainWifiObj.setWifiEnabled(true);
                }
                if(mainWifiObj !=null) {
                    camConnected =0;
                    mainWifiObj.startScan();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void finallyConnect(String networkSSID) {
        String networkPass = networkSSID.substring(7,networkSSID.indexOf("."));
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
        // remember id
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"\"" + networkSSID + "\"\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        mainWifiObj.addNetwork(conf);

    }
    private void connectToWifi(final String wifiSSID) {
        finallyConnect(wifiSSID);
    }
    private class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            boolean isGo = false;
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals (intent.getAction())) {
                NetworkInfo netInfo = intent.getParcelableExtra (WifiManager.EXTRA_NETWORK_INFO);
                if (ConnectivityManager.TYPE_WIFI == netInfo.getType ()) {
                    WifiInfo info = mainWifiObj.getConnectionInfo ();
                    String ssid  = info.getSSID();
                    if(ssid.trim().toUpperCase().contains("THETA")){
                        isGo = true;
                        int ip = info.getIpAddress();
                        String ipAddress = Formatter.formatIpAddress(ip);
                        wif_text_view.setText(ssid );
                        camConnected =1;
                    }
                    else{
                        camConnected =0;
                        wif_text_view.setText("No Camera Connected!");
                    }
                }
            }
            if(isGo){
                return;
            }
            String d = intent.getAction().toUpperCase();
            filtered.clear();
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis.clear();
            turnOff();

            for (int i = 0; i < wifiScanList.size(); i++) {
                String name = wifiScanList.get(i).toString();
                String[] temp = name.split(",");
                String k = temp[0].substring(5).trim().toUpperCase();
                if (k.contains("THETA")) {
                    wifis.add(name);
                }
                else{
                }
            }
            int counter = 0;
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");
                String k = temp[0].substring(5).trim().toUpperCase();
                if (k.contains("THETA")) {
                    filtered.add(k);
                }
                counter++;
            }

            adapter.notifyDataSetChanged();
        }

        private void turnOff(){
            WifiManager wifiManager = (WifiManager)_WifiActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            if(list==null){
                return;
            }
            for( WifiConfiguration i : list ) {
                String name = i.SSID;
                if (name.trim().toUpperCase().contains("THETA")) {
                }
                else {
                    wifiManager.removeNetwork(i.networkId);
                    wifiManager.saveConfiguration();
                }
            }
        }
    }

    private void isMobileData(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                helper.internetConnectionType = "WIFI";
                WifiManager wifiManager  = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo ();
                String ssid  = info.getSSID();
                if(ssid.trim().toUpperCase().contains("THETA")){
                    helper.internetConnectionType = "MOBILE";
                    helper.internetConnectionName = "";
                }
                else{
                    helper.internetConnectionName = ssid.replaceAll("\"", "");
                    int k =0;
                }
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                helper.internetConnectionType = "MOBILE";
                helper.internetConnectionName = "";
            }
        }
        else {
            helper.internetConnectionType = "MOBILE";
            helper.internetConnectionName = "";
        }
    }
}
