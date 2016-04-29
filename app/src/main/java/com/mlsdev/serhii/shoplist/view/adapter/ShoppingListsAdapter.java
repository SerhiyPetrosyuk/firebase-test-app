package com.mlsdev.serhii.shoplist.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ListItemBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>{
    private List<DataSnapshot> dataSnapshots = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoppingList shoppingList = dataSnapshots.get(position).getValue(ShoppingList.class);
        ShoppingListViewModel shoppingListViewModel = new ShoppingListViewModel(shoppingList);
        holder.binding.setViewModel(shoppingListViewModel);
    }

    @Override
    public int getItemCount() {
        return dataSnapshots.size();
    }

    public void setData(List<DataSnapshot> data) {
        dataSnapshots = data;
        notifyDataSetChanged();
    }

    public void addItem(DataSnapshot item, String previousKey) {
        int position = 0;

        if (previousKey != null)
            position = getIndexForKey(previousKey) + 1;

        dataSnapshots.add(position, item);
        notifyItemInserted(dataSnapshots.size() - 1);
    }

    public void onItemChanged(DataSnapshot dataSnapshot) {
        int position = getIndexForKey(dataSnapshot.getKey());
        dataSnapshots.remove(position);
        dataSnapshots.add(position, dataSnapshot);
        notifyItemChanged(position);
    }

    public void onItemRemoved(String key) {
        int position = getIndexForKey(key);
        dataSnapshots.remove(position);
        notifyItemRemoved(position);
    }

    private int getIndexForKey(String key) {
        int index = 0;

        for (DataSnapshot dataSnapshot : dataSnapshots) {
            if (dataSnapshot.getKey().equals(key))
                return index;
            else
                index++;
        }

        throw new IllegalArgumentException("Key not found");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
