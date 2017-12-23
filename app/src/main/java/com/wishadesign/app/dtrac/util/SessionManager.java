package com.wishadesign.app.dtrac.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wishadesign.app.dtrac.activity.LoginActivity;

/**
 * Created by aksha on 12/10/2017.
 */

public class SessionManager {

    private static SessionManager instance;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    private int PRIVATE_MODE = 0;
    private static String PREF_NAME = "";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_TOKEN = "CPUserID";
    public static final String KEY_USERNAME = "username";

    private SessionManager(Context context) {
        this.context = context;
        PREF_NAME = context.getPackageName();
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if(instance == null){
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void createLoginSession(String token, String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if(!this.isLoggedIn()){
            redirectOnLogout(LoginActivity.class);
        }
    }


    public void logoutUser(){
        clearUserSettings();
        redirectOnLogout(LoginActivity.class);
    }

    public void redirectOnLogout(Class loginActivity) {

        Intent i = new Intent(context, loginActivity);

        // clear stack and start new activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public void clearUserSettings(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString(KEY_USERNAME, "");

    }

    public void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

}
