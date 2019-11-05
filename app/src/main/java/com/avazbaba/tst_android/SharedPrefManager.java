package com.avazbaba.tst_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String SHARED_PREF_NAME_FOR_URL = "mysharedpref12_url";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_JWT = "jwt";
    private static final String Key_Url="url";


    private SharedPrefManager(Context context) {

        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String jwt) {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_DATE, todayAsString);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_JWT, jwt);
        editor.apply();
        return true;
    }

    public void seturl(String url){
        clearurl();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_FOR_URL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key_Url,url);
        editor.apply();
    }
    
    public String geturl(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_FOR_URL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key_Url,null);
    }

    public boolean clearurl(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_FOR_URL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public boolean  isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username =sharedPreferences.getString(KEY_USERNAME, null);
        String issueddatestr =sharedPreferences.getString(KEY_DATE, null);

        if (username!= null &&issueddatestr !=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(issueddatestr));
            cal.add(Calendar.HOUR_OF_DAY, 11);
            Date now = new Date();
            if(now.before(cal.getTime())){
                return true;
            }else {
                logout();
                return false;
            }
        }
        else{
        return false;
        }
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


    public String getUsername() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getJwt() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_JWT, null);
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int x = sharedPreferences.getInt(KEY_USER_ID, 0);
        return String.valueOf(x);
    }

    public String getDate() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DATE, null);
    }


}
