package com.mlsdev.serhii.shoplist.view.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by serhii on 5/11/16.
 */
public interface IAuthenticationView {

    void showEmailError(String errorMessage);

    void showPasswordError(String errorMessage);

    void showMessage(String title, String message);

    AppCompatActivity getViewActivity();

    void hideKeyboard();

}
