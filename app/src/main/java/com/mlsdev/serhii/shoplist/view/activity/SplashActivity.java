package com.mlsdev.serhii.shoplist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.model.UserSession;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserSession.getInstance().retrySession(this);

        if (UserSession.getInstance().isActive())
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, SignInActivity.class));

        finish();
    }
}
