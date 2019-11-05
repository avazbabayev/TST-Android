package com.avazbaba.tst_android;

import android.content.Context;
import android.content.SharedPreferences;

import static com.avazbaba.tst_android.SharedPrefManager.*;

public class Constants {

    private static Constants mInstance;
    private static Context mCtx;
    public static final String Root_url = "http://mb.tst-tool.com/android/android/";
    public static final String Url_login = Root_url + "userlogin";
    public static final String url_wb = Root_url + "getwb";
    public static final String url_tour_start = Root_url + "tourstart";
    public static final String url_tour_finish = Root_url + "tourfinish";

    private Constants(Context context) {

        mCtx = context;
    }

    public static synchronized Constants getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Constants(context);
        }
        return mInstance;
    }

    public static String getRoot_url() {
        if(getRoot_url_from_shared()!=null){
            return getRoot_url_from_shared();
        }else{
            return Root_url;
        }
    }

    public static String getUrl_login() {
        if(getRoot_url_from_shared()!=null){
            return getRoot_url_from_shared() + "userlogin";
        }else{
            return Url_login;
        }
    }

    public static String getUrl_wb() {
        if(getRoot_url_from_shared()!=null){
            return getRoot_url_from_shared() + "getwb";
        }else{
            return url_wb;
        }
    }

    public static String getUrl_tour_start() {
        if(getRoot_url_from_shared()!=null){
            return getRoot_url_from_shared() + "tourstart";
        }else{
            return url_tour_start;
        }
    }

    public static String getUrl_tour_finish() {
        if(getRoot_url_from_shared()!=null){
            return getRoot_url_from_shared() + "tourfinish";
        }else{
            return url_tour_finish;
        }
    }

    public static String getRoot_url_from_shared(){
        return SharedPrefManager.getInstance(mCtx).geturl();
    }
}
