package com.mlsdev.serhii.shoplist.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShopListsFragmentBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.activity.ItemDetailsActivity;
import com.mlsdev.serhii.shoplist.view.adapter.ShoppingListsAdapter;
import com.mlsdev.serhii.shoplist.viewmodel.ShopListsViewModel;

public class ShopListsFragment extends BaseFragment implements ShoppingListsAdapter.OnItemClickListener {
    private ShopListsFragmentBinding binding;
    private ShopListsViewModel viewModel;

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
        ShoppingListsAdapter adapter = new ShoppingListsAdapter();
        adapter.setOnItemClickListener(this);
        viewModel = ShopListsViewModel.getNewInstance(getActivity(), adapter);
        binding.rvShopLists.setAdapter(adapter);
        binding.rvShopLists.setHasFixedSize(true);
        binding.rvShopLists.setItemAnimator(new DefaultItemAnimator());
        binding.rvShopLists.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void addItem(String title) {
        viewModel.addNewShopList(title);
    }

    @Override
    public void onItemClicked(String listId) {
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        intent.putExtra(Constants.KEY_LIST_ID, listId);
        startActivity(intent);
    }
}
