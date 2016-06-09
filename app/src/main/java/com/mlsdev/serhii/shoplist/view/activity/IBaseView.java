package com.mlsdev.serhii.shoplist.view.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by serhii on 6/9/16.
 */
public interface IBaseView {
    void hideKeyboard();
    void showMessage(String title, String message);
    AppCompatActivity getViewActivity();
}
