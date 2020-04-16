package com.bosch.glm100c.easy_connect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bosch.glm100c.easy_connect.data.DataUpdate;
import com.bosch.glm100c.easy_connect.data.Room;
import com.bosch.glm100c.easy_connect.glview.GLPhotoView;
import com.bosch.glm100c.easy_connect.model.Photo;
import com.bosch.glm100c.easy_connect.model.RotateInertia;
import com.bosch.glm100c.easy_connect.network.HttpDownloadListener;
import com.bosch.glm100c.easy_connect.network.HttpConnector;
import com.bosch.glm100c.easy_connect.network.ImageData;
import com.bosch.glm100c.easy_connect.view.ConfigurationDialog;
import com.bosch.glm100c.easy_connect.view.LogView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class GLPhotoActivity extends Activity implements ConfigurationDialog.DialogBtnListener {
    private static final String CAMERA_IP_ADDRESS = "CAMERA_IP_ADDRESS";
    private static final String OBJECT_ID = "OBJECT_ID";
    private static final String THUMBNAIL = "THUMBNAIL";
    private GLPhotoView mGLPhotoView;
    private Photo mTexture = null;
    private LoadPhotoTask mLoadPhotoTask = null;
    private RotateInertia mRotateInertia = RotateInertia.INERTIA_0;
    public static final int REQUEST_REFRESH_LIST = 100;
    public static final int REQUEST_NOT_REFRESH_LIST = 101;
    public String roomName = "";
    public Context DataContext;
    private DataService helper;
    private GLPhotoActivity _GLPhotoActivity;
    private Button GoToFloorLayout;
    private Button BackToImageList;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glphoto);
        DataContext = this.getApplicationContext();
        helper = DataService.getInstance(this);

        String title = helper.Project.Address + ", " + helper.Project.City + ", " + helper.Project.State;
        getActionBar().setTitle(title);
        TextView info = findViewById(R.id.levelRoomInfo);
        info.setText("Level: " + helper.Room.LevelName + ",  Room: " + helper.Room.Name);
        _GLPhotoActivity = this;
        getName();
        //cleanData();
        Intent intent = getIntent();
        String cameraIpAddress = intent.getStringExtra(CAMERA_IP_ADDRESS);
        String fileId = intent.getStringExtra(OBJECT_ID);
        try {
            byte[] byteThumbnail = intent.getByteArrayExtra(THUMBNAIL);
           // ByteArrayInputStream inputStreamThumbnail = new ByteArrayInputStream(byteThumbnail);
            // Drawable thumbnail = BitmapDrawable.createFromStream(inputStreamThumbnail, null);
            //Photo _thumbnail = new Photo(((BitmapDrawable) thumbnail).getBitmap());

            Bitmap bmp = BitmapFactory.decodeByteArray( byteThumbnail, 0,  byteThumbnail.length);
            Photo _thumbnail = new Photo(bmp);
            GoToFloorLayout = findViewById(R.id.GoToFloorLayout);
            GoToFloorLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    navigateToLevelActivity();
                }
            });

            GoToFloorLayout.setEnabled(false);
            BackToImageList = findViewById(R.id.BackToImageList);
            BackToImageList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            BackToImageList.setEnabled(false);
            mGLPhotoView = findViewById(R.id.photo_image2);
            mGLPhotoView.setTexture(_thumbnail);
            mGLPhotoView.setmRotateInertia(mRotateInertia);
            mLoadPhotoTask = new LoadPhotoTask(cameraIpAddress, fileId);
            mLoadPhotoTask.execute();
        }
        catch(Exception ex){
            _GLPhotoActivity.handleOutOfMemory();
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
    private void getName(){
        Room Item = helper.Room;
        String k = Item.PictureName.trim();
        if(k.equals("")){
            String name = Item.Name;
            name = name.replaceAll(" ", "");
            name = name + "_Pro_" + Item.ProjectId + "_Lev_" + Item.LevelId + "_Pic_" + 1;
            roomName = name + ".jpg";
            return;
        }
        else{
            String [] names = Item.PictureName.split(",");
            String name = Item.Name;
            name = name.replaceAll(" ", "");
            name = name + "_Pro_" + Item.ProjectId + "_Lev_" + Item.LevelId + "_Pic_" + (names.length + 1);
            roomName = name + ".jpg";
            return;
        }
    }
    private void cleanData(){
        try {
            File dataToZipFolder = DataContext.getDir("DataToZip", Context.MODE_PRIVATE);
            File file = new File(dataToZipFolder, roomName);
            if(file.exists()){
                file.delete();
            }
        }
        catch (Exception ex){
        }
    }
    @Override
    protected void onDestroy() {
        if (mTexture != null) {
            mTexture.getPhoto().recycle();
        }
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask.cancel(true);
        }
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.configuration, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.configuration:
                FragmentManager mgr = getFragmentManager();
                ConfigurationDialog.show(mgr, mRotateInertia);
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGLPhotoView.onResume();
        if (null != mTexture) {
            if (null != mGLPhotoView) {
                mGLPhotoView.setTexture(mTexture);
            }
        }
    }
    @Override
    protected void onPause() {
        this.mGLPhotoView.onPause();
        super.onPause();
    }
    public void navigateToLevelActivity(){
        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void turnOn(){
        GoToFloorLayout.setEnabled(true);
        BackToImageList.setEnabled(true);
    }
    @Override
    public void onDialogCommitClick(RotateInertia inertia) {
        mRotateInertia = inertia;
        if (null != mGLPhotoView) {
            mGLPhotoView.setmRotateInertia(mRotateInertia);
        }
    }
    private  void handleOutOfMemory() {
        runOnUiThread(new Runnable() {
            public void run() {

            }
        });
    }
    private class LoadPhotoTask extends AsyncTask<Void, Object, ImageData> {
        private LogView logViewer;
        private ProgressBar progressBar;
        private String cameraIpAddress;
        private String fileId;
        private long fileSize;
        private long receivedDataSize = 0;
        private AlertDialog dialogBuilder;
        public LoadPhotoTask(String cameraIpAddress, String fileId) {
            this.logViewer = findViewById(R.id.photo_info);
            this.progressBar = findViewById(R.id.loading_photo_progress_bar);
            this.cameraIpAddress = cameraIpAddress;
            this.fileId = fileId;
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(_GLPhotoActivity);
            builder.setTitle("Image Download");
            builder.setMessage("Please wait while we download the image from the camera!");
            dialogBuilder = builder.create();
            dialogBuilder.show();
            colorAlertDialogTitle(dialogBuilder);
        }
        @Override
        protected ImageData doInBackground(Void... params) {
            try {
                publishProgress("start to download image" + fileId);
                HttpConnector camera = new HttpConnector(cameraIpAddress);
                ImageData resizedImageData = camera.getImage(fileId, new HttpDownloadListener() {
                    @Override
                    public void onTotalSize(long totalSize)
                    {
                        fileSize = totalSize;
                    }
                    @Override
                    public void onDataReceived(int size) {
                        receivedDataSize += size;
                        if (fileSize != 0) {
                            int progressPercentage = (int) (receivedDataSize * 100 / fileSize);
                            publishProgress(progressPercentage);
                        }
                    }
                });
                publishProgress("finish to download");
                return resizedImageData;
            }
            catch (Throwable throwable) {
                String errorLog = Log.getStackTraceString(throwable);
                publishProgress(errorLog);
                return null;
            }
        }
        @Override
        protected void onProgressUpdate(Object... values) {
            for (Object param : values) {
                if (param instanceof Integer) {
                    progressBar.setProgress((Integer) param);
                }
                else if (param instanceof String) {
                    logViewer.append((String) param);
                }
            }
        }
        @Override
        protected void onPostExecute(ImageData imageData) {
            try {
                if (imageData != null) {
                    byte[] dataObject = imageData.getRawData();
                    if (dataObject == null) {
                        logViewer.append("failed to download image");
                        dialogBuilder.dismiss();
                        showErrorMessage();
                        return;
                    }
                    insertInPrivateStorage(dataObject);
                    //checkFileList();
                    Bitmap __bitmap = BitmapFactory.decodeByteArray(dataObject, 0, dataObject.length);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Saving Room Pic!", Toast.LENGTH_SHORT).show();
                    Double yaw = imageData.getYaw();
                    Double pitch = imageData.getPitch();
                    Double roll = imageData.getRoll();
                    logViewer.append("<Angle: yaw=" + yaw + ", pitch=" + pitch + ", roll=" + roll + ">");
                    mTexture = new Photo(__bitmap, yaw, pitch, roll);
                    //__bitmap.recycle();
                    if (null != mGLPhotoView) {
                        mGLPhotoView.setTexture(mTexture);
                    }
                    _GLPhotoActivity.turnOn();
                    dialogBuilder.dismiss();
                }
                else {
                    logViewer.append("failed to download image");
                    dialogBuilder.dismiss();
                    showErrorMessage();
                }

            }
            catch (Exception ex){
                dialogBuilder.dismiss();
                _GLPhotoActivity.handleOutOfMemory();
                dialogBuilder.dismiss();
                showErrorMessage();
            }
        }
        private void showErrorMessage(){
            AlertDialog.Builder builder = new AlertDialog.Builder(_GLPhotoActivity);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(_GLPhotoActivity.getApplicationContext(), ImageListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setTitle("Download Fails");
            builder.setMessage("The image download fails, check the camera pairing and try again!");
            AlertDialog dialog = builder.create();
            dialog.show();
            colorAlertDialogTitle(dialog);
        }
        private void insertInPrivateStorage(byte[]bytes){
            try {
                File dataToZipFolder = DataContext.getDir("DataToZip", Context.MODE_PRIVATE);
                File file1 = new File(dataToZipFolder, roomName);
                FileOutputStream fos = new FileOutputStream(file1);
                fos.write(bytes);
                fos.close();
                if(helper.Room.PictureName.trim().length()<1){
                    helper.Room.PictureName= roomName;
                }
                else{
                   helper.Room.PictureName = helper.Room.PictureName + ", " + roomName;
                }
                helper.UpdateRoom(helper.Room);
                helper.Room = helper.GetRoomDataByProjectIdAndLevelIdAndRoomName(helper.Room.ProjectId, helper.Room.LevelId, helper.Room.Name);
                checkFileList();
            }
            catch (Exception ex){
            }
        }
        private void checkFileList(){
              File dataToZipFolder = DataContext.getDir("DataToZip", Context.MODE_PRIVATE);
              File[] k = dataToZipFolder.listFiles();
              int k2 =0;
        }
    }
    public static void startActivityForResult(Activity activity, String cameraIpAddress, String fileId, byte[] thumbnail, boolean refreshAfterClose) {
        int requestCode;
        if (refreshAfterClose) {
            requestCode = REQUEST_REFRESH_LIST;
        }
        else {
            requestCode = REQUEST_NOT_REFRESH_LIST;
        }
        Intent intent = new Intent(activity, GLPhotoActivity.class);
        intent.putExtra(CAMERA_IP_ADDRESS, cameraIpAddress);
        intent.putExtra(OBJECT_ID, fileId);
        intent.putExtra(THUMBNAIL, thumbnail);
        activity.startActivityForResult(intent, requestCode);
        activity.finish();
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