package com.emobi.convoy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpEmailActivity extends AppCompatActivity implements View.OnClickListener,WebServicesCallBack{
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                loginMethod();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }

    public void loginMethod(){
        String email=et_email.getText().toString();
        String password=et_password.getText().toString();
        if(email.equals("")||password.equals("")){
            Toast.makeText(getApplicationContext(), "Please Fill All Box Properly", Toast.LENGTH_SHORT).show();
        }
        else{
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("log_email", email));
            nameValuePairs.add(new BasicNameValuePair("log_password", password));
            nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetStringPref(getApplicationContext(),Pref.FCM_REGISTRATION_TOKEN,"")));
            new WebServiceBase(nameValuePairs, this, "login").execute(WebServicesUrls.LOGIN_URL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case "login":
                parseLoginData(response);
                break;
        }
    }
    private final String TAG=getClass().getName();
    public void parseLoginData(String response){
        Log.d(TAG,"login Response:-"+response);
        Gson gson=new Gson();
        RegisterPOJO pojo=gson.fromJson(response,RegisterPOJO.class);
        if(pojo!=null){
            try{
                if(pojo.getMessage().equals("Invalid Email Or Password")){
                    Toast.makeText(getApplicationContext(),"Invalid Email Or Password",Toast.LENGTH_LONG).show();
                }
                else{

                }
            }
            catch (Exception e){
                try{
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ID,pojo.getLog_id());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME,pojo.getLog_name());
                    Log.d(TAG,"name:-"+pojo.getLog_name());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL,pojo.getLog_email());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD,pojo.getLog_password());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE,pojo.getLog_mob());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK,pojo.getLog_facbook());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TAG,pojo.getLog_tag());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LAST_LOGIN,pojo.getLog_last_login());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_CREATED,pojo.getLog_created());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PICS,"http://pornvideocum.com/user/"+pojo.getLog_pics());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN,pojo.getLog_device_token());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ENTERTAINMENT,pojo.getLog_entertainment());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_SPORTS,pojo.getLog_sports());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TRAVELLING,pojo.getLog_travelling());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STUDY,pojo.getLog_study());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GAMING,pojo.getLog_gaming());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TECHNOLOGY,pojo.getLog_technology());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ACTION,pojo.getLog_action());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EVERYTHING,pojo.getLog_everything());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FAMILY,pojo.getLog_family());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS1,pojo.getLog_ans1());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS2,pojo.getLog_ans2());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS3,pojo.getLog_ans3());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS4,pojo.getLog_ans4());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS5,pojo.getLog_ans5());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MESSAGE,pojo.getLog_message());
                    Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN,true);



                    Intent intent=new Intent(SignUpEmailActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                catch (Exception e1){

                }


            }
        }
    }
}
