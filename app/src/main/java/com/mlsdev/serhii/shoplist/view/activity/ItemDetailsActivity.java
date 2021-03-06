package com.mlsdev.serhii.shoplist.view.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class ItemDetailsActivity extends BaseActivity implements IShopListsView,
        ShoppingListDialogFragment.OnCompleteListener,
        ShoppingListViewModel.OnShoppingListRemovedListener {
    private ItemDetailsBinding binding;
    private ShoppingListViewModel viewModel;
    private boolean isUserListOwner = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        binding = DataBindingUtil.setContentView(this, R.layout.item_details);
        initToolBar(true);
        viewModel = new ShoppingListViewModel(this, args, this);
        viewModel.setOnShoppingListRemovedListener(this);
        binding.setViewModel(viewModel);
        binding.switchShoppingMode.setOnCheckedChangeListener(viewModel);
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

        MenuItem removeList = menu.findItem(R.id.remove_menu_item);
        MenuItem editList = menu.findItem(R.id.edit_menu_item);

        removeList.setVisible(isUserListOwner);
        editList.setVisible(isUserListOwner);

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
        viewModel.onComplete(resultData);
    }

    private void createDialog(int dialogType) {
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DIALOG_TYPE, dialogType);
        args.putString(Constants.EXTRA_LIST_ITEM_TITLE, binding.tvListTitle.getText().toString());
        ShoppingListDialogFragment dialog = ShoppingListDialogFragment.getNewInstance(args);
        dialog.setOnCompleteListener(this);
        dialog.show(getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
    }

    private void initRecyclerView() {
        ShoppingListItemsAdapter adapter = new ShoppingListItemsAdapter(this);
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

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public AppCompatActivity getViewActivity() {
        return this;
    }

    @Override
    public void showEditingButtons(boolean isShow) {
        isUserListOwner = isShow;
        invalidateOptionsMenu();
    }
}
