package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.model.User;
import com.mlsdev.serhii.shoplist.model.UserSession;
import com.mlsdev.serhii.shoplist.utils.Utils;
import com.mlsdev.serhii.shoplist.view.activity.CreateAccountActivity;
import com.mlsdev.serhii.shoplist.view.activity.GoogleClientActivity;
import com.mlsdev.serhii.shoplist.view.activity.IAuthenticationView;
import com.mlsdev.serhii.shoplist.view.activity.MainActivity;
import com.mlsdev.serhii.shoplist.view.activity.SignInActivity;

import static com.mlsdev.serhii.shoplist.utils.Constants.USER;

/**
 * Created by serhii on 5/10/16.
 */
public class AccountViewModel extends BaseViewModel implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "AccountViewModel";
    private IAuthenticationView authenticationView;
    private FirebaseAuth.AuthStateListener authStateListener;
    public final ObservableField<String> userName;
    public final ObservableField<String> userEmail;
    public final ObservableField<String> userPassword;
    protected GoogleApiClient googleApiClient;
    protected GoogleSignInResult signInResult;
    protected GoogleSignInAccount signInAccount;
    private FirebaseAuth auth;
    private OnCompleteListener<AuthResult> onCompleteListener;

    public AccountViewModel(IAuthenticationView authenticationView) {
        super();
        this.authenticationView = authenticationView;
        auth = FirebaseAuth.getInstance();
        userName = new ObservableField<>("");
        userEmail = new ObservableField<>("");
        userPassword = new ObservableField<>("");

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(authenticationView.getViewActivity().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(authenticationView.getViewActivity())
                .enableAutoManage(authenticationView.getViewActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        initListeners();
        auth.addAuthStateListener(authStateListener);

        OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (pendingResult.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(GoogleClientActivity.TAG, "Got cached sign-in");
            signInResult = pendingResult.get();
            handleSignInResult();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    signInResult = googleSignInResult;
                    handleSignInResult();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onCompleteListener = null;
        authStateListener = null;
        googleApiClient.disconnect();
        if (authStateListener != null)
            auth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onCreateButtonClicked(View view) {
        User user = new User(userEmail.get(), userName.get(), Utils.getCurrentDateTime());
        UserSession.getInstance().saveUserData(authenticationView.getViewActivity(), user.toJson());
        authenticationView.hideKeyboard();
        auth.createUserWithEmailAndPassword(userEmail.get(), userPassword.get())
                .addOnCompleteListener(onCompleteListener);
    }

    public void onSignIpButtonClicked(View view) {
        authenticationView.hideKeyboard();
        auth.signInWithEmailAndPassword(userEmail.get(), userPassword.get())
                .addOnCompleteListener(onCompleteListener);
    }

    public void onGoogleSignInClicked(View view) {
        signIn();
    }

    private void signInWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getIdToken());

        User user = new User(account.getEmail(), account.getDisplayName(), Utils.getCurrentDateTime());
        UserSession.getInstance().saveUserData(authenticationView.getViewActivity(), user.toJson());

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(onCompleteListener);
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
        onCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    authenticationView.showMessage(null, task.getException().getMessage());
                } else {
                    User user = UserSession.getInstance().getUser(authenticationView.getViewActivity());

                    if (user != null) {
                        databaseReference.child(USER).child(Utils.encodeEmail(user.getEmail())).setValue(user);
                    }

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    UserSession.getInstance().openSession(authenticationView.getViewActivity(),
                            firebaseUser.getUid(), Utils.getCurrentDateTime());
                    authenticationView.getViewActivity().startActivity(
                            new Intent(authenticationView.getViewActivity(), MainActivity.class));
                    authenticationView.getViewActivity().finish();
                }
            }
        };

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void updateFields(@Nullable String name, String email, String password) {
        if (name != null && !name.equals(userName.get()))
            userName.set(name);

        if (!email.equals(userEmail.get()))
            userEmail.set(email);

        if (!password.equals(userPassword.get()))
            userPassword.set(password);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(GoogleClientActivity.TAG, "onConnectionFailed:" + connectionResult);
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        authenticationView.getViewActivity().startActivityForResult(signInIntent, GoogleClientActivity.RC_SIGN_IN);
    }
    // [END signIn]

    // [START handleSignInResult]
    public void handleSignInResult(GoogleSignInResult signInResult) {
        this.signInResult = signInResult;
        handleSignInResult();
    }

    public void handleSignInResult() {
        if (signInResult.isSuccess()) {
            Log.d(GoogleClientActivity.TAG, "A user has been authenticated");
            signInAccount = signInResult.getSignInAccount();

            if (signInAccount != null) signInWithGoogleAccount(signInAccount);

        } else {
            // TODO: 5/30/16 handle an error
            Log.d(GoogleClientActivity.TAG, "A user hasn't been authenticated");
        }
    }
    // [END handleSignInResult]
}
