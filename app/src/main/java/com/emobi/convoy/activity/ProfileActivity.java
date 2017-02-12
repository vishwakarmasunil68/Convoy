package com.emobi.convoy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.PublicPost.PublicPostResponsePOJO;
import com.emobi.convoy.pojo.PublicPost.publicPostPOJO;
import com.emobi.convoy.utility.ImageUtil;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements WebServicesCallBack,View.OnClickListener {
    private final static String NEWS_FEED_API = "news_feed_api";
    private final static String UPDATE_PROFILE_API = "update_profile_api";
    private static final int FILE_SELECT_CODE = 0;
    private final String TAG = getClass().getName();

    @BindView(R.id.ll_scroll_news)
    LinearLayout ll_scroll_news;
    @BindView(R.id.iv_cover_pic)
    ImageView iv_cover_pic;
    @BindView(R.id.iv_profile_pic)
    CircleImageView iv_profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Log.d(TAG,"profile_id:-"+Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));
        callNewFeedApi();
        iv_profile_pic.setOnClickListener(this);
    }

    public void callNewFeedApi() {
//        callNewFeedApi();
        Log.d(TAG,"profile_pic:-"+ Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,""));
        Picasso.with(getApplicationContext())
                .load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,""))
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(iv_profile_pic);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, NEWS_FEED_API).execute(WebServicesUrls.NEWS_FEED_API);

    }

    public void SelectPictureFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, FILE_SELECT_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            Log.d("sun", "on activity result");
            if (resultCode == Activity.RESULT_OK) {
                if (null == data)
                    return;
                Uri selectedImageUri = data.getData();
                System.out.println(selectedImageUri.toString());
                // MEDIA GALLERY
                String selectedImagePath = getPath(
                        this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    Log.d(TAG,"image path:-"+ selectedImagePath);
                    Bitmap bmImg = BitmapFactory.decodeFile(selectedImagePath);
                    if(bmImg!=null){
                        UpdateProfile(ImageUtil.encodeTobase64(bmImg));
                    }
                } else {
                    Toast.makeText(this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
        }
    }
    public void UpdateProfile(String image_base_64){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("log_pics", image_base_64));
        nameValuePairs.add(new BasicNameValuePair("log_password", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD, "")));
        nameValuePairs.add(new BasicNameValuePair("log_name", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME, "")));
        nameValuePairs.add(new BasicNameValuePair("log_email", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL, "")));
        nameValuePairs.add(new BasicNameValuePair("log_mob", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE, "")));
        nameValuePairs.add(new BasicNameValuePair("log_facbook", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK, "")));
        nameValuePairs.add(new BasicNameValuePair("log_tag", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_TAG, "")));
        nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN, "")));
        nameValuePairs.add(new BasicNameValuePair("log_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, UPDATE_PROFILE_API).execute(WebServicesUrls.UPDATE_PROFILE_API);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];

        switch (apicall) {
            case NEWS_FEED_API:
                parseNewsFeedData(response);
                break;
            case  UPDATE_PROFILE_API:
                Log.d(TAG,"response:-"+response);
        }
    }

    public void parseNewsFeedData(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            Gson gson = new Gson();
            publicPostPOJO publicPostPOJOobj = gson.fromJson(response, publicPostPOJO.class);
            if (publicPostPOJOobj != null) {
                if (publicPostPOJOobj.getSuccess().equals("true")) {
                    if (publicPostPOJOobj.getList_news() != null && publicPostPOJOobj.getList_news().size() > 0) {
                        inflateResponse(publicPostPOJOobj.getList_news());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            Toast.makeText(getApplicationContext(), "SOmething went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void inflateResponse(List<PublicPostResponsePOJO> post_response) {
        for (int i = 0; i < post_response.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.inflate_profile_news_feed, null);
            CircleImageView iv_profile = (CircleImageView) v.findViewById(R.id.iv_profile);
            TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
            TextView tv_type = (TextView) v.findViewById(R.id.tv_type);
            TextView tv_post = (TextView) v.findViewById(R.id.tv_post);
            ImageView iv_post_image = (ImageView) v.findViewById(R.id.iv_post_image);


            String image_url = WebServicesUrls.IMAGE_BASE_URL + post_response.get(i).getLog_pics();
            Log.d("sunil", "image urls:-" + image_url);
            Picasso.with(getApplicationContext())
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_profile);
            tv_post.setText(post_response.get(i).getPost_msg());

            tv_type.setText(post_response.get(i).getPost_cat_id());

            Picasso.with(getApplicationContext())
                    .load(WebServicesUrls.IMAGE_BASE_URL + post_response.get(i).getPost_image())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_post_image);

            ll_scroll_news.addView(v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_profile_pic:
                    SelectPictureFromGallery();
                break;
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
