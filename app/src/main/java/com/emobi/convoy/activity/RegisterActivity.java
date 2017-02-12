package com.emobi.convoy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.utility.ImageUtil;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,WebServicesCallBack{
    @BindView(R.id.et_first_name)
    EditText et_first_name;
    @BindView(R.id.et_last_name)
    EditText et_last_name;
    @BindView(R.id.et_mobile_number)
    EditText et_mobile_number;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirm_pass)
    EditText et_confirm_pass;

    @BindView(R.id.tv_entertainment)
    TextView tv_entertainment;
    @BindView(R.id.tv_travelling)
    TextView tv_travelling;
    @BindView(R.id.tv_family)
    TextView tv_family;
    @BindView(R.id.tv_study)
    TextView tv_study;
    @BindView(R.id.tv_gaming)
    TextView tv_gaming;
    @BindView(R.id.tv_evening)
    TextView tv_evening;
    @BindView(R.id.tv_sports)
    TextView tv_sports;
    @BindView(R.id.tv_technology)
    TextView tv_technology;
    @BindView(R.id.tv_action)
    TextView tv_action;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_clear)
    Button btn_clear;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final int FILE_SELECT_CODE = 0;
    String image_path_string="";
    boolean con_entertainment=false,con_travelling=false,con_family=false,con_study=false,
            con_gaming=false,con_eve=false,con_sports=false,con_technology=false,con_action=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Register");

        iv_profile.setOnClickListener(this);
        tv_entertainment.setOnClickListener(this);
        tv_travelling.setOnClickListener(this);
        tv_family.setOnClickListener(this);
        tv_study.setOnClickListener(this);
        tv_gaming.setOnClickListener(this);
        tv_evening.setOnClickListener(this);
        tv_sports.setOnClickListener(this);
        tv_technology.setOnClickListener(this);
        tv_action.setOnClickListener(this);

        btn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_clear:
                break;
            case R.id.btn_submit:
                submitRequest();
                break;
            case R.id.tv_entertainment:
                if(con_entertainment){
                    con_entertainment=false;
                    tv_entertainment.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_entertainment=true;
                    tv_entertainment.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_travelling:
                if(con_travelling){
                    con_travelling=false;
                    tv_travelling.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_travelling=true;
                    tv_travelling.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_family:
                if(con_family){
                    con_family=false;
                    tv_family.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_family=true;
                    tv_family.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_study:
                if(con_study){
                    con_study=false;
                    tv_study.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_entertainment=true;
                    tv_study.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_gaming:
                if(con_gaming){
                    con_gaming=false;
                    tv_gaming.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_gaming=true;
                    tv_gaming.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_evening:
                if(con_eve){
                    con_eve=false;
                    tv_evening.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_eve=true;
                    tv_evening.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_sports:
                if(con_sports){
                    con_sports=false;
                    tv_sports.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_sports=true;
                    tv_sports.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_technology:
                if(con_technology){
                    con_technology=false;
                    tv_technology.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_technology=true;
                    tv_technology.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.tv_action:
                if(con_action){
                    con_action=false;
                    tv_action.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    con_action=true;
                    tv_action.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case R.id.iv_profile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, FILE_SELECT_CODE);
                break;
        }
    }

    public void submitRequest(){
        String first_name=et_first_name.getText().toString();
        String last_name=et_last_name.getText().toString();
        String mobile_num=et_mobile_number.getText().toString();
        String email=et_email.getText().toString();
        String password=et_password.getText().toString();
        String confirm_pass=et_confirm_pass.getText().toString();

        if(first_name.equals("")||last_name.equals("")||mobile_num.equals("")||email.equals("")
                ||password.equals("")||confirm_pass.equals("")||image_path_string.equals("")) {
            Toast.makeText(getApplicationContext(),"Please Fill all Fields Properly",Toast.LENGTH_LONG).show();
        }
        else{
            if (password.equals(confirm_pass)) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("log_pics", ImageUtil.encodeTobase64(BitmapFactory.decodeFile(image_path_string))));
                nameValuePairs.add(new BasicNameValuePair("log_password", password));
                nameValuePairs.add(new BasicNameValuePair("log_name", first_name + " " + last_name));
                nameValuePairs.add(new BasicNameValuePair("log_email", email));
                nameValuePairs.add(new BasicNameValuePair("log_mob", mobile_num));
                nameValuePairs.add(new BasicNameValuePair("log_facbook", ""));
                nameValuePairs.add(new BasicNameValuePair("log_tag", ""));
                nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetStringPref(getApplicationContext(), Pref.FCM_REGISTRATION_TOKEN, "")));
                nameValuePairs.add(new BasicNameValuePair("log_entertainment", checkInterestCondition(con_entertainment, "entertainment")));
                nameValuePairs.add(new BasicNameValuePair("log_sports", checkInterestCondition(con_sports, "sports")));
                nameValuePairs.add(new BasicNameValuePair("log_travelling", checkInterestCondition(con_travelling, "travelling")));
                nameValuePairs.add(new BasicNameValuePair("log_study", checkInterestCondition(con_study, "study")));
                nameValuePairs.add(new BasicNameValuePair("log_gaming", checkInterestCondition(con_gaming, "gaming")));
                nameValuePairs.add(new BasicNameValuePair("log_technology", checkInterestCondition(con_technology, "technology")));
                nameValuePairs.add(new BasicNameValuePair("log_action", checkInterestCondition(con_action, "action")));
                nameValuePairs.add(new BasicNameValuePair("log_everything", checkInterestCondition(con_eve, "everything")));
                nameValuePairs.add(new BasicNameValuePair("log_family", checkInterestCondition(con_family, "family")));
                nameValuePairs.add(new BasicNameValuePair("log_ans1", ""));
                nameValuePairs.add(new BasicNameValuePair("log_ans2", ""));
                nameValuePairs.add(new BasicNameValuePair("log_ans3", ""));
                nameValuePairs.add(new BasicNameValuePair("log_ans4", ""));
                nameValuePairs.add(new BasicNameValuePair("log_ans5", ""));
                nameValuePairs.add(new BasicNameValuePair("log_pics", ""));
                new WebServiceBase(nameValuePairs, this, "register").execute(WebServicesUrls.REGISTER_URL);

            } else {
                Toast.makeText(getApplicationContext(), "Password do not Match", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String checkInterestCondition(boolean bol,String cond){
        return bol?cond:"";
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case "register":
                Log.d(TAG,response);
                parseRegisterJSON(response);
                break;
        }
    }
    private final String TAG=getClass().getName();

    public void parseRegisterJSON(String response){
        Gson gson=new Gson();
        RegisterPOJO pojo=gson.fromJson(response,RegisterPOJO.class);
        if(pojo!=null){
            try{
                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ID,pojo.getLog_id());
                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME,pojo.getLog_name());
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

                Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
            catch (Exception e){

            }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            Log.d("sun","on activity result");
            if (resultCode == Activity.RESULT_OK) {
                if (null == data)
                    return;
                Uri selectedImageUri = data.getData();
                System.out.println(selectedImageUri.toString());
                // MEDIA GALLERY
                String selectedImagePath = getPath(
                        RegisterActivity.this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if(selectedImagePath!=null&&selectedImagePath!=""){
                    image_path_string=selectedImagePath;
                    Log.d("sunil",selectedImagePath);
                    Bitmap bmImg = BitmapFactory.decodeFile(image_path_string);
                    iv_profile.setImageBitmap(bmImg);
//                    Preferences.setCardImagePath(getApplicationContext(),selectedImagePath);

//                    startActivity(new Intent(SelectImageActivity.this,CardActivity.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this,"File Selected is corrupted",Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path ="+selectedImagePath);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }
}
