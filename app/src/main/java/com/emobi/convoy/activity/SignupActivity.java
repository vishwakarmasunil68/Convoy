package com.emobi.convoy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.pojo.RegisterResultPOJO;
import com.emobi.convoy.utility.FileUtils;
import com.emobi.convoy.utility.ImageUtil;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,WebServicesCallBack {
    @BindView(R.id.iv_twitter)
    ImageView iv_twitter;
    @BindView(R.id.iv_fb)
    ImageView iv_fb;
    @BindView(R.id.iv_google)
    ImageView iv_google;
    @BindView(R.id.tv_signup_email)
    TextView tv_signup_email;

    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    CallbackManager callbackManager;

    String email="";
    String name="";
    String profile_image_url ="";

    private static final String TWITTER_KEY = "7R8opfRwJ6fj6GkRLwog3VBhx";
    private static final String TWITTER_SECRET = "qxIctQH9skRZvg11q1VpwhuTuji3A8IkFleEqnnbBKYlbDy3H6";
    TwitterLoginButton twitterLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        RequestData();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignupActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SignupActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initializeGoogleSignIn();

        //Setting onclick listener to signing button
        signInButton.setOnClickListener(this);

        iv_twitter.setOnClickListener(this);
        iv_fb.setOnClickListener(this);
        iv_google.setOnClickListener(this);
        tv_signup_email.setOnClickListener(this);


        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLogin);
        //Add callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        if(Pref.GetBooleanPref(getApplicationContext(),StringUtils.IS_LOGIN,false)){
            startActivity(new Intent(SignupActivity.this,HomeActivity.class));
            finishAffinity();
        }
    }

    public void GetData(){
        if(email.length()>0&& profile_image_url.length()>0&&name.length()>0){
            Log.d("sunil","email:-"+email);
            Log.d("sunil","profile_image_url:-"+ profile_image_url);
            getBitmapCallApi(profile_image_url,name,email,"");
        }
    }
    private final String TAG=getClass().getName();
    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;

//        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        TwitterAuthClient authClient = new TwitterAuthClient();
        Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
        userResult.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                User user = result.data;
                String user_name = user.email;
                String description = user.description;
                int followersCount = user.followersCount;
                Log.d(TAG,"name:-"+user.name);
                name=user.name;
                String profileImage = user.profileImageUrl.replace("_normal", "");
                profile_image_url =profileImage;
                GetData();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("sunil","failed");
            }
        });
//        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                email=result.data;
                GetData();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

    }
    public void initializeGoogleSignIn(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_twitter:
                TwitterIntegration();
                break;
            case R.id.iv_fb:
                FbIntegration();
                break;
            case R.id.iv_google:
                GoogleIntegration();
                break;
            case R.id.tv_signup_email:
                startActivity(new Intent(this, SignUpEmailActivity.class));
                break;
        }
    }

    public void TwitterIntegration() {

    }

    public void FbIntegration() {
        LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
//        startActivity(new Intent(this,HomeActivity.class));
    }

    public void GoogleIntegration() {
        signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            try {
                Log.d("sunil", "name:-" + acct.getDisplayName());
                Log.d("sunil", "email:-" + acct.getEmail());
//                Log.d("sunil", "image:-" + acct.getPhotoUrl().toString());
                Log.d("sunil", "id:-" + acct.getId());
                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME,acct.getDisplayName());
                Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS,acct.getPhotoUrl().toString());
                getBitmapCallApi(acct.getPhotoUrl().toString(),acct.getDisplayName(),
                        acct.getEmail(),"");
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Your Google account is not configured with google plus account",Toast.LENGTH_SHORT).show();
            }

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("sunil", response.toString());
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
//                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
//                        Log.d("sunil",text);
                        Log.d("sunil", json.getString("name"));
                        Log.d("sunil", json.getString("link"));
                        Log.d("sunil", json.getString("id"));
                        Log.d("sunil", json.getString("email"));
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME, json.getString("name"));
                        String path = FileUtils.BASE_FILE_PATH;
                        File file = new File(path + File.separator + "fb_profile.png");
                        String profile_url="https://graph.facebook.com/" + json.getString("id") + "/picture?type=large";
                        Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS,profile_url);

                        getBitmapCallApi(profile_url,json.getString("name"),
                                json.getString("email"),"");
                    }

                } catch (JSONException e) {
                    Log.d("profile", e.toString());
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getBitmapCallApi(final String image_url, final String name, final String email, final String mobile_no) {
//        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
        new AsyncTask<Void,Void,Void>(){
            Bitmap myBitmap;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL(image_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callRegisterAPI(myBitmap,name,email,mobile_no);
            }
        }.execute();
//        new AsyncTask<Void,Void,Void>(){
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                Profile profile=Profile.getCurrentProfile();
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//
//                    URL imageURL = new URL("https://graph.facebook.com/" + user_id + "/picture?type=large");
//                    Pref.SetStringPref(getApplicationContext(),Pref.PROFILE_IMAGE,"https://graph.facebook.com/" + user_id + "/picture?type=large");
//                    Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
//                    String path = FileUtils.BASE_FILE_PATH;
//                    OutputStream fOut = null;
//                    File file = new File(path+File.separator+"fb_profile.png"); // the File to save to
//                    fOut = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//                    fOut.flush();
//                    fOut.close();
//                    Log.d("sunil","saved");
//                } catch (MalformedURLException e) {
//                    Log.d("sunil","malformedurl");
//                } catch (IOException e) {
//                    Log.d("sunil","IOexception");
//                }
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//            }
//        }.execute();
    }
    public void callRegisterAPI(Bitmap bmp,String name,String email,String mobile_no){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("log_pics", ImageUtil.encodeTobase64(bmp)));
        nameValuePairs.add(new BasicNameValuePair("log_password", "1234"));
        nameValuePairs.add(new BasicNameValuePair("log_name", name));
        nameValuePairs.add(new BasicNameValuePair("log_email", email));
        nameValuePairs.add(new BasicNameValuePair("log_mob", mobile_no));
        nameValuePairs.add(new BasicNameValuePair("log_facbook", ""));
        nameValuePairs.add(new BasicNameValuePair("log_tag", ""));
        nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetStringPref(getApplicationContext(), Pref.FCM_REGISTRATION_TOKEN, "")));
        nameValuePairs.add(new BasicNameValuePair("log_entertainment", "entertainment"));
        nameValuePairs.add(new BasicNameValuePair("log_sports", "sports"));
        nameValuePairs.add(new BasicNameValuePair("log_travelling", ""));
        nameValuePairs.add(new BasicNameValuePair("log_study", ""));
        nameValuePairs.add(new BasicNameValuePair("log_gaming", ""));
        nameValuePairs.add(new BasicNameValuePair("log_technology", ""));
        nameValuePairs.add(new BasicNameValuePair("log_action", ""));
        nameValuePairs.add(new BasicNameValuePair("log_everything", ""));
        nameValuePairs.add(new BasicNameValuePair("log_family", ""));
        nameValuePairs.add(new BasicNameValuePair("log_ans1", ""));
        nameValuePairs.add(new BasicNameValuePair("log_ans2", ""));
        nameValuePairs.add(new BasicNameValuePair("log_ans3", ""));
        nameValuePairs.add(new BasicNameValuePair("log_ans4", ""));
        nameValuePairs.add(new BasicNameValuePair("log_ans5", ""));
        nameValuePairs.add(new BasicNameValuePair("log_pics", ""));
        new WebServiceBase(nameValuePairs, this, "register").execute(WebServicesUrls.REGISTER_URL);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case "register":
                Log.d("sunil","response:-"+response);
                parseRegisterJSON(response);
                break;
        }
    }

    public void parseRegisterJSON(String response){
        Gson gson=new Gson();
        RegisterResultPOJO pojo1=gson.fromJson(response,RegisterResultPOJO.class);
        if(pojo1!=null){
            RegisterPOJO pojo=pojo1.getList_result();
            if(pojo!=null) {
//            if(pojo.getLog_message().equals("Email Or password Allready Exit!")) {
                try {
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ID, pojo.getLog_id());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME, pojo.getLog_name());
                    Log.d(TAG, "reg name:-" + pojo.getLog_name());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL, pojo.getLog_email());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD, pojo.getLog_password());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE, pojo.getLog_mob());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK, pojo.getLog_facbook());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TAG, pojo.getLog_tag());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LAST_LOGIN, pojo.getLog_last_login());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_CREATED, pojo.getLog_created());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PICS, "http://pornvideocum.com/user/" + pojo.getLog_pics());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN, pojo.getLog_device_token());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ENTERTAINMENT, pojo.getLog_entertainment());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_SPORTS, pojo.getLog_sports());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TRAVELLING, pojo.getLog_travelling());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STUDY, pojo.getLog_study());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GAMING, pojo.getLog_gaming());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TECHNOLOGY, pojo.getLog_technology());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ACTION, pojo.getLog_action());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EVERYTHING, pojo.getLog_everything());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FAMILY, pojo.getLog_family());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS1, pojo.getLog_ans1());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS2, pojo.getLog_ans2());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS3, pojo.getLog_ans3());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS4, pojo.getLog_ans4());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS5, pojo.getLog_ans5());
                    Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MESSAGE, pojo.getLog_message());
                    Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, true);

                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } catch (Exception e) {

                }
            }
//            }

        }
    }
}
