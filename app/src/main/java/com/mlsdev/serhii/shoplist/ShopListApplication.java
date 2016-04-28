package com.mlsdev.serhii.shoplist;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.Logger;

/**
 * Created by serhii on 22.04.16.
 */
public class ShopListApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
//        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
    }
}
