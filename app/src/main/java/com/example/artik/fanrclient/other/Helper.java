package com.example.artik.fanrclient.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

/**
 * Created by artik on 04.05.2018.
 */

public class Helper {
    public static String HOST = "http://192.168.0.102:8080/";

    SharedPreferences pref;
    static SharedPreferences.Editor editor;
    Context _context;
    int MODE_PRIVATE = 0;
    private static final String PREFS_NAME= "checker";
    private static final String PREFS_CHECK = "check";

    private static final String PREFS_KEY = "key";
    public Helper(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(PREFS_CHECK, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(PREFS_CHECK, true);
    }

    public  String getToken() {
        return "Bearer "+pref.getString(PREFS_KEY,"");
    }

    public  void setToken(String token) {
        editor.putString(PREFS_KEY, token);
        setFirstTimeLaunch(false);
    }


    public   boolean isOnline( )
    {
        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }
}
