package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivitySignInBinding;
import com.mlsdev.serhii.shoplist.view.listener.EditTextWatcher;
import com.mlsdev.serhii.shoplist.viewmodel.AccountViewModel;

public class SignInActivity extends BaseActivity implements IAuthenticationView {
    private ActivitySignInBinding binding;
    private AccountViewModel viewModel;

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
    public void showMessage(String title, String message) {
        super.showMessage(title, message);
    }

    @Override
    public AppCompatActivity getViewActivity() {
        return this;
    }

    @Override
    public void hideKeyboard() {
        hideSoftKeyboard();
    }
}
