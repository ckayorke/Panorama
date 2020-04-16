package com.bosch.glm100c.easy_connect;
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
public class NeedInfoActivity extends Activity {
    private DataService helper;
    private ProjectAdapter2 projectAdapter2;
    private ListView simpleList;
    private List<Project> list;
    private TextView labelCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_info);

        helper = DataService.getInstance(this);
        list =helper.GetAllProjectsData();
        labelCount = findViewById(R.id.labelCount);
        List<Project> list2 = new ArrayList<Project>();
        for(Project p: list){
            if(p.Status==2){
                list2.add(p);
            }
        }
        simpleList = findViewById(R.id.ProjectView);
        projectAdapter2 = new ProjectAdapter2(this, R.layout.activity_projects, 0, list2, helper);
        simpleList.setAdapter(projectAdapter2);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project item = projectAdapter2.getItem(position);
                if(item.Address.trim().equals("Open Projects") || item.Address.trim().equals("Needs Information Projects")){
                    return;
                }
                helper.SetProject(projectAdapter2.getItem(position));
                Intent intent = new Intent(getApplicationContext(), ProjectCommandsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        Button GoToLogin = findViewById(R.id.GoToLogin7);
        GoToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.dbRequestor =2;
                restoredOldConnection();
                Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        Button GoToDecision7 = findViewById(R.id.GoToDecision7);
        GoToDecision7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DecisionActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        TextView _label = findViewById(R.id.missingData);
        _label.setVisibility(View.GONE);
        TextView _completeData = findViewById(R.id.completeData);
        _label.setVisibility(View.GONE);
        _completeData.setVisibility(View.GONE);
        for(Project p: list2){
            int pState = helper.projectCompleted(p).ReturnType;
            if(pState==2){
                _label.setVisibility(View.VISIBLE);
                break;
            }
        }
        for(Project p: list2){
            if(p.Completed.equals("Yes")){
                _completeData.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(list.size()<1){
            labelCount.setVisibility(View.VISIBLE);
        }
        else{
            labelCount.setVisibility(View.INVISIBLE);
        }
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
            Toast.makeText(this, "Project cannot be upload because it has an empty level or a room is missing some information!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Empty Project cannot be upload!", Toast.LENGTH_SHORT).show();
        }
    }
}
