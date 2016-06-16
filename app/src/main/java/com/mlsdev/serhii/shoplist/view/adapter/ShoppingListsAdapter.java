package com.mlsdev.serhii.shoplist.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ListItemBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.viewmodel.ShoppingListViewModel;

public class ShoppingListsAdapter extends BaseShoppingListAdapter<ShoppingListsAdapter.ViewHolder> {
    private Context context;

    public ShoppingListsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShoppingList shoppingList = dataSnapshots.get(position).getValue(ShoppingList.class);
        ShoppingListViewModel shoppingListViewModel = new ShoppingListViewModel(shoppingList);
        setShoppersNumber(shoppingListViewModel, shoppingList.getUsersInShop().size());
        holder.binding.setViewModel(shoppingListViewModel);
    }

    public void setShoppersNumber(ShoppingListViewModel viewModel, int shoppers) {
        if (shoppers == 0) return;

        String shoppersNumberString = shoppers == 1
                ? context.getString(R.string.one_shopper, shoppers)
                : context.getString(R.string.more_than_one_shopper, shoppers);
        viewModel.shoppersNumber.set(shoppersNumberString);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClicked(dataSnapshots.get(getAdapterPosition()).getKey());
        }
    }
}
