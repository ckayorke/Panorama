package com.bosch.glm100c.easy_connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bosch.glm100c.easy_connect.data.Level;
import com.bosch.glm100c.easy_connect.data.Project;
import com.bosch.glm100c.easy_connect.data.ProjectErrorAdapter;
import com.bosch.glm100c.easy_connect.data.Room;
import com.bosch.glm100c.easy_connect.data.RoomName;
import com.bosch.glm100c.easy_connect.data.RoomNameAdapter;
import com.bosch.glm100c.easy_connect.view.CustomView;
import com.bumptech.glide.load.engine.Resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LevelActivity extends Activity {
    private CustomView mCustomView;
    private DataService helper;
    private String levelName = "";
    private static boolean isShowSaving = false;
    LevelActivity _LevelActivity;
    private Context DataContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        _LevelActivity = this;
        DataContext = this.getApplicationContext();
        mCustomView = (CustomView) findViewById(R.id.customView);
        helper = DataService.getInstance(this);
        String title = helper.Project.Address + ", " + helper.Project.City + ", " + helper.Project.State;
        getActionBar().setTitle(title);
        Button floorNumber = findViewById(R.id.floorNumber);
        floorNumber.setText("Floor: " + helper.Level.Name);
        Button AddingRooms = findViewById(R.id.AddingRooms);
        AddingRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.saveLayout();
                Toast.makeText(getApplicationContext(), "Saving Room!", Toast.LENGTH_SHORT).show();
                if(helper.isProcessing ==1){
                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if(helper.isProcessing ==1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(_LevelActivity, "Please wait while we save the level info!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                timer.cancel();
                                doneWithLevelSave();
                            }
                        }
                    },0, 5000);
                }
                else{
                    doneWithLevelSave();
                }
            }
        });

        Button GoToProjectCommands = findViewById(R.id.GoToProjectCommands);
        GoToProjectCommands.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(helper.isProcessing ==1){
                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if(helper.isProcessing ==1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(_LevelActivity, "Please wait while we save the level info!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                timer.cancel();
                                doneWithLevelEdit();
                            }
                        }
                    },0, 5000);
                }
                else{
                    doneWithLevelEdit();
                }
            }
        });

        Button doneToProjectCommands = findViewById(R.id.doneToProjectCommands);
        doneToProjectCommands.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(helper.isProcessing ==1){
                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if(helper.isProcessing ==1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(_LevelActivity, "Please wait while we save the level info!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                timer.cancel();
                                doneWithLevelEdit();
                            }
                        }
                    },0, 5000);
                }
                else{
                    doneWithLevelEdit();
                }
            }
        });

        getName();
        if(isShowSaving){
            showToast();
        }
    }

    private void doneWithLevelEdit(){
        if(isShowSaving){
            return;
        }
        Intent intent = new Intent(getApplicationContext(), ProjectCommandsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void doneWithLevelSave(){
        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
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
    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCustomView.saveLayout();
            Bitmap b = Screenshot.takescreenshotOfRootView(mCustomView);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            cleanData();
            insertInPrivateStorage(byteArray);
        }
        catch(Exception p){

        }
    }
    public static void showImageSaving(boolean saving) {
        isShowSaving = true;
    }
    private Toast mToastToShow;
    public void showToast() {
        int toastDurationInMilliSeconds = 10000;
        mToastToShow = Toast.makeText(this, "Saving Floor Layout. Wait!", Toast.LENGTH_LONG);
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }
            public void onFinish() {
                isShowSaving = false;
                mToastToShow.cancel();
            }
        };
        mToastToShow.show();
        toastCountDown.start();
    }
    private void cleanData(){
        try {
            File dataToZipFolder = getApplicationContext().getDir("DataToZip", Context.MODE_PRIVATE);
            File file = new File(dataToZipFolder, levelName);
            if(file.exists()){
                file.delete();
            }
        }
        catch (Exception ex){
        }
    }
    private void insertInPrivateStorage(byte[]bytes){
        try {
            File dataToZipFolder = getApplicationContext().getDir("DataToZip", Context.MODE_PRIVATE);
            File file1 = new File(dataToZipFolder, levelName);
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(bytes);
            fos.close();
            helper.Level.PicName= levelName;
            helper.UpdateLevel(helper.Level);
        }
        catch (Exception ex){
            int k =0;
        }
    }
    private void getName(){
        Project j = helper.GetProjectData(helper.Level.ProjectId);
        if(j.Status==0) {
            Level Item = helper.Level;
            String levName = Item.Name;
            levelName = levName.replaceAll(" ", "");
            levelName = "Pro_" + Item.ProjectId + "_" + levelName + "1.jpg";
        }
        else{
            Level Item = helper.Level;
            String levName = Item.Name;
            levelName = levName.replaceAll(" ", "");
            levelName = "Pro_" + Item.ProjectId + "_" + levelName + "2.jpg";
        }
    }
    private void editRoomInfo(final int option){
        Room _SelectedRoom = helper.Room ;
        if(_SelectedRoom ==null){
            return;
        }
        if(option==1){
            if(_SelectedRoom.PictureName.trim().equals("")){
                navigateToTakingPicture();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToTakingPicture();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setTitle("Room Picture");
                builder.setMessage("Taking More Room Picture. Are you sure?");
                AlertDialog dialog = builder.create();
                dialog.show();
                colorAlertDialogTitle(dialog);
            }
        }
        else if(option==2){
            if(_SelectedRoom.RoomLength.trim().equals("")){
                navigateToTakingMeasurement();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToTakingMeasurement();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setTitle("Room Measurements");
                builder.setMessage("Retaking Room Measurements. Are you sure?");
                AlertDialog dialog = builder.create();
                dialog.show();
                colorAlertDialogTitle(dialog);
            }
        }
    }
    private void navigateToTakingPicture(){
        helper.toProjects = 0;
        if(helper.isProcessing ==1){
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  if(helper.isProcessing ==1){
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(_LevelActivity, "Please wait while we ready the camera!", Toast.LENGTH_LONG).show();
                          }
                      });
                  }
                  else{
                      timer.cancel();
                      navigateToTakingPictureContinue();
                  }
              }
             },0, 5000);
        }
        else{
            navigateToTakingPictureContinue();
        }
    }
    private void navigateToTakingPictureContinue(){
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            String ssid = wifiInfo.getSSID();
            if(ssid.trim().toUpperCase().contains("THETA")){
                helper.toImageList =0;
                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else{
                helper.toImageList =1;
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        else{
            helper.toImageList =1;
            Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    private void navigateToTakingMeasurement(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void deleteSelectRoom(){
        helper.DeleteRoom(helper.Room.Id);
        helper.Room = null;
        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void deleteDialog(){
        Room _SelectedRoom = helper.Room;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]
                        {"Yes", "No"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mCustomView.cleanDeleteRoom(helper.Room);
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.setTitle("Deleting " + _SelectedRoom.Name  + ". Are Your Sure?");
        if(_SelectedRoom !=null){
            if(_SelectedRoom.PictureName.trim().equals("") && _SelectedRoom.RoomLength.trim().equals("")){
            }
            else{
                builder.setTitle("Deleting "  + _SelectedRoom.Name +  ". This room has data. Are your sure?");
            }
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    public void getAction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]
                        {"Delete Room", "Take Room Picture", "Take Room Measurement", "Delete All Rooms", "Shift Layout Left", "Shift Layout Right", "Shift Layout Up", "Shift Layout Down"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteDialog();
                                break;
                            case 1:
                                editRoomInfo(1);
                                break;
                            case 2:
                                editRoomInfo(2);
                                break;
                            case 3:
                                deleteAllRoom();
                                break;
                            case 4:
                                shiftSideToSide(1);
                                break;
                            case 5:
                                shiftSideToSide(2);
                                break;
                            case 6:
                                shiftTopToDown(1);
                                break;
                            case 7:
                                shiftTopToDown(2);
                                break;
                        }


                    }
                }
        );
        String tName = helper.Room.LevelName + ": " + helper.Room.Name;
        builder.setTitle(tName);
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    public void handleRoomDoubleTap(Room r){
        helper.Room = r;
        getAction();
    }
    private Room DoSelectItem(RoomName item) {
        int id = helper.GetRoomCount() + 1;
        helper.SetRoomCount(id);
        Room room = new Room();
        room.Id =-20;
        room.RoomId = id;
        room.ProjectId = helper.Level.ProjectId;
        room.LevelId = helper.Level.LevelId;
        room.Name = item.Text;
        room.PictureName = "";
        room.RoomLength = "";
        room.RoomWidth = "";
        room.LevelName = helper.Level.Name;
        Project exProject = helper.GetProjectData(helper.Level.ProjectId);
        room.Address = exProject.Address;
        room.State = exProject.State;
        room.City = exProject.City;
        room.ZIP = exProject.ZIPCode;
        room.Connectors = "";
        return room;
    }
    public void updateRoom(Room room){
        Room exRoom = helper.GetRoomDataByProjectIdAndLevelIdAndRoomName(room.ProjectId, room.LevelId, room.Name);
        if(exRoom.Id== -20)
        {
            helper.InsertRoom(room);
        }
        helper.Room= helper.GetRoomDataByProjectIdAndLevelIdAndRoomName(room.ProjectId, room.LevelId, room.Name);
    }
    public void updateRoom2(Room room){
        helper.UpdateRoom(room);
    }
    public void getRoomName(final Point p) {
        final List<RoomName> list =helper.GetAllRoomNameData();
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        ArrayList<String> existedR  = new ArrayList<String>();
        for(Room n: list2){
            existedR.add(n.Name.trim().toLowerCase());
        }
        String[] items = new String[list.size()];
        int count =0;
        final List<RoomName> list3 = new ArrayList<RoomName>();
        for(RoomName n: list){
            if(existedR.contains(n.Value.trim().toLowerCase())){
                continue;
            }
            items[count] = n.Value;
            count = count +1;
            list3.add(n);
        }
       String[] items2 = new String[ count + 1];
       for(int t=0; t<count; t++){
           items2[t] = items[t];
       }


        RoomName name = new RoomName();
        name.Text = "OTHER";
        name.Value = "OTHER";
        name.isCheck = false;
        list3.add(name);
        items2[count] = "OTHER";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SELECT ROOM NAME");
        builder.setItems(items2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                RoomName j = list3.get(item);
                if(j.Value.equals("OTHER")){
                    handel(p);
                }
                else{
                    Room r = DoSelectItem(j);
                    mCustomView.startLayout(r, p);
                }






            }
        });
        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        colorAlertDialogTitle(alert);
    }
    public void deleteRoom(int x, int y){
        mCustomView.deleteUnfinished(x, y);
    }
    public void deleteAllRoom(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETE CONFIRMATION");
        builder.setMessage("Are you sure you want to delete all rooms, their photos and measurements for this level?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteAllConfirmation();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    public void deleteAllConfirmation(){
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        for(Room n: list2){
            if(n.PictureName.trim().length()>0){
                String [] pNames = n.PictureName.split(",");
                for(String j:pNames) {
                    try {
                        File data = DataContext.getDir("DataToZip/" + j, Context.MODE_PRIVATE);
                        data.delete();
                    }
                    catch (Exception ex) {

                    }
                }
            }
            helper.DeleteRoom(n.Id);
        }
        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
    public List<Room> getRooms(){
        return helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
    }
    public void handleMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle("Alert...");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    private void handel(final Point p){
        final EditText txtUrl = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ENTER THE ROOM NAME");
        builder.setView(txtUrl);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String url = txtUrl.getText().toString();
                if(url.trim().length()>0){
                    RoomName name = new RoomName();
                    name.Text = url.trim().toUpperCase();
                    name.Value = url.trim().toUpperCase();
                    name.isCheck = false;
                    Room r = DoSelectItem(name);
                    mCustomView.startLayout(r, p);
                }
                else{
                    Toast.makeText(_LevelActivity, "No Room Name Entered!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(_LevelActivity, "No Room Name Entered!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    private void shiftTopToDown(int dir){
        ArrayList<ArrayList<Integer>> rowTotalArray = new ArrayList<ArrayList<Integer>>();
        int value = 1;
        for(int i =0; i <9; i++){
            ArrayList<Integer> rowArray = new ArrayList<Integer>();
            for(int j =0; j <9; j++){
                rowArray.add(value);
                value = value +1;
            }
            rowTotalArray.add(rowArray );
        }
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        ArrayList<Integer> all = new ArrayList<Integer>();
        for(Room n: list2){
            if(n.Connectors.trim().length()>0){
                String [] c= n.Connectors.trim().split(",");
                for(String c1:c) {
                    all.add(Integer.parseInt(c1.trim()));
                }
            }
        }
        if(dir ==1) {
            for (int i = 0; i < rowTotalArray.size(); i++) {
                HashSet<Integer> set1 = new HashSet<>(rowTotalArray.get(i));
                HashSet<Integer> set2 = new HashSet<>(all);
                set2.retainAll(set1);
                if (set2.size() > 0) {
                    if (i > 0) {
                        shiftUp(9);
                    }
                    break;
                }
            }
        }
        else if(dir ==2) {
            Collections.reverse(rowTotalArray);
            for (int i = 0; i < rowTotalArray.size(); i++) {
                HashSet<Integer> set1 = new HashSet<>(rowTotalArray.get(i));
                HashSet<Integer> set2 = new HashSet<>(all);
                set2.retainAll(set1);
                if (set2.size() > 0) {
                    if (i > 0) {
                        shiftDown(9);
                    }
                    break;
                }
            }
        }
    }
    private void  shiftSideToSide(int dir){
        ArrayList<ArrayList<Integer>> colTotalArray = new ArrayList<ArrayList<Integer>>();
        for(int i =1; i <10; i++){
            ArrayList<Integer> colArray = new ArrayList<Integer>();
            int value = 0;
            for(int j =0; j <9; j++){
                if(j==0){
                    colArray.add(i + value);
                }
                else{
                    colArray.add(i + value);
                }
                value = value + 9;
            }
            colTotalArray.add(colArray );
        }
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        ArrayList<Integer> all = new ArrayList<Integer>();
        for(Room n: list2){
            if(n.Connectors.trim().length()>0){
                String [] c= n.Connectors.trim().split(",");
                for(String c1:c) {
                    all.add(Integer.parseInt(c1.trim()));
                }
            }
        }
        if(dir ==1) {
            for (int i = 0; i < colTotalArray.size(); i++) {
                HashSet<Integer> set1 = new HashSet<>(colTotalArray.get(i));
                HashSet<Integer> set2 = new HashSet<>(all);
                set2.retainAll(set1);
                if (set2.size() > 0) {
                    if (i > 0) {
                        shiftLeft(1);
                    }
                    break;
                }
            }
        }
        else if(dir ==2) {
            Collections.reverse(colTotalArray);
            for (int i = 0; i < colTotalArray.size(); i++) {
                HashSet<Integer> set1 = new HashSet<>(colTotalArray.get(i));
                HashSet<Integer> set2 = new HashSet<>(all);
                set2.retainAll(set1);
                if (set2.size() > 0) {
                    if (i > 0) {
                        shiftRight(1);
                    }
                    break;
                }
            }
        }
    }
    private void shiftLeft(int col){
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        for(Room n: list2){
            if(n.Connectors.trim().length()>0){
                String [] c= n.Connectors.trim().split(",");
                int [] ids1= new int[c.length];
                int count =0;
                for(String c1:c) {
                   ids1[count] = (Integer.parseInt(c1.trim())) -col;
                   count = count + 1;
                }
                String ids2 = Arrays.toString(ids1);
                n.Connectors = (ids2.substring(1, ids2.indexOf("]"))).trim();
                n.CenterX = "" + (Integer.parseInt(n.CenterX) -  mCustomView.snappSize);
                helper.UpdateRoom(n);
            }
        }

        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void shiftRight(int col){
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        for(Room n: list2){
            if(n.Connectors.trim().length()>0){
                String [] c= n.Connectors.trim().split(",");
                int [] ids1= new int[c.length];
                int count =0;
                for(String c1:c) {
                    ids1[count] = (Integer.parseInt(c1.trim())) + col;
                    count = count + 1;
                }
                String ids2 = Arrays.toString(ids1);
                n.Connectors = (ids2.substring(1, ids2.indexOf("]"))).trim();
                n.CenterX = "" + (Integer.parseInt(n.CenterX) +  mCustomView.snappSize);
                helper.UpdateRoom(n);
            }
        }
        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void shiftUp(int row){
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        for(Room n: list2){
            if(n.Connectors.trim().length()>0){
                String [] c= n.Connectors.trim().split(",");
                int [] ids1= new int[c.length];
                int count =0;
                for(String c1:c) {
                    ids1[count] = (Integer.parseInt(c1.trim())) -row;
                    count = count + 1;
                }
                String ids2 = Arrays.toString(ids1);
                n.Connectors = (ids2.substring(1, ids2.indexOf("]"))).trim();
                n.CenterY = "" + (Integer.parseInt(n.CenterY) -  mCustomView.snappSize);
                helper.UpdateRoom(n);
            }
        }

        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void shiftDown(int row){
        List<Room> list2 =helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Level.ProjectId, helper.Level.LevelId);
        for(Room n: list2){
            if(n.Connectors.trim().length()>0){
                String [] c= n.Connectors.trim().split(",");
                int [] ids1= new int[c.length];
                int count =0;
                for(String c1:c) {
                    ids1[count] = (Integer.parseInt(c1.trim())) + row;
                    count = count + 1;
                }
                String ids2 = Arrays.toString(ids1);
                n.Connectors = (ids2.substring(1, ids2.indexOf("]"))).trim();
                n.CenterY = "" + (Integer.parseInt(n.CenterY) +  mCustomView.snappSize);
                helper.UpdateRoom(n);
            }
        }

        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
