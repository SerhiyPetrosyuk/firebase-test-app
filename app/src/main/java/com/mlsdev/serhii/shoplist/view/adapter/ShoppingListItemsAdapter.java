package com.mlsdev.serhii.shoplist.view.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShoppingListItemBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.viewmodel.BaseViewModel;

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
        DataSnapshot dataSnapshot = dataSnapshots.get(position);
        ShoppingListItem shoppingListItem = dataSnapshot.getValue(ShoppingListItem.class);
        shoppingListItem.setKey(dataSnapshot.getKey());

        holder.binding.setViewModel(new ShoppingListItemViewModel(shoppingListItem));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShoppingListItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

    public class ShoppingListItemViewModel extends BaseViewModel {
        public final ObservableField<String> title;
        private ShoppingListItem shoppingListItem;

        public ShoppingListItemViewModel() {
            title = new ObservableField<>();
        }

        public ShoppingListItemViewModel(ShoppingListItem shoppingListItem) {
            this.shoppingListItem = shoppingListItem;
            this.title = new ObservableField<>(shoppingListItem.getTitle());
        }

        public void onRemoveItemClicked(View view) {
            getFirebase()
                    .child(Constants.ACTIVE_LIST_ITEMS)
                    .child(getParentKey())
                    .child(shoppingListItem.getKey())
                    .removeValue();
        }

    }
}
