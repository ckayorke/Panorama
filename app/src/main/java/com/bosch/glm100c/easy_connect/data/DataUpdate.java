package com.bosch.glm100c.easy_connect.data;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataUpdate extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        return checkOnline();
    }
    private String checkOnline(){
        HttpURLConnection httpUrlConn;
        try {
            httpUrlConn =  (HttpURLConnection) new URL("https://www.google.com/").openConnection();
            httpUrlConn.setRequestMethod("HEAD");
            httpUrlConn.setConnectTimeout(30000);
            httpUrlConn.setReadTimeout(30000);
            if(httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                httpUrlConn.disconnect();
                return "Good";
            }
            else{
                httpUrlConn.disconnect();
                return "bad";
            }
        }
        catch (Exception e) {
            return "Bad";
        }
    }
}
