package com.mlsdev.serhii.shoplist.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShopListsFragmentBinding;

public class ShopListsFragment extends BaseFragment {
    public ShopListsFragmentBinding binding;

    public static ShopListsFragment getNewInstance(Bundle args) {
        ShopListsFragment shopListsFragment = new ShopListsFragment();
        shopListsFragment.setArguments(args);
        return shopListsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_lists_fragment, container, false);
        binding = DataBindingUtil.bind(view);
        initList();
        return view;
    }

    private void initList() {
        binding.rvShopLists.setHasFixedSize(true);
        binding.rvShopLists.setItemAnimator(new DefaultItemAnimator());
        binding.rvShopLists.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void addItem(String title) {
        // TODO: 25.04.16 add a new item
        Log.d("Test", title);
    }
}
