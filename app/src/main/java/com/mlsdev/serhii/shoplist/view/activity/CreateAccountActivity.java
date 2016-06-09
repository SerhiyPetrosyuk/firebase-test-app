package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityCreateAccountBinding;
import com.mlsdev.serhii.shoplist.view.listener.EditTextWatcher;
import com.mlsdev.serhii.shoplist.viewmodel.AccountViewModel;

public class CreateAccountActivity extends GoogleClientActivity implements IAuthenticationView {
    private ActivityCreateAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new AccountViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);
        binding.setViewModel(viewModel);
        initToolBar(false);
        setTitle(getString(R.string.title_create_account));
        EditTextWatcher textWatcher = new EditTextWatcher(viewModel, binding.etUserName, binding.etUserEmail,
                binding.etUserPassword);
        binding.etUserName.addTextChangedListener(textWatcher);
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
    protected void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showEmailError(String errorMessage) {
        binding.tilUserEmail.setError(errorMessage);
    }

    @Override
    public void showPasswordError(String errorMessage) {
        binding.tilUserPassword.setError(errorMessage);
    }

}
