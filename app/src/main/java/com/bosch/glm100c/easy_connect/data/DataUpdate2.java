package com.bosch.glm100c.easy_connect.data;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.bosch.glm100c.easy_connect.DataService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class DataUpdate2 extends AsyncTask<String, Void, String> {
    public File urlParameter = null;
    public Data data;
    @Override
    protected String doInBackground(String... strings) {
        return UpdateDataBase();
    }
    private String UpdateDataBase(){
        try {
            int kj= loadData();
            if(kj==1){
                return "404";
            }
            if(kj==2){
                return "server connection";
            }
            String url = "http://360floorplans.nvms.com/Home/DBSync";
            //String url = "http://192.168.1.15:8085/Home/DBSync";
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, DataService.dataName);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new IOException("Error : "+response);
            }
            String s = response.body().string();
            validateJson(s);
            if(data.message.trim().equals("404")){
                return "404";
            }
            else if (data.message.trim().equals("Good")){
                return "Good";
            }
            else if (data.message.trim().equals("File Name Not Found")){
                return "File Name Not Found";
            }
            return data.message.trim();
        }
        catch (Exception ex){
            data = new Data();
            data.levels = new ArrayList<Level>();
            data.projects = new ArrayList<Project>();
            data.rooms = new ArrayList<Room>();
            return ex.getMessage();
        }
    }

    private int loadData() {
        try{
            String user ="cyork";
            String password = "Gy8+RVaQ";
            FTPClient ftpClient = new FTPClient();
            ftpClient.setConnectTimeout(5000);
            ftpClient.connect("199.36.136.26", 54812);
            ftpClient.login(user, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlKeepAliveTimeout(300);
            FTPFile[] names = ftpClient.listFiles();
            for (FTPFile name : names) {
               if(name.getName().equals(DataService.dataName)){
                   ftpClient.deleteFile(name.getName());
               }
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(urlParameter));
            ftpClient.storeFile(DataService.dataName, buffIn);
            buffIn.close();
            ftpClient.logout();
            ftpClient.disconnect();
            return 0;
        }
        catch (Exception ex){
            return 2;
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    private void validateJson(String s){
        try {
            JSONObject jsonObj = new JSONObject(s);
            Gson gson = new Gson();
            data = gson.fromJson(String.valueOf(jsonObj), Data.class);
        }
        catch (JSONException e) {
            String out = e.getMessage();
            out = out.substring(out.indexOf('{'), out.lastIndexOf('}') +1);
            try {
                JSONObject jsonObj = new JSONObject(out);
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(jsonObj), Data.class);
            }
            catch (JSONException e1) {
                data = new Data();
                data.levels = new ArrayList<Level>();
                data.projects = new ArrayList<Project>();
                data.rooms = new ArrayList<Room>();
            }
        }
    }
}
