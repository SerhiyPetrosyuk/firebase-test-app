package com.mlsdev.serhii.shoplist.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShoppingListItemBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListItemViewModel;

/**
 * Created by serhii on 5/4/16.
 */
public class ShoppingListItemsAdapter extends BaseShoppingListAdapter<ShoppingListItemsAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoppingListItem shoppingListItem = dataSnapshots.get(position).getValue(ShoppingListItem.class);
        holder.binding.setViewModel(new ShoppingListItemViewModel(shoppingListItem.getTitle()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ShoppingListItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
