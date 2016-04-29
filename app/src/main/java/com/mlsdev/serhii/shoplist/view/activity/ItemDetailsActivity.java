package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityItemDetailsBinding;
import com.mlsdev.serhii.shoplist.databinding.ItemDetailsBinding;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListViewModel;

public class ItemDetailsActivity extends BaseActivity {
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
}
