package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mlsdev.serhii.shoplist.model.UserSession;
import com.mlsdev.serhii.shoplist.view.activity.CreateAccountActivity;
import com.mlsdev.serhii.shoplist.view.activity.IAuthenticationView;
import com.mlsdev.serhii.shoplist.view.activity.MainActivity;
import com.mlsdev.serhii.shoplist.view.activity.SignInActivity;

/**
 * Created by serhii on 5/10/16.
 */
public class AccountViewModel extends BaseViewModel {
    private IAuthenticationView authenticationView;
    private Firebase.AuthResultHandler authResultHandler;
    private Firebase.ResultHandler resultHandler;
    public final ObservableField<String> userName;
    public final ObservableField<String> userEmail;
    public final ObservableField<String> userPassword;

    public AccountViewModel(IAuthenticationView authenticationView) {
        this.authenticationView = authenticationView;
        userName = new ObservableField<>("");
        userEmail = new ObservableField<>("");
        userPassword = new ObservableField<>("");
        initListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onCreateButtonClicked(View view) {
        authenticationView.hideKeyboard();
        getFirebase().createUser(userEmail.get(), userPassword.get(), resultHandler);
    }

    public void onSignUpButtonClicked(View view) {
        authenticationView.hideKeyboard();
        getFirebase().authWithPassword(userEmail.get(), userPassword.get(), authResultHandler);
    }

    public void onShowCreateAccountScreen(View view) {
        authenticationView.getViewActivity().startActivity(
                new Intent(authenticationView.getViewActivity(), CreateAccountActivity.class));
        authenticationView.getViewActivity().finish();
    }

    public void onShowSignInScreen(View view) {
        authenticationView.getViewActivity().startActivity(
                new Intent(authenticationView.getViewActivity(), SignInActivity.class));
        authenticationView.getViewActivity().finish();
    }

    private void initListeners() {
        resultHandler = new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                onSignUpButtonClicked(null);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                handleErrors(firebaseError);
            }
        };

        authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                authenticationView.getViewActivity().startActivity(
                        new Intent(authenticationView.getViewActivity(), MainActivity.class));
                authenticationView.getViewActivity().finish();
                UserSession.getInstance().openSession(authenticationView.getViewActivity(),
                        authData.getToken(), authData.getExpires());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                handleErrors(firebaseError);
            }
        };
    }

    private void handleErrors(FirebaseError firebaseError) {
        switch (firebaseError.getCode()) {
            case FirebaseError.USER_DOES_NOT_EXIST:
                authenticationView.showMessage(null, firebaseError.getMessage());
                break;
            case FirebaseError.INVALID_EMAIL:
                authenticationView.showEmailError(firebaseError.getMessage());
                break;
            case FirebaseError.INVALID_PASSWORD:
                authenticationView.showPasswordError(firebaseError.getMessage());
                break;
            default:
                break;
        }
    }

    public void updateFields(@Nullable String name, String email, String password) {
        if (name != null && !name.equals(userName.get()))
            userName.set(name);

        if (!email.equals(userEmail.get()))
            userEmail.set(email);

        if (!password.equals(userPassword.get()))
            userPassword.set(password);
    }

}
