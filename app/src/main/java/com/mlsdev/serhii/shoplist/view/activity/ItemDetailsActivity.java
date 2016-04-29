package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityItemDetailsBinding;
import com.mlsdev.serhii.shoplist.databinding.ItemDetailsBinding;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListViewModel;

public class ItemDetailsActivity extends BaseActivity implements ShoppingListDialogFragment.OnCompleteListener {
    private ItemDetailsBinding binding;
    private ShoppingListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.item_details);
        initToolBar(true);
        viewModel = new ShoppingListViewModel(getIntent());
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu_item:
                Bundle args = new Bundle();
                args.putInt(Constants.EXTRA_DIALOG_TYPE, Constants.EDITING);
                ShoppingListDialogFragment dialog = ShoppingListDialogFragment.getNewInstance(args);
                dialog.setOnCompleteListener(this);
                dialog.show(getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onComplete(String title) {
        viewModel.onEditListTitle(title);
    }
}
