package com.mlsdev.serhii.shoplist.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.utils.Utils;

/**
 * Created by serhii on 5/14/16.
 */
public class UserSession {
    private static UserSession userSession;
    private String token;
    private long expires;

    public static UserSession getInstance() {
        if (userSession == null)
            userSession = new UserSession();

        return userSession;
    }

    public void retrySession(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        token = sharedPreferences.getString(Constants.SESSION_TOKEN_KEY, null);
        expires = sharedPreferences.getLong(Constants.SESSION_EXPIRES_KEY, 0);
    }

    public boolean isActive() {
        return token != null && expires != 0 && expires > Utils.getCurrentDateTime();
    }

    public void openSession(Context context, String token, long expires) {
        expires *= 1000;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SESSION_TOKEN_KEY, token);
        this.token = token;
        editor.putLong(Constants.SESSION_EXPIRES_KEY, expires);
        this.expires = expires;
        editor.apply();
    }

    public void saveUserData(Context context, String json) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_MODEL_KEY, json).apply();
    }

    public User getUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userJson = sharedPreferences.getString(Constants.USER_MODEL_KEY, null);
        Gson gson = new Gson();
        return gson.fromJson(userJson, User.class);
    }
}
