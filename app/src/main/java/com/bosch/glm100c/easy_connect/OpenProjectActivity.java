package com.bosch.glm100c.easy_connect;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bosch.glm100c.easy_connect.data.*;

import java.util.ArrayList;
import java.util.List;
public class OpenProjectActivity extends Activity {
    private DataService helper;
    private ProjectAdapter projectAdapter;
    private ListView simpleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_project);
        helper = DataService.getInstance(this);
        List<Project> list =helper.GetAllProjectsData();
        TextView labelCount = findViewById(R.id.labelCount);
        if(list.size()<1){
            labelCount.setVisibility(View.VISIBLE);
        }
        else{
            labelCount.setVisibility(View.INVISIBLE);
        }
        List<Project> list2 = new ArrayList<Project>();
        for(Project p: list){
            if(p.Status==0){
                list2.add(p);
            }
        }
        list.clear();
        simpleList = findViewById(R.id.ProjectView);
        projectAdapter = new ProjectAdapter(this, R.layout.activity_projects, 0, list2, helper);
        simpleList.setAdapter(projectAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project item = projectAdapter.getItem(position);
                if(item.Address.trim().equals("Open Projects") || item.Address.trim().equals("Needs Information Projects")){
                    return;
                }
                helper.SetProject(projectAdapter.getItem(position));
                Intent intent = new Intent(getApplicationContext(), ProjectCommandsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        Button GoToLogin = findViewById(R.id.GoToLogin);
        GoToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.dbRequestor =1;
                restoredOldConnection();
                Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Button GoToDecision6 = findViewById(R.id.GoToDecision6);
        GoToDecision6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DecisionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        reLoad();
    }
    @Override
    public void onBackPressed() {
        return;
    }
    private void restoredOldConnection(){
        try {
            WifiManager wifiManager  = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
                String s = wifiConfiguration.SSID.trim().replaceAll("\"", "");
                if (s.equals(helper.internetConnectionName) && s.length()>0  && helper.internetConnectionType.equals("WIFI")) {
                    wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                    Log.i("Authenticate", "connectToWifi: will enable " + wifiConfiguration.SSID);
                    wifiManager.reconnect();
                    break;
                }
            }
        }
        catch (Exception e) {
            Log.e("Authenticate", "connectToWifi: Missing network configuration.");
        }
    }

    public void alertIncomplete(int id) {
        if (id == 1) {
            Toast.makeText(this, "Project cannot be upload because it is missing some information!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Empty Project cannot be upload!", Toast.LENGTH_SHORT).show();
        }
    }
    public void reLoad(){

        TextView _completeData = findViewById(R.id.completeData);
        _completeData.setVisibility(View.GONE);
        List<Project> list2 =helper.GetAllProjectsData();
        for(Project p: list2){
            if(p.Completed.equals("Yes")){
                _completeData.setVisibility(View.VISIBLE);
                break;
            }
        }

        TextView _label = findViewById(R.id.missingData);
        _label.setVisibility(View.GONE);
        for(Project p: list2){
            int pState = helper.projectCompleted(p).ReturnType;
            if(pState == 2){
                _label.setVisibility(View.VISIBLE);
                break;
            }
            else if(pState == 3  && p.Completed.equals("No")){
                _label.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}
