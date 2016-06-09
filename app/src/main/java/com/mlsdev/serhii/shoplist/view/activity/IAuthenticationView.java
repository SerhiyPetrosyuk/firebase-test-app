package com.mlsdev.serhii.shoplist.view.activity;

/**
 * Created by serhii on 5/11/16.
 */
public interface IAuthenticationView extends IBaseView {
    void showEmailError(String errorMessage);
    void showPasswordError(String errorMessage);
}
