package com.mlsdev.serhii.shoplist.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShopListsFragmentBinding;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.activity.IShopListsView;
import com.mlsdev.serhii.shoplist.view.activity.ItemDetailsActivity;
import com.mlsdev.serhii.shoplist.view.activity.MainActivity;
import com.mlsdev.serhii.shoplist.view.adapter.ShoppingListsAdapter;
import com.mlsdev.serhii.shoplist.viewmodel.ShopListsViewModel;

public class ShopListsFragment extends BaseFragment implements IShopListsView,
        ShoppingListsAdapter.OnItemClickListener {
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
        viewModel = ShopListsViewModel.getNewInstance(this, adapter);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    public void setTitle(String userName) {
        String title = getString(R.string.users_list_title, userName);
        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity.getSupportActionBar() != null)
            mainActivity.getSupportActionBar().setTitle(title);
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public AppCompatActivity getViewActivity() {
        return (AppCompatActivity) getActivity();
    }
}
