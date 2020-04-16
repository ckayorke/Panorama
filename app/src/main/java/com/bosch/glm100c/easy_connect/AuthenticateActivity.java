package com.bosch.glm100c.easy_connect;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bosch.glm100c.easy_connect.data.DataUpdate;
import com.bosch.glm100c.easy_connect.data.Password;

import java.util.List;
public class AuthenticateActivity extends Activity {
    private AuthenticateActivity DataContext;
    private Button GoToLogin;
    private TextView connected;
    EditText password;
    EditText userName;
    private WifiManager mainWifiObj;
    private AlertDialog dialogBuilder;
    private  DataService helper;
    private int  mobileCheck =0;
    int internetCheckCounter = 0;
    private int connectCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        DataContext = this;
        helper = DataService.getInstance(this);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        connected = findViewById(R.id.connected);
        connected.setVisibility(View.GONE);
        GoToLogin = findViewById(R.id.GoToLogin);
        GoToLogin.setEnabled(false);
        GoToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.Name = userName.getText().toString();
                helper.Pass = password.getText().toString();
                List<Password> passwords = helper.GetAllPasswordsData();
                if(passwords.size()>0){
                    passwords.get(0).Name = helper.Name;
                    passwords.get(0).Pass = helper.Pass;
                    helper.UpdatePassword( passwords.get(0));
                }
                else{
                    Password p = new Password();
                    p.Name =helper.Name;
                    p.Pass = helper.Pass;
                    p.Id = -2;
                    helper.InsertPasswword(p);
                }

                Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        Button GoBackToProjects = findViewById(R.id.GoBackToProjects);
        GoBackToProjects.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleFinish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        return;
    }
    @Override
    protected void onPause() {
        dialogBuilder.dismiss();
        super.onPause();
    }
    private void handleFinish(){
        if(helper.dbRequestor ==1){
            Intent intent = new Intent(getApplicationContext(), OpenProjectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), NeedInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    protected void onResume() {
        super.onResume();
        internetCheckCounter = 0;
        mobileCheck =0;
        connectCounter = 0;
        showNetworkCheck();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkInternetConnection();
            }
        }, 7000);
    }
    private void showNetworkCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataContext);
        builder.setTitle("Internet Connection");
        builder.setMessage("Please wait while we check network connection!");
        dialogBuilder = builder.create();
        dialogBuilder.show();
        colorAlertDialogTitle(dialogBuilder);
    }
    private void turnOff() {
        WifiManager wifiManager = (WifiManager) DataContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        if (list == null) {
            return;
        }
        for (WifiConfiguration i : list) {
            String name = i.SSID;
            if (name.trim().toUpperCase().contains("THETA")) {
                wifiManager.removeNetwork(i.networkId);
                wifiManager.saveConfiguration();
            }
        }
    }
    private void updateTextBoxes(){
        List<Password> passwords = helper.GetAllPasswordsData();
        if(passwords.size()>0){
            userName.setText(passwords.get(0).Name);
            password.setText(passwords.get(0).Pass);
        }
    }
    private void showMobileDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DataContext);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleFinish();
            }
        });
        builder.setTitle("Mobile Data Connection");
        builder.setMessage("You internet connection is using mobile data. Do you wish to continue?");
        AlertDialog dialog = builder.create();
        dialogBuilder.dismiss();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    private void toBackToProjects(){
        dialogBuilder.dismiss();
        Toast.makeText(getApplicationContext(), "Check WIFI Connection!", Toast.LENGTH_SHORT).show();
        handleFinish();
    }
    private void isMobileData(){
        ConnectivityManager cm = (ConnectivityManager) DataContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                dialogBuilder.dismiss();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if(mobileCheck ==0){
                    mobileCheck =1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkInternetConnection();
                        }
                    }, 2000);
                }
                else{
                    showMobileDialog();
                }
            }
        }
        else {
            toBackToProjects();
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void forceConnectToWifi() {
        ConnectivityManager cm = (ConnectivityManager) DataContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if ((info != null) && info.isAvailable()) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                NetworkRequest requestedNetwork = builder.build();
                ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        ConnectivityManager.setProcessDefaultNetwork(network);
                    }
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                    }
                };
                cm.registerNetworkCallback(requestedNetwork, callback);
            }
            else{
                showNoInternet();
            }
        }
        else {
            showNoInternet();
        }
    }
    public boolean internetIsConnected() {
        try {
            DataUpdate dataUpdate = new DataUpdate();
            String info = dataUpdate.execute("").get();
            if (info.trim().equals("Good")) {
                GoToLogin.setEnabled(true);
                connected.setVisibility(View.GONE);
                dialogBuilder.hide();
                updateTextBoxes();
                isMobileData();
            }
            else {
                if (connectCounter < 5) {
                    connectCounter = connectCounter + 1;
                    forceConnectToWifi();
                    try {
                        Thread.sleep(2000);
                        checkInternetConnection();
                    }
                    catch (Exception ex) {
                        checkInternetConnection();
                    }
                }
                else {
                    connectCounter = 0;
                    dialogBuilder.hide();
                    dialogBuilder.dismiss();
                    showNoInternet();
                }
            }
            return true;
        }
        catch (Exception e) {
            Log.d("What", e.getMessage());
            dialogBuilder.hide();
            showNoInternet();
            return false;
        }
    }
    private void checkInternetConnection() {
        try {
            if (internetCheckCounter > 15) {
                dialogBuilder.dismiss();
                handleFinish();
                return;
            }
            internetCheckCounter = internetCheckCounter + 1;
            WifiManager wifiManager = (WifiManager) DataContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            SupplicantState k = wifiInfo.getSupplicantState();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                String ssid = wifiInfo.getSSID();
                if (ssid.trim().toUpperCase().contains("THETA")) {
                    turnOff();
                    try {
                        Thread.sleep(3000);
                        checkInternetConnection();
                    }
                    catch (Exception ex) {
                        checkInternetConnection();
                    }
                }
                else {
                    internetIsConnected();
                }
            }
            else {
                try {
                    Thread.sleep(3000);
                    if (wifiInfo.getSupplicantState() == SupplicantState.SCANNING) {
                        checkInternetConnection();
                    } else if (wifiInfo.getSupplicantState() == SupplicantState.INVALID ||
                            wifiInfo.getSupplicantState() == SupplicantState.UNINITIALIZED ||
                            wifiInfo.getSupplicantState() == SupplicantState.DISCONNECTED) {
                        mainWifiObj = (WifiManager) DataContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        if (mainWifiObj.isWifiEnabled() == false) {
                            mainWifiObj.setWifiEnabled(true);
                            mainWifiObj.startScan();
                            checkInternetConnection();
                        } else {
                            mainWifiObj.startScan();
                            checkInternetConnection();
                        }
                    }
                    else {
                        checkInternetConnection();
                    }
                } catch (Exception ex) {
                    checkInternetConnection();
                }
            }
        }
        catch (Exception m){
            handleFinish();
        }
    }
    private void showNoInternet() {
        dialogBuilder.dismiss();
        connected.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(DataContext);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please check wifi connection  and try again!");
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
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
