package com.bosch.glm100c.easy_connect;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bosch.glm100c.easy_connect.data.Data;
import com.bosch.glm100c.easy_connect.data.DataUpdate2;
import com.bosch.glm100c.easy_connect.data.DataUpdate3;
import com.bosch.glm100c.easy_connect.data.Level;
import com.bosch.glm100c.easy_connect.data.Password;
import com.bosch.glm100c.easy_connect.data.Project;
import com.bosch.glm100c.easy_connect.data.ProjectError;
import com.bosch.glm100c.easy_connect.data.ProjectErrorAdapter;
import com.bosch.glm100c.easy_connect.data.Room;
import com.google.gson.Gson;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class DatabaseActivity extends Activity {
    private String inputFile = "Data.zip";
    private DataService helper;
    private DataUpdate3 dataUpdate3;
    private static DatabaseActivity _DatabaseActivity;
    private int updateResutInfo = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        ProgressBar pgsBar = (ProgressBar) findViewById(R.id.pBar);
        pgsBar.setVisibility(View.VISIBLE);
        int j = getResources().getColor(R.color.colorAccent);
        pgsBar.getIndeterminateDrawable().setColorFilter(j, android.graphics.PorterDuff.Mode.MULTIPLY);
        ActivityCompat.requestPermissions(DatabaseActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        dataUpdate3 = new DataUpdate3();
        _DatabaseActivity = this;
    }
    @Override
    public void onBackPressed() {
        return;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    openView();
                }
                else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(DatabaseActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private void openView(){
        helper = DataService.getInstance(this);
        List<Password> passwords = helper.GetAllPasswordsData();
        if(passwords.size()>0){
            String p =  passwords.get(0).Name.replaceAll("@", "");
            p =  p.replace(".", "");
            inputFile = "Data_" + p + ".zip";
            helper.dataName = inputFile;
        }
        new Thread() {
            public void run() {
                prepareFile();
                _DatabaseActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if(updateResutInfo ==0){
                            Toast.makeText(getApplicationContext(), "Failed To Update DB! Try Again Later", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else if(updateResutInfo ==1){
                            deleteUploadedProjects();
                            helper.ResetData(dataUpdate3.data);
                            File dir2 = _DatabaseActivity.getApplicationContext().getDir("selectedData", Context.MODE_PRIVATE);
                            dir2.delete();
                            if(helper._error.size()>0){
                                showDialog();
                            }
                            else{
                                if(helper.dbRequestor==1) {
                                    Toast.makeText(getApplicationContext(), "DB Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), OpenProjectActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "DB Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), NeedInfoActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        else if(updateResutInfo ==2){
                            Toast.makeText(getApplicationContext(), "Wrong Login!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else if(updateResutInfo ==3){
                            Toast.makeText(getApplicationContext(), "Large dataset! Uncheck some projects and try again", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else if(updateResutInfo ==4){
                            Toast.makeText(getApplicationContext(), "Uploaded File Not Found!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else if(updateResutInfo ==5){
                            Toast.makeText(getApplicationContext(), "Data Server Issue. Try Again Later.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }.start();
    }
    public Data getValidProjects(){
        Data data = new Data();
        data.name = helper.Name;
        data.pass = helper.Pass;
        data.message = "";
        data.projects = new ArrayList<Project>();
        data.rooms = new ArrayList<Room>();
        data.levels = new ArrayList<Level>();
        helper._error.clear();
        helper.uploadedProject.clear();
        List<Project> projects =  helper.GetAllProjectsData();
        List<Room> rooms = helper.GetAllRoomsData();
        for(Project p: projects){
            ProjectError complete = helper.projectCompleted(p);
            if(complete.ReturnType == 3 && p.Completed.equals("Yes")){
                data.projects.add(p);
                helper.uploadedProject.add(p);
                data.levels.addAll(helper.GetAllLevelsData(p.ProjectId));
                for(Room r :rooms){
                    if(r.ProjectId == p.ProjectId){
                        data.rooms.add(r);
                    }
                }
            }
            else if(complete.ReturnType == 3 && p.Completed.equals("Yes") == false){
                complete.MissingCheck = "Yes";
                complete.Address = p.Address;
                complete.City = p.City  + ", " + p.State + ", " + p.ZIPCode;
                helper._error.add(complete);
            }
            else if(complete.ReturnType == 2){
                helper._error.add(complete);
            }
        }
        return data;
    }
    private void copyTo(int pid){
        File root = this.getApplicationContext().getDir("DataToZip", Context.MODE_PRIVATE);
        String name;
        for (File f : root.listFiles()) {
            if (f.isFile()) {
                String fileName = f.getName();
                String a = "_Pro_" + pid + "_";
                String b = "Pro_" + pid + "_";
                String c = "Proj_" + pid + "_Out_";
                if (fileName.contains(a) || fileName.contains(b) ||fileName.contains(c)) {
                    try {
                        InputStream in = new FileInputStream(f);
                        File targetDir = this.getApplicationContext().getDir("selectedData", Context.MODE_PRIVATE);
                        File tartFile = new File(targetDir, fileName);
                        OutputStream out = new FileOutputStream(tartFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                    }
                    catch (Exception ex) {
                    }
                }
            }
        }
    }
    private void prepareFile(){
        saveDatabaseText();
        updateDB2();
    }
    private void saveDatabaseText(){
        Data data = getValidProjects();
        Gson gson = new Gson();
        String info = gson.toJson(data);
        File dataToZipFolder = this.getApplicationContext().getDir("selectedData", Context.MODE_PRIVATE);
        File file = new File (dataToZipFolder, "db.txt");
        if(file.exists()){
            file.delete();
        }
        File dataToZipFolder2 = this.getApplicationContext().getDir("selectedData", Context.MODE_PRIVATE);
        File file2 = new File (dataToZipFolder2, "db.txt");
        try {
            FileOutputStream out = new FileOutputStream(file2);
            out.write(info.getBytes());
            out.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        for(Project p: data.projects) {
            copyTo(p.ProjectId);
        }
    }
    private void updateDB2(){
        try{
            dataUpdate3.files.clear();
            File root = this.getApplicationContext().getDir("selectedData", Context.MODE_PRIVATE);
            for (File f : root.listFiles()) {
                if(f.getName().contains(".txt")){
                    dataUpdate3.fileEvents = f;
                    continue;
                }
                dataUpdate3.files.add(f);
            }
            List<Password> passwords = helper.GetAllPasswordsData();
            if(passwords.size()>0){
                String p =  passwords.get(0).Name.replaceAll("@", "");
                p =  p.replace(".", "");
                dataUpdate3.inputFile = "Data_" + p ;
            }
            String info = dataUpdate3.execute().get();
            if(info.trim().equals("Good")){
                updateResutInfo =1;
            }
            else if(info.trim().equals("404")){
                updateResutInfo =2;
            }
            else if(info.toLowerCase().contains("request entity too large")){
                updateResutInfo =3;
            }
            else if(info.contains("File Name Not Found!")){
                updateResutInfo =4;
            }
            else{
                updateResutInfo =5;
            }
        }
        catch (Exception ex){
            updateResutInfo =0;
        }
    }
    public int deleteUploadedProjects(){
        File dir = _DatabaseActivity.getApplicationContext().getDir("DataToZip", Context.MODE_PRIVATE);
        for(Project p: helper.uploadedProject){
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    try{
                        if(children[i].trim().contains("Proj_" + p.ProjectId + "_Out")){
                            new File(dir, children[i]).delete();
                        }
                        else if(children[i].trim().contains("_Pro_" + p.ProjectId + "_Lev_")){
                            new File(dir, children[i]).delete();
                        }
                        else if(children[i].trim().contains("_Pro_" + p.ProjectId + "_3D_")){
                            new File(dir, children[i]).delete();
                        }
                    }
                    catch (Exception z){
                    }
                }
            }
            List<Room> rooms = helper.GetAllRoomsData();
            for(Room r:rooms){
                if(r.ProjectId == p.ProjectId){
                    helper.DeleteRoom(r.Id);
                }
            }
            List<Level> levels = helper.GetAllLevelsData();
            for(Level r:levels){
                if(r.ProjectId == p.ProjectId){
                    helper.DeleteLevel(r.Id);
                }
            }
            helper.DeleteProject(p.Id);
        }
        helper.uploadedProject.clear();
        return 0;
    }
    public void showDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_listview, null);
        Button btndialog = view.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper.dbRequestor==1) {
                    Toast.makeText(getApplicationContext(), "DB Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), OpenProjectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "DB Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NeedInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listviewDialog);
        ProjectErrorAdapter arrayAdapter = new ProjectErrorAdapter(this, helper._error);
        listView.setAdapter(arrayAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setCancelable(false);
        builder.setTitle("Projects Missing Information");
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dialog.getWindow().setLayout(width-20, height -200);
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
