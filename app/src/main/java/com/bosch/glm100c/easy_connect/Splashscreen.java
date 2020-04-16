package com.bosch.glm100c.easy_connect;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bosch.glm100c.easy_connect.data.Project;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
public class Splashscreen extends Activity {
    private DataService helper;
    private int counter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        int k =0;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onResume(){
        super.onResume();
        helper = DataService.getInstance(this);
        List<Project> list =helper.GetTestData();
        if(list !=null) {
            list.clear();
        }
        requestPermissions();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if((requestCode == 100) &&  allGranted(grantResults)){
            UIComplete();
        }
        else {
            Intent i = new Intent(Splashscreen.this, PermissionActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }
    private boolean allGranted(@NonNull int[] grantResults){
        boolean k = true;
        int count = grantResults.length;
        for(int i =0; i< count-1; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED ){
                k = false;
                break;
            }
        }
        return k;
    }
    private void requestPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED||

                            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||

                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},100);
            }
            else{
                UIComplete();
            }
        }
    }
    private void UIComplete(){
       // Clean();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(counter<2) {
                    counter = counter +1;
                    Intent i = new Intent(Splashscreen.this, DecisionActivity.class);
                    //Intent i = new Intent(Splashscreen.this, Level2Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        return;
    }
    private void Clean(){
        helper.DeleteAllProjects();
        helper.DeleteAllLevels();
        helper.DeleteAllRooms();
        File dir = this.getApplicationContext().getDir("DataToZip", Context.MODE_PRIVATE);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
        File dir2 = this.getApplicationContext().getDir("Data.zip", Context.MODE_PRIVATE);
        if(dir2.exists()){
            dir2.delete();
        }
    }
}
