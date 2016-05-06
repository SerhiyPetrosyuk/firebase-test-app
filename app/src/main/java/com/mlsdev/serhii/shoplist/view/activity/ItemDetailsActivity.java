package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ItemDetailsBinding;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.adapter.ShoppingListItemsAdapter;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListViewModel;

public class ItemDetailsActivity extends BaseActivity implements ShoppingListDialogFragment.OnCompleteListener,
        ShoppingListViewModel.OnShoppingListRemovedListener {
    private ItemDetailsBinding binding;
    private ShoppingListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        binding = DataBindingUtil.setContentView(this, R.layout.item_details);
        initToolBar(true);
        viewModel = new ShoppingListViewModel(this, args, this);
        viewModel.setOnShoppingListRemovedListener(this);
        binding.setViewModel(viewModel);
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.onStop();
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
                createDialog(Constants.DIALOG_TYPE_EDITING);
                return true;
            case R.id.remove_menu_item:
                createDialog(Constants.DIALOG_TYPE_REMOVE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onComplete(Bundle resultData) {
        int dialogType = resultData.getInt(Constants.EXTRA_DIALOG_TYPE);
        if (dialogType == Constants.DIALOG_TYPE_EDITING || dialogType == Constants.DIALOG_TYPE_CREATING) {
            String title = resultData.getString(Constants.EXTRA_LIST_ITEM_TITLE);

            if (dialogType == Constants.DIALOG_TYPE_EDITING)
                viewModel.onEditListItem(title);
            else
                viewModel.onCreateNewListItem(title);
        } else {
            viewModel.removeShoppingList();
        }
    }

    private void createDialog(int dialogType) {
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DIALOG_TYPE, dialogType);
        ShoppingListDialogFragment dialog = ShoppingListDialogFragment.getNewInstance(args);
        dialog.setOnCompleteListener(this);
        dialog.show(getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
    }

    private void initRecyclerView() {
        ShoppingListItemsAdapter adapter = new ShoppingListItemsAdapter();
        binding.rvListItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvListItems.setHasFixedSize(true);
        binding.rvListItems.setItemAnimator(new DefaultItemAnimator());
        binding.rvListItems.setAdapter(adapter);
        viewModel.setAdapter(adapter);
    }

    @Override
    public void onShoppingListRemoved() {
        finish();
    }
}
