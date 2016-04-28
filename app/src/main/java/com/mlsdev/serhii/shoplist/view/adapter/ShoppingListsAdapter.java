package com.mlsdev.serhii.shoplist.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ListItemBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>{
    private List<ShoppingList> shoppingLists = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoppingListViewModel shoppingListViewModel = new ShoppingListViewModel(shoppingLists.get(position));
        holder.binding.setViewModel(shoppingListViewModel);
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public void setData(List<ShoppingList> data) {
        shoppingLists = data;
        notifyDataSetChanged();
    }

    public void addItem(ShoppingList item) {
        shoppingLists.add(item);
        notifyItemInserted(shoppingLists.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
