package com.bosch.glm100c.easy_connect.data;
import android.os.AsyncTask;

import com.bosch.glm100c.easy_connect.DataService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DataUpdate3 extends AsyncTask<String, Void, String> {
    private int errorCounter =0;
    public Data data;
    public int track = 0;
    public String inputFile = "";
    public File fileEvents;
    public ArrayList<File> files = new ArrayList<File>();
    @Override
    protected String doInBackground(String... strings) {
        if(files.size()> 0){
            File f = files.get(track);
            return uploadImage(f, f.getName(), "1");
        }
        else{
            return sendText(readFile());
        }
    }

    private String uploadImage(File sourceFile, String fileName, String counter){
        //String upLoadServerUri = "http://192.168.1.15:8085/Home/LoadPictures?file=" + inputFile + "&start=" + counter;
        String upLoadServerUri = "http://360floorplans.nvms.com/Home/LoadPictures?file=" + inputFile + "&start=" + counter;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"\\r\n");
            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            fileInputStream.close();
            dos.flush();
            dos.close();
            if(serverResponseCode == 200){
                track = track + 1;
                if(track <files.size() && errorCounter == 0){
                    File f = files.get(track);
                    uploadImage(f, f.getName(), "2");
                }
                else{
                    return sendText(readFile());
                }
            }
            else{
                errorCounter = 1;
               return "Server Error";
            }
        }
        catch (Exception e) {
            errorCounter = 1;
            return "Server Error";
        }
        return "Good";
    }

    private String readFile() {
        String result = "";
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            result = text.toString().trim();
        }
        catch (IOException e) {
            errorCounter = 2;
            return "File Name Not Found";
        }
        return result;
    }
    private String sendText(String info){
        if(info.trim().length() == 0 || info.trim().contains("File Name Not Found") ){
            errorCounter = 2;
            return  "File Name Not Found";
        }

        if(errorCounter == 1){
            return "Bad";
        }

        String outResult = "Good";
        String text = "";
        BufferedReader reader=null;
        try
        {
            //String upLoadServerUri = "http://192.168.1.15:8085/Home/LoadDBIOS?file=" + inputFile;
            String upLoadServerUri = "http://360floorplans.nvms.com/Home/LoadDBIOS?file=" + inputFile;
            URL url = new URL(upLoadServerUri);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( info );
            wr.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            text = sb.toString();
            validateJson(text);
            if(data.message.trim().equals("404")){
                outResult =  "404";
            }
            else if (data.message.trim().equals("Good")){
                outResult = "Good";
            }
            else if (data.message.trim().equals("File Name Not Found")){
                outResult =  "File Name Not Found";
            }
            outResult = data.message.trim();
        }
        catch(Exception ex){
            outResult = "File Name Not Found!";
        }
        finally {
            try {
                reader.close();
            }
            catch(Exception ex) {
            }
        }
        int k = 0;
        return outResult;
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
