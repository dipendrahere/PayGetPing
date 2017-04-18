package com.example.dipendra.paygetping.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.dipendra.paygetping.R;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dipendra on 06/04/17.
 */

public class Constants {
    final static public String BASE_URL = "https://paygetping.firebaseio.com/";
    private static FirebaseDatabase database;
    public static FirebaseDatabase getDatabase(){
        if(database == null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    public static int menu = R.menu.menu_main;
    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
