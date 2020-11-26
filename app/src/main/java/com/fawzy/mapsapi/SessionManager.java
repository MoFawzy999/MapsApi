package com.fawzy.mapsapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    private final static  String SHARED_PREF_NAME = "user_token" ;
    private final static  String KEY_NAME = "name" ;
    private final static  String KEY_EMAIL = "email" ;
    private final static  String KEY_TOKEN = "token" ;
    private final static  String   KEY_ID = "id" ;


     private static  SessionManager Instance ;
     private static Context mcontext ;


    private SessionManager(Context context) {
        mcontext = context ;
    }

    public static  synchronized SessionManager getInstance (Context context){
        if (Instance == null){
            Instance = new SessionManager(context);
        }
        return Instance ;
    }

    public void userlogin(User user){
         SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,mcontext.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putInt(KEY_ID , user.getId());
         editor.putString(KEY_NAME, user.getName());
         editor.putString(KEY_EMAIL,user.getEmail());
         editor.putString(KEY_TOKEN, user.getToken());
         editor.apply();
    }

    public boolean isLoggedin(){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,mcontext.MODE_PRIVATE);
        return  sharedPreferences.getString(KEY_TOKEN,null) != null ;
    }

    public User getToken(){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,mcontext.MODE_PRIVATE);
        return  new User(sharedPreferences.getString(KEY_TOKEN,null));
    }

    public User getUser(){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,mcontext.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_TOKEN, 0),
                sharedPreferences.getString(KEY_NAME,null),
                sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_TOKEN,null)
        );
    }

    public void logout(){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SHARED_PREF_NAME,mcontext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mcontext.startActivity(new Intent(mcontext,LoginActivity.class));
    }










}
