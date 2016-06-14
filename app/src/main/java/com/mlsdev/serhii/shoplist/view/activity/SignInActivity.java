package com.mlsdev.serhii.shoplist.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivitySignInBinding;
import com.mlsdev.serhii.shoplist.view.listener.EditTextWatcher;
import com.mlsdev.serhii.shoplist.viewmodel.AccountViewModel;

public class SignInActivity extends GoogleClientActivity implements IAuthenticationView {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        viewModel = new AccountViewModel(this);

        binding.setViewModel(viewModel);
        EditTextWatcher textWatcher = new EditTextWatcher(viewModel, null, binding.etUserEmail,
                binding.etUserPassword);
        binding.etUserEmail.addTextChangedListener(textWatcher);
        binding.etUserPassword.addTextChangedListener(textWatcher);
        binding.btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onGoogleSignInClicked(v);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.getViewModel().onStop();
    }

    @Override
    public void showEmailError(String errorMessage) {
        binding.tilUserEmail.setErrorEnabled(true);
        binding.tilUserEmail.setError(errorMessage);
    }

    @Override
    public void showPasswordError(String errorMessage) {
        binding.tilUserPassword.setErrorEnabled(true);
        binding.tilUserPassword.setError(errorMessage);
    }

    @Override
    public void userAuthenticated() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
