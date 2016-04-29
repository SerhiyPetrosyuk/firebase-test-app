package com.mlsdev.serhii.shoplist.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.DialogFragmentBinding;
import com.mlsdev.serhii.shoplist.utils.Constants;

public class ShoppingListDialogFragment extends DialogFragment {
    public final static int REQUEST_CODE = 0;
    private DialogFragmentBinding binding;
    private int dialogType = Constants.CREATING;
    private OnCompleteListener onCompleteListener;

    public static ShoppingListDialogFragment getNewInstance(Bundle args) {
        ShoppingListDialogFragment dialogFragment = new ShoppingListDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogType = getArguments().getInt(Constants.EXTRA_DIALOG_TYPE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.dialog_fragment, null);
        binding = DataBindingUtil.bind(rootView);
        binding.etItemTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                    onComplete();
                    ShoppingListDialogFragment.this.dismiss();
                }
                return true;
            }
        });

        String positiveButtonLabel = dialogType == Constants.CREATING
                ? getString(R.string.button_create)
                : getString(R.string.button_edit);

        builder.setView(rootView)
                .setPositiveButton(positiveButtonLabel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onComplete();
                            }
                        });

        if (dialogType == Constants.EDITING)
            builder.setNegativeButton(getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

        return builder.create();
    }

    private void onComplete() {
        if (getTargetFragment() == null) {
            onCompleteListener.onComplete(binding.etItemTitle.getText().toString());
        } else {
            Intent resultData = new Intent();
            resultData.putExtra(Constants.EXTRA_LIST_ITEM_TITLE, binding.etItemTitle.getText().toString());
            getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, resultData);
        }
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public interface OnCompleteListener {
        void onComplete(String title);
    }

}
