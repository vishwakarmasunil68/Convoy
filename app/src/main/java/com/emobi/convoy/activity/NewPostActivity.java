package com.emobi.convoy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.NewsPostPOJO;
import com.emobi.convoy.utility.GPSTracker;
import com.emobi.convoy.utility.ImageUtil;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener,WebServicesCallBack{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_camera)
    ImageView iv_camera;
    @BindView(R.id.iv_smiley)
    ImageView iv_smiley;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.iv_selected_image)
    ImageView iv_selected_image;
    @BindView(R.id.iv_category_post)
    ImageView iv_category_post;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.et_msg)
    TextView et_msg;

    private static final int FILE_SELECT_CODE = 0;
    private static final int CAMERA_REQUEST = 1888;

    private String image_path_string="";
    private String pictureImagePath = "";

    private final String API_REQUEST_CALL="newspost";
    GPSTracker gps;

    private final static String CAT_PUBLIC="public";
    private final static String CAT_FOLLOWERS="followers";
    private String Category=CAT_PUBLIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("New Post");


        iv_camera.setOnClickListener(this);
        iv_smiley.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        iv_category_post.setOnClickListener(this);

    }
    public void getLocation() {

        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng latlng = new LatLng(latitude, longitude );
            String address=getAddress(latitude,longitude);
            Log.d("sun", latitude + "\n" + longitude);
        } else {
            gps.showSettingsAlert();
        }
    }
    public String getAddress(double latitude,double longitude){
        String address="";
//                    LocationAddress.getAddressFromLocation(latitude,longitude,LocationService.this,new GeocoderHandler());
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
//                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                }
                strReturnedAddress.append(returnedAddress.getCountryName()+" ");
                Log.d("sunil",strReturnedAddress.toString());
                address=strReturnedAddress.toString();
                return address;
            }
            else{
                Log.d("sunil","No Address returned!");
                return "";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("sunil","Canont get Address!");
            return "Current Location";
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_post, menu);//Menu Resource, Menu
        return true;
    }
    private final String TAG=getClass().getName();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                    finish();
                return true;
            case R.id.menu_post:
                    Log.d(TAG,"menu clicked");
                UploadPostData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UploadPostData(){
        Bitmap bmp=null;
        if(pictureImagePath.length()>0){
            bmp = BitmapFactory.decodeFile(pictureImagePath);
            bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4, false);
        }
        else{
            if(image_path_string.length()>0){
                bmp=BitmapFactory.decodeFile(image_path_string);
            }
        }
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        if(bmp!=null){
            nameValuePairs.add(new BasicNameValuePair("post_image", ImageUtil.encodeTobase64(bmp)));
        }
        else{
            nameValuePairs.add(new BasicNameValuePair("post_image", ""));
        }
        nameValuePairs.add(new BasicNameValuePair("post_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        nameValuePairs.add(new BasicNameValuePair("post_cat_id", Category));
        nameValuePairs.add(new BasicNameValuePair("post_msg", et_msg.getText().toString()));
        new WebServiceBase(nameValuePairs, this, API_REQUEST_CALL).execute(WebServicesUrls.NEWS_POST_API);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_camera:
                    SelectPicSelectDialog();
                break;
            case R.id.iv_location:
                getLocation();
                break;
            case R.id.iv_smiley:
                break;
            case R.id.iv_category_post:
                    showCategoryDialog();
                break;
        }
    }
    public void showCategoryDialog(){
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog1.setContentView(R.layout.dialog_choose_category);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog1.setTitle("Select");
        dialog1.setCancelable(true);
        dialog1.show();
        LinearLayout ll_followers= (LinearLayout) dialog1.findViewById(R.id.ll_followers);
        LinearLayout ll_public= (LinearLayout) dialog1.findViewById(R.id.ll_public);
        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        ll_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Category=CAT_FOLLOWERS;
                tv_category.setText(CAT_FOLLOWERS);
            }
        });
        ll_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Category=CAT_PUBLIC;
                tv_category.setText(CAT_PUBLIC);
            }
        });
    }


    public void SelectPicSelectDialog(){
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog1.setContentView(R.layout.dialog_choose_pictures);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog1.setTitle("Select");
        dialog1.setCancelable(true);
        dialog1.show();
        LinearLayout ll_gallery= (LinearLayout) dialog1.findViewById(R.id.ll_gallery);
        LinearLayout ll_camera= (LinearLayout) dialog1.findViewById(R.id.ll_camera);
        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        ll_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                SelectPictureFromCamera();
            }
        });
        ll_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                SelectPictureFromGallery();
            }
        });
    }
    public void SelectPictureFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_SELECT_CODE);
    }
    public void SelectPictureFromCamera(){
        String strMyImagePath = Environment.getExternalStorageDirectory() + File.separator + "temp.png";
        pictureImagePath = strMyImagePath;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
                        this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if(selectedImagePath!=null&&selectedImagePath!=""){
                    image_path_string=selectedImagePath;
                    Log.d("sunil",selectedImagePath);
//                    image_path=selectedImagePath;
                    Bitmap bmImg = BitmapFactory.decodeFile(image_path_string);
                    iv_selected_image.setImageBitmap(bmImg);
                    iv_selected_image.setVisibility(View.VISIBLE);
//                    Preferences.setCardImagePath(getApplicationContext(),selectedImagePath);
                    pictureImagePath="";
//                    startActivity(new Intent(SelectImageActivity.this,CardActivity.class));
                }
                else{
                    Toast.makeText(this,"File Selected is corrupted",Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path ="+selectedImagePath);
            }
        }
        if (requestCode==CAMERA_REQUEST&&resultCode == Activity.RESULT_OK) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                image_path_string="";
                Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath);
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4, false);
                iv_selected_image.setImageBitmap(bmp);
                iv_selected_image.setVisibility(View.VISIBLE);
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

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case API_REQUEST_CALL:
                    Log.d(TAG,response);
                parseResponse(response);
                break;
        }
    }
    private void parseResponse(String response){
        Gson gson=new Gson();
        NewsPostPOJO postPOJO=gson.fromJson(response,NewsPostPOJO.class);
        if(postPOJO!=null){
            if(postPOJO.getSuccess().equals("true")){
                Toast.makeText(getApplicationContext(),"Successfully Posted on your NewsFeed",Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","posted");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","unpublished");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    }
}
