package com.example.dipendra.paygetping.utils;

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
}
