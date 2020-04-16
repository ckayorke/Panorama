package com.bosch.glm100c.easy_connect;

import android.Manifest;
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

public class InternetActivity extends Activity {
    private WifiManager mainWifiObj;
    private WifiScanReceiver wifiReciever;
    private ListView list;
    private ArrayList<String> wifis = new ArrayList<String>();
    private ArrayList<String> filtered = new ArrayList<String>();
    private ArrayAdapter adapter;
    private TextView wif_text_view;
    private InternetActivity _InternetActivity;
    private int intConnected =0;
    private String ssid="";
    private WifiConfiguration _wifiConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        wifiReciever = new WifiScanReceiver();
        UIComplete();
    }
    @Override
    public void onBackPressed() {
        return;
    }
    private void UIComplete(){
        mainWifiObj = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wif_text_view = findViewById(R.id.wif_text_view);
        _InternetActivity = this;
        list= findViewById(R.id.wifi_list);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.label, filtered);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ssid = ((TextView) view).getText().toString().trim();
                if(ssid.length()<1){
                    return;
                }
                connectToSSID();
            }
        });

        Button GoToDecision5 = findViewById(R.id.GoToDecision5);
        GoToDecision5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), DecisionActivity.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(intent);
            }
        });

        Button GoToLogin5 = findViewById(R.id.GoToLogin5);
        GoToLogin5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(intConnected ==0){
                    Toast.makeText(getApplicationContext(), "No Internet Connected!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
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
                Toast.makeText(getApplicationContext(), "Scanning!", Toast.LENGTH_SHORT).show();
                if(mainWifiObj !=null) {
                    intConnected =0;
                    mainWifiObj.startScan();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void connectToSSID()
    {
        if(mainWifiObj.isWifiEnabled()==false){
            mainWifiObj.setWifiEnabled(true);
        }

        for (WifiConfiguration wifiConfiguration : mainWifiObj.getConfiguredNetworks())
        {
            if (wifiConfiguration.SSID.equals("\"" + ssid + "\""))
            {
                mainWifiObj.enableNetwork(wifiConfiguration.networkId, true);
                break;
            }
        }
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
                        wif_text_view.setText("No Wifi Connected!");
                        intConnected =1;
                    }
                    else{
                        wif_text_view.setText(ssid );
                        intConnected =1;
                    }
                }
            }
            filtered.clear();
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis.clear();
            turnOff();
            for (int i = 0; i < wifiScanList.size(); i++) {
                String name = wifiScanList.get(i).toString();
                String[] temp = name.split(",");
                String k = temp[0].substring(5).trim().toUpperCase();
                if (k.contains("THETA")) {
                }
                else{
                    if(k.length()>0) {
                        wifis.add(name);
                    }
                }
            }

            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");
                String k = temp[0].substring(5).trim().toUpperCase();
                if (k.contains("THETA")) {
                }
                else{
                    filtered.add(k);
                }
            }
            adapter.notifyDataSetChanged();
        }
        private void turnOff(){
            WifiManager wifiManager = (WifiManager)_InternetActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            if(list==null){
                return;
            }
            for( WifiConfiguration i : list ) {
                String name = i.SSID;
                if (name.trim().toUpperCase().contains("THETA")) {
                    wifiManager.removeNetwork(i.networkId);
                    wifiManager.saveConfiguration();
                }
            }
        }
    }
}
