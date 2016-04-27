package com.mlsdev.serhii.shoplist.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ActivityItemDetailsBinding;

public class ItemDetailsActivity extends AppCompatActivity {
    private ActivityItemDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_details);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
