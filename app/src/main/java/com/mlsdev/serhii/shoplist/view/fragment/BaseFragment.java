package com.mlsdev.serhii.shoplist.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mlsdev.serhii.shoplist.utils.Constants;

/**
 * Created by serhii on 25.04.16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ShoppingListDialogFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra(Constants.EXTRA_LIST_ITEM_TITLE);
            addItem(title);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public abstract void addItem(String title);

}
