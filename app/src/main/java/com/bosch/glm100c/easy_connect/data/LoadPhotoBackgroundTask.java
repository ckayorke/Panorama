package com.bosch.glm100c.easy_connect.data;
import android.content.Context;
import android.os.AsyncTask;

import com.bosch.glm100c.easy_connect.ImageListActivity;
import com.bosch.glm100c.easy_connect.network.HttpConnector;
import com.bosch.glm100c.easy_connect.network.HttpDownloadListener;
import com.bosch.glm100c.easy_connect.network.ImageData;

import java.io.File;
import java.io.FileOutputStream;

public class LoadPhotoBackgroundTask extends AsyncTask<String, Void, String> {
    String cameraIpAddress = "192.168.1.1";
    private String fileId;
    private String roomName;
    private long fileSize;
    private long receivedDataSize = 0;
    private ImageListActivity DataContext;
    public LoadPhotoBackgroundTask(String fileId, ImageListActivity DataContext) {
        this.fileId = fileId;
        this.roomName = DataContext.getName();;
        this.DataContext = DataContext;
        this.DataContext.helper.isProcessing =1;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            HttpConnector camera = new HttpConnector(cameraIpAddress);
            camera.setImageSize(this.DataContext.currentImageSize);
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
                    }
                }
            });
            if (resizedImageData != null) {
                byte[] dataObject = resizedImageData.getRawData();
                if (dataObject == null) {
                    return "Bad Download";
                }
                else{
                    insertInPrivateStorage(dataObject);
                    checkFileList();
                }
            }
            else{
                return "Bad Download";
            }
            return "Good Download";
        }
        catch (Throwable throwable) {
            return "Bad Download";
        }
    }
    private void insertInPrivateStorage(byte[]bytes){
        try {
            File dataToZipFolder = DataContext.getDir("DataToZip", Context.MODE_PRIVATE);
            File file1 = new File(dataToZipFolder, roomName);
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(bytes);
            fos.close();

            if(DataContext.helper.toProjects==1 || DataContext.helper.toProjects==2){
                String u = DataContext.helper.Project.Outside3DPictures.trim();

                if(DataContext.helper.Project.Outside3DPictures.trim().length()<1){
                    DataContext.helper.Project.Outside3DPictures= roomName;
                }
                else{
                    DataContext.helper.Project.Outside3DPictures = DataContext.helper.Project.Outside3DPictures + ", " + roomName;
                }
                DataContext.helper.UpdateProject(DataContext.helper.Project);
                DataContext.helper.Project = DataContext.helper.GetProjectData(DataContext.helper.Project.ProjectId);
                this.DataContext.helper.isProcessing =0;
            }
            else{
                if(DataContext.helper.Room.PictureName.trim().length()<1){
                    DataContext.helper.Room.PictureName= roomName;
                }
                else{
                    DataContext.helper.Room.PictureName = DataContext.helper.Room.PictureName + ", " + roomName;
                }
                DataContext.helper.UpdateRoom(DataContext.helper.Room);
                DataContext.helper.Room = DataContext.helper.GetRoomDataByProjectIdAndLevelIdAndRoomName(DataContext.helper.Room.ProjectId, DataContext.helper.Room.LevelId, DataContext.helper.Room.Name);
                this.DataContext.helper.isProcessing =0;
            }
            // checkFileList();
        }
        catch (Exception ex){
            int k  =0;
        }
    }
    private void checkFileList(){
        File dataToZipFolder = DataContext.getDir("DataToZip", Context.MODE_PRIVATE);
        File[] k = dataToZipFolder.listFiles();
        int k2 =0;
    }
}
