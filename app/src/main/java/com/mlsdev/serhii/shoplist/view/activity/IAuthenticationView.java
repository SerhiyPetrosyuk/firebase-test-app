package com.mlsdev.serhii.shoplist.view.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by serhii on 5/11/16.
 */
public interface IAuthenticationView {

    void showEmailError(String errorMessage);

    void showPasswordError(String errorMessage);

    AppCompatActivity getViewActivity();

}
