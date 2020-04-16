package com.bosch.glm100c.easy_connect;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bosch.glm100c.easy_connect.data.*;
import java.util.List;
import java.util.regex.Pattern;

public class Login extends Activity {
    private DataService helper;
    private DataUpdate dataUpdate;
    private DataUpdate2 dataUpdate2;
    EditText password;
    EditText userName;
    public Button GoToLogin;
    public TextView connected;
    public boolean isConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dataUpdate = new DataUpdate();
        dataUpdate2 = new DataUpdate2();
        connected = findViewById(R.id.connected);
        GoToLogin = findViewById(R.id.GoToLogin);
        GoToLogin.setEnabled(false);
        internetIsConnected();
        ViewUpdate();

    }
    private void ViewUpdate(){
        helper = DataService.getInstance(this);


        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        password.addTextChangedListener(new TextValidator(password) {
            @Override public void validate(TextView textView, String text) {
                isValid();
            }
        });
        userName.addTextChangedListener(new TextValidator(userName) {
            @Override public void validate(TextView textView, String text) {
                isValid();
            }
        });

        List<Password> passwords = helper.GetAllPasswordsData();
        if(passwords.size()>0){
            userName.setText(passwords.get(0).Name);
            password.setText(passwords.get(0).Pass);
        }
        Button GoToInternet = findViewById(R.id.GoToInternet);
        GoToInternet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InternetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        GoToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helper.Name = userName.getText().toString();
                helper.Pass = password.getText().toString();
                List<Password> passwords = helper.GetAllPasswordsData();
                if(passwords.size()>0){
                   passwords.get(0).Name = helper.Name;
                   passwords.get(0).Pass = helper.Pass;
                   helper.UpdatePassword( passwords.get(0));
                }
                else{
                    Password p = new Password();
                    p.Name =helper.Name;
                    p.Pass = helper.Pass;
                    p.Id = -2;
                    helper.InsertPasswword(p);
                }
                Intent intent = new Intent(getApplicationContext(), DatabaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        return;
    }
    private void isValid(){
        String name = userName.getText().toString();
        String pass = password.getText().toString();
        if((isValid(name)==false ||isValid(name)==true)  && pass.length()<6){
            GoToLogin.setEnabled(false);
        }
        else {
            if(isConnected){
                GoToLogin.setEnabled(true);
            }
            else{
                GoToLogin.setEnabled(false);
            }
        }
    }
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public boolean internetIsConnected() {
        try  {
            String info = dataUpdate.execute("").get();
            if(info.trim().equals("Good")){
                isConnected = true;
                connected.setVisibility(View.INVISIBLE);
            }
            else{
                isConnected = false;
                connected.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), InternetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
