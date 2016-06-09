package com.mlsdev.serhii.shoplist.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by serhii on 6/9/16.
 */
public interface IShopListsView {
    void setTitle(String title);
    Context getViewContext();
    AppCompatActivity getViewActivity();
}
