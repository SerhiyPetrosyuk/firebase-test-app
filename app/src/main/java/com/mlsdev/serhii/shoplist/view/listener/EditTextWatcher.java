package com.mlsdev.serhii.shoplist.view.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.mlsdev.serhii.shoplist.viewmodel.AccountViewModel;

/**
 * Created by serhii on 5/11/16.
 */
public class EditTextWatcher implements TextWatcher {
    private AccountViewModel viewModel;
    private EditText userName, userEmail, userPassword;

    public EditTextWatcher(AccountViewModel viewModel, EditText userName, EditText userEmail, EditText userPassword) {
        this.viewModel = viewModel;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        viewModel.updateFields(
                userName != null ? userName.getText().toString() : null,
                userEmail != null ? userEmail.getText().toString() : null,
                userPassword != null ? userPassword.getText().toString() : null);
    }
}
