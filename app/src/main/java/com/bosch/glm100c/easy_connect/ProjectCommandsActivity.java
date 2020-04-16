package com.bosch.glm100c.easy_connect;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bosch.glm100c.easy_connect.data.Level;
import com.bosch.glm100c.easy_connect.data.Project;
import com.bosch.glm100c.easy_connect.data.ProjectError;
import com.bosch.glm100c.easy_connect.data.Room;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectCommandsActivity extends Activity {
    private ProjectCommandsActivity  DataContext2;
    private DataService helper;
    private Button AddBasement;
    private Button AddFirstFloor;
    private Button AddSecondFloor;
    private Button AddThirdFloor;
    private Button AddFourthFloor;
    private ListView listView;
    private ArrayList<String> levels = new ArrayList<String>();
    private ArrayList<String> filtered = new ArrayList<String>();
    private ArrayAdapter adapter;
    //==================================================================
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    private static String imageStoragePath;
    private Button Surrounding;
    private Button checkForCompleteness;
    private Context DataContext;
    private ArrayList<String> pictureNames = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_commands);
        DataContext2 = this;
        DataContext = this.getApplicationContext();
        helper = DataService.getInstance(this);
        String title = helper.Project.Address + ", " + helper.Project.City + ", " + helper.Project.State;
        getActionBar().setTitle(title);
        listView= findViewById(R.id.LevelList);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.label, filtered);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name= ((TextView) view).getText().toString().trim();
                List<Level> list =helper.GetAllLevelsData(helper.Project.ProjectId);
                for(Level l: list){
                    if(l.Name.trim().equals(name)){
                        helper.Level = l;
                        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String name= ((TextView) view).getText().toString().trim();
                List<Level> list =helper.GetAllLevelsData(helper.Project.ProjectId);
                Level j = new Level();
                for(Level l: list){
                    if(l.Name.trim().equals(name)){
                        j = l;
                        break;
                    }
                }
                showLevelDeleteDialog(j);
                return true;
            }
        });
        AddBasement = findViewById(R.id.AddBasement);
        AddBasement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateFloor("BASEMENT", 1);
            }
        });

        AddFirstFloor = findViewById(R.id.AddFirstFloor);
        AddFirstFloor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateFloor("FIRST FLOOR", 2);
            }
        });

        AddSecondFloor = findViewById(R.id.AddSecondFloor);
        AddSecondFloor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateFloor("SECOND FLOOR", 3);
            }
        });

        AddThirdFloor = findViewById(R.id.AddThirdFloor);
        AddThirdFloor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateFloor("THIRD FLOOR", 4);
            }
        });

        AddFourthFloor = findViewById(R.id.AddFourthFloor);
        AddFourthFloor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateFloor("FOURTH FLOOR", 5);
            }
        });

        checkForCompleteness = (Button) findViewById(R.id.checkForCompleteness);
        checkForCompleteness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                projectstate();
            }
        });

        Button GoToProjects = findViewById(R.id.GoToProjects);
        GoToProjects.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(helper.Project.Status==0){
                    Intent intent = new Intent(getApplicationContext(), OpenProjectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                if(helper.Project.Status==2){
                    Intent intent = new Intent(getApplicationContext(), NeedInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        });

      //  if(helper.Project.Status==0){
          //  GoToProjects.setText("Back");
       // }
       // if(helper.Project.Status==2){
         //   GoToProjects.setText("Back");
        //}

        updateUI();

        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // finish();
        }
        Surrounding = findViewById(R.id.Surrounding);
        Surrounding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                }
                else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });

       Button Entrance = findViewById(R.id.Entrance);
        Entrance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigateToTakingPicture();
            }
        });

        restoreFromBundle(savedInstanceState);
    }
    private void CreateFloor(String name, int levId){
        Level exLevel = helper.GetLevelDataByProjectIdAndLevelName(helper.Project.ProjectId, name); // TODO
        if(exLevel.Id != -20 )
        {
            return;
        }
        int id = helper.GetLevelCount() + 1;
        helper.SetLevelCount(id);
        Level level = new Level();
        level.Id= -2;
        level.LevelId = id;
        level.ProjectId = helper.GetProject().ProjectId;
        level.Name = name.trim().toUpperCase();
        level.Status = 1;
        level.Status2 = "Created";
        level.PicName = "";
        helper.InsertLevel(level);
        updateUI();
    }
    private void updateUI(){
        levels.clear();
        filtered.clear();
        List<Level> list =helper.GetAllLevelsData(helper.Project.ProjectId);
        for(Level l: list){
            if(l.Name.trim().equals("BASEMENT")){
                AddBasement.setVisibility(View.GONE);
            }
            else if(l.Name.trim().equals("FIRST FLOOR")){
                AddFirstFloor.setVisibility(View.GONE);
            }
            else if(l.Name.trim().equals("SECOND FLOOR")){
                AddSecondFloor.setVisibility(View.GONE);
            }
            else if(l.Name.trim().equals("THIRD FLOOR")){
                AddThirdFloor.setVisibility(View.GONE);
            }
            else if(l.Name.trim().equals("FOURTH FLOOR")){
                AddFourthFloor.setVisibility(View.GONE);
            }
            levels.add(l.Name.trim());
        }
        for(String s: levels){
            filtered.add(s);
        }
        adapter.notifyDataSetChanged();
    }
    //Restoring store image path from saved instance state
    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }
    // Requesting permissions using Dexter library
    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                                  @Override
                                  public void onPermissionsChecked(MultiplePermissionsReport report) {
                                      if (report.areAllPermissionsGranted()) {

                                          if (type == MEDIA_TYPE_IMAGE) {
                                              // capture picture
                                              captureImage();
                                          }

                                      } else if (report.isAnyPermissionPermanentlyDenied()) {
                                          showPermissionsAlert();
                                      }
                                  }
                                  @Override
                                  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                      token.continuePermissionRequest();
                                  }
                              }
                ).check();
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    //Saving stored image path to saved instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                previewCapturedImage();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }
    private void previewCapturedImage() {
        try {
            Bitmap bitmap = rotateImage(CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath),90);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            insertInPrivateStorage(byteArray);
            Toast.makeText(getApplicationContext(), "Picture Saved!", Toast.LENGTH_SHORT).show();
            stream.close();
            //imgPreview.setImageBitmap(bitmap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getOutsideImageNumber(){
        String ms = helper.Project.OutsidePictures;
        if(helper.Project.Status==0) {
            if (ms.trim().length() == 0) {
                return "Proj_" + helper.Project.ProjectId + "_Out_11.jpg";
            }
            String[] msArray = ms.split(",");
            pictureNames.clear();
            for (String n : msArray) {
                pictureNames.add(n.trim());
            }
            String name = "Proj_" + helper.Project.ProjectId + "_Out_1" + (msArray.length + 1) + ".jpg";
            return name.trim();
        }
        else{
            if (ms.trim().length() == 0) {
                return "Proj_" + helper.Project.ProjectId + "_Out_21.jpg";
            }
            String[] msArray = ms.split(",");
            pictureNames.clear();
            for (String n : msArray) {
                pictureNames.add(n.trim());
            }
            String name = "Proj_" + helper.Project.ProjectId + "_Out_2" + (msArray.length + 1) + ".jpg";
            return name.trim();
        }
    }
    private void insertInPrivateStorage(byte[]bytes){
        try {
            String name = getOutsideImageNumber();
            File dataToZipFolder = DataContext.getDir("DataToZip", Context.MODE_PRIVATE);
            File file1 = new File(dataToZipFolder, name);
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(bytes);
            fos.close();
            pictureNames.add(name);
            helper.Project.OutsidePictures = Arrays.toString(pictureNames.toArray());
            helper.Project.OutsidePictures  = helper.Project.OutsidePictures.trim();
            helper.Project.OutsidePictures = helper.Project.OutsidePictures.substring(1, helper.Project.OutsidePictures.indexOf("]") );
            helper.UpdateProject(helper.Project);
        }
        catch (Exception ex){
        }
    }
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!");
        builder.setMessage("Camera needs few permissions to work properly. Grant them in settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CameraUtils.openSettings(ProjectCommandsActivity.this);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    private void projectstate() {
        Project p = helper.Project;
        ProjectError pError = helper.projectCompleted(p);
        if((pError.ReturnType == 3) && (p.Completed.equals("Yes"))){
            AlertDialog.Builder builder = new AlertDialog.Builder(DataContext2);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setTitle("PROJECT IS COMPLETE");
            AlertDialog dialog = builder.create();
            dialog.show();
            colorAlertDialogTitle(dialog);
        }
        else if(pError.ReturnType == 3){
            pError.MissingCheck = "Yes";
            pError.Address = p.Address;
            pError.City = p.City  + ", " + p.State + ", " + p.ZIPCode;
            showDialog(pError);
        }
        else if(pError.ReturnType == 2){
            showDialog(pError);
        }
        else{
            String msg = "Project is Empty.";
            AlertDialog.Builder builder = new AlertDialog.Builder(DataContext2);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setMessage(msg);
            builder.setTitle("PROJECT COMPLETION CHECK");
            AlertDialog dialog = builder.create();
            dialog.show();
            colorAlertDialogTitle(dialog);
        }
    }
    public void showDialog(ProjectError pError){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_missinginfo);
        Button btndialog = dialog.findViewById(R.id.btndialog1);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Projects Missing Information");
        TextView error_Address = dialog.findViewById(R.id.error_Address1);
        error_Address.setText("Address: " +pError.Address);
        TextView error_City = dialog.findViewById(R.id.error_City1);
        error_City.setText("City: " +pError.City);

        TextView error_BK = dialog.findViewById(R.id.error_BK1);
        TextView error_BK2 = dialog.findViewById(R.id.error_BK);
        String s4 = pError.BK.trim();
        if(s4.length()>0){
            error_BK.setText(pError.BK.toLowerCase().trim());
        }
        else{
            error_BK.setVisibility(View.GONE);
            error_BK2.setVisibility(View.GONE);
        }

        TextView error_Levels = dialog.findViewById(R.id.error_Levels1);
        TextView error_Levels2 = dialog.findViewById(R.id.error_Levels);
        String s1 = pError.EmptyLevels.trim();
        if(s1.length()>0) {
            error_Levels2.setText(pError.EmptyLevels.toLowerCase().trim());
        }
        else{
            error_Levels.setVisibility(View.GONE);
            error_Levels2.setVisibility(View.GONE);
        }

        String s2 = pError.MissingPicture.trim();
        TextView error_Pictures = dialog.findViewById(R.id.error_Pictures1);
        TextView error_Pictures2 = dialog.findViewById(R.id.error_Pictures);
        if(s2.length()>0) {
            error_Pictures.setText(pError.MissingPicture.toLowerCase().trim());
        }
        else{
            error_Pictures2.setVisibility(View.GONE);
            error_Pictures.setVisibility(View.GONE);
        }




        TextView error_Pic = dialog.findViewById(R.id.error_Out3DPics);
        TextView error_Pic2 = dialog.findViewById(R.id.error_Out3DPics1);
        error_Pic2.setText(pError.MissingOutside3DPics.trim());
        if((pError.MissingOutside3DPics.trim()).equals("No")) {
            error_Pic2.setVisibility(View.GONE);
            error_Pic.setVisibility(View.GONE);
        }

        TextView error_OutPics = dialog.findViewById(R.id.error_OutPics1);
        TextView error_OutPics2 = dialog.findViewById(R.id.error_OutPics);
        error_OutPics.setText(pError.MissingOutsidePics);
        if((pError.MissingOutsidePics.trim()).equals("No")){
            error_OutPics.setVisibility(View.GONE);
            error_OutPics2.setVisibility(View.GONE);
        }

        TextView error_Measure = dialog.findViewById(R.id.error_Measure1);
        TextView error_Measure2 = dialog.findViewById(R.id.error_Measure);
        String s3 = pError.MissingMeasure.trim();
        if(s3.length()>0) {
            error_Measure.setText(pError.MissingMeasure.toLowerCase().trim());
        }
        else{
            error_Measure.setVisibility(View.GONE);
            error_Measure2.setVisibility(View.GONE);
        }







        TextView error_Check = dialog.findViewById(R.id.error_Check1);
        TextView error_Check2 = dialog.findViewById(R.id.error_Check);
        error_Check.setText(pError.MissingCheck);
        if((pError.MissingCheck.trim()).equals("No")){
            error_Check.setVisibility(View.GONE);
            error_Check2.setVisibility(View.GONE);
        }
        dialog.show();
    }
    private void showLevelDeleteDialog(final Level lev){
        AlertDialog.Builder builder = new AlertDialog.Builder(DataContext2);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Room> rooms = helper.GetAllRoomsDataByProjectIdAndLevelId(helper.Project.ProjectId, lev.LevelId);
                for(Room r:rooms){
                    helper.DeleteRoom(r.Id);
                }
                String name = "_Pro_" + helper.Project.ProjectId + "_Lev_" + lev.LevelId + "_Pic_";
                deleteLevelInfo(name);
                String levName = lev.Name;
                String _levelName = levName.replaceAll(" ", "");
                name = "Pro_" + helper.Project.ProjectId + "_" + _levelName;
                deleteLevelInfo(name);
                helper.DeleteLevel(lev.Id);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), ProjectCommandsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setTitle("DELETE CONFIRMATION");
        builder.setMessage("Are you sure you want to delete this level?");
        AlertDialog dialog = builder.create();
        dialog.show();
        colorAlertDialogTitle(dialog);
    }
    public  int deleteLevelInfo(String name){
        File dir = DataContext2.getApplicationContext().getDir("DataToZip", Context.MODE_PRIVATE);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                try{
                    if(children[i].trim().contains(name)){
                        new File(dir, children[i]).delete();
                    }
                }
                catch (Exception z){
                }
            }
        }
        return 0;
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
    private void navigateToTakingPicture(){
        if(helper.isProcessing ==1){
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(helper.isProcessing ==1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DataContext2, "Please wait while we ready the camera!", Toast.LENGTH_LONG).show();
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
                helper.toProjects = 2;
                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else{
                helper.toProjects =1;
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        else{
            helper.toProjects =1;
            Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
