package com.emobi.convoy.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunil on 19-01-2017.
 */

public class Pref {

    private static final String PrefDB="convey.txt";

    public static final String PROFILE_IMAGE="profile_image";
    public static final String PROFILE_NAME="profile_name";
    public static final String FCM_REGISTRATION_TOKEN="fcm_registration_token";

    public static void SetStringPref(Context context,String KEY,String Value){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(KEY,Value);
        editor.commit();
    }

    public static String GetStringPref(Context context,String KEY,String defValue){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        return sp.getString(KEY,defValue);
    }

    public static void SetBooleanPref(Context context,String KEY,boolean Value){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(KEY,Value);
        editor.commit();
    }

    public static boolean GetBooleanPref(Context context,String KEY,boolean defValue){
        SharedPreferences sp=context.getSharedPreferences(PrefDB,Context.MODE_PRIVATE);
        return sp.getBoolean(KEY,defValue);
    }
}
