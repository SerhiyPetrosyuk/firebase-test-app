package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityCreateAccountBinding;
import com.mlsdev.serhii.shoplist.view.listener.EditTextWatcher;
import com.mlsdev.serhii.shoplist.viewmodel.AccountViewModel;

public class CreateAccountActivity extends BaseActivity implements IAuthenticationView {
    private ActivityCreateAccountBinding binding;
    private AccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new AccountViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);
        binding.setViewModel(viewModel);
        initToolBar(false);
        setTitle(getString(R.string.title_create_account));
        EditTextWatcher textWatcher = new EditTextWatcher(viewModel, null, binding.etUserEmail,
                binding.etUserPassword);
        binding.etUserName.addTextChangedListener(textWatcher);
        binding.etUserEmail.addTextChangedListener(textWatcher);
        binding.etUserPassword.addTextChangedListener(textWatcher);
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

    @Override
    public AppCompatActivity getViewActivity() {
        return this;
    }

    @Override
    public void hideKeyboard() {
        hideSoftKeyboard();
    }

    @Override
    public void showMessage(@Nullable String title, String message) {
        super.showMessage(title, message);
    }
}
