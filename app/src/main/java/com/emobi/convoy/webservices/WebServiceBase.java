package com.emobi.convoy.webservices;

/**
 * Created by sunil on 29-12-2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.emobi.convoy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sunil on 28-12-2016.
 */

public class WebServiceBase extends AsyncTask<String,Void,String> {
    ArrayList<NameValuePair> nameValuePairs;
    String jResult;
    Activity activity;
    Fragment fragment;
    static HttpClient httpClient;
    static HttpPost httppost;
    static HttpResponse response;
    static BufferedReader bufferedReader;
    InputStream is;
    ProgressDialog progressDialog;
    String msg;
    private final String TAG=getClass().getName();
    @Override
    public String toString() {
        return "WebServiceBase{" +
                "nameValuePairs=" + nameValuePairs.toString() +
                ", jResult='" + jResult + '\'' +
                ", activity=" + activity +
                ", is=" + is +
                ", progressDialog=" + progressDialog +
                ", msg='" + msg + '\'' +
                '}';
    }

    public WebServiceBase(ArrayList<NameValuePair> nameValuePairs, Activity activity, String msg){
        this.nameValuePairs=nameValuePairs;
        this.activity=activity;
        this.msg=msg;
        Log.d(TAG,this.toString());
    }
    public WebServiceBase(ArrayList<NameValuePair> nameValuePairs, Activity activity, Fragment fragment, String msg){
        this.nameValuePairs=nameValuePairs;
        this.activity=activity;
        this.msg=msg;
        this.fragment=fragment;
        Log.d(TAG,this.toString());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            jResult = httpCall(params[0], nameValuePairs);
        } catch (Exception e) {
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }
        return jResult;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        if(fragment!=null){
            WebServicesCallBack mcallback = (WebServicesCallBack) fragment;
            String rsp[] = {msg, s};
            mcallback.onGetMsg(rsp);
        }
        else {
            WebServicesCallBack mcallback = (WebServicesCallBack) activity;
            String rsp[] = {msg, s};
            mcallback.onGetMsg(rsp);
        }
    }


    public static String httpCall(String url, ArrayList<NameValuePair> postParameters) {
        String result = "";
        try {
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(url);

            httppost.setEntity(new UrlEncodedFormEntity(postParameters));

            // Execute HTTP Post Request
            response = httpClient.execute(httppost);

            //converting response into string
            result = convertToString(response);
            return result;
        } catch (IOException e) {
            Log.i("Io", e.toString());

            return "";
        }
    }

    private static String convertToString(HttpResponse response) {

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer stringBuffer = new StringBuffer("");
            String line = "";
            String LineSeparator = System.getProperty("line.separator");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + LineSeparator);
            }
            bufferedReader.close();
            return stringBuffer.toString();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

}
