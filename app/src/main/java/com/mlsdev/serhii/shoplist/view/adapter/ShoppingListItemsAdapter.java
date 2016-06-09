package com.mlsdev.serhii.shoplist.view.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShoppingListItemBinding;
import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;
import com.mlsdev.serhii.shoplist.viewmodel.BaseViewModel;

/**
 * Created by serhii on 5/4/16.
 */
public class ShoppingListItemsAdapter extends BaseShoppingListAdapter<ShoppingListItemsAdapter.ViewHolder> {
    private AppCompatActivity activity;

    public ShoppingListItemsAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

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

        holder.binding.setViewModel(new ShoppingListItemViewModel(shoppingListItem, position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private ShoppingListItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public boolean onLongClick(View v) {
            Bundle args = new Bundle();
            args.putInt(Constants.EXTRA_DIALOG_TYPE, Constants.DIALOG_TYPE_EDITING_ITEM);
            args.putString(Constants.EXTRA_LIST_ITEM_TITLE, binding.tvListItemTitle.getText().toString());
            createDialog(args, binding.getViewModel());
            return false;
        }
    }

    public class ShoppingListItemViewModel extends BaseViewModel implements ShoppingListDialogFragment.OnCompleteListener {
        public final ObservableField<String> title;
        public int position;
        private final ShoppingListItem shoppingListItem;

        public ShoppingListItemViewModel(@NonNull ShoppingListItem shoppingListItem, int position) {
            this.position = position;
            this.shoppingListItem = shoppingListItem;
            this.title = new ObservableField<>(shoppingListItem.getTitle());
        }

        @Override
        public void onComplete(Bundle resultData) {
            int dialogType = resultData.getInt(Constants.EXTRA_DIALOG_TYPE);
            String title = resultData.getString(Constants.EXTRA_LIST_ITEM_TITLE);
            switch (dialogType) {
                case Constants.DIALOG_TYPE_EDITING_ITEM:
                    shoppingListItem.setTitle(title);
                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.ACTIVE_LIST_ITEMS)
                            .child(getParentKey())
                            .child(shoppingListItem.getKey())
                            .setValue(shoppingListItem);
                    break;
                case Constants.DIALOG_TYPE_REMOVE_ITEM:
                    FirebaseDatabase.getInstance().getReference()
                            .child(Constants.ACTIVE_LIST_ITEMS)
                            .child(getParentKey())
                            .child(shoppingListItem.getKey())
                            .removeValue();
                    break;
                default:
                    break;
            }
        }

        public void onRemoveItemClicked(View view) {
            Bundle args = new Bundle();
            args.putInt(Constants.EXTRA_DIALOG_TYPE, Constants.DIALOG_TYPE_REMOVE_ITEM);
            createDialog(args, this);
        }
    }

    public void createDialog(Bundle args, ShoppingListDialogFragment.OnCompleteListener listener) {
        ShoppingListDialogFragment dialogFragment = ShoppingListDialogFragment.getNewInstance(args);
        dialogFragment.setOnCompleteListener(listener);
        dialogFragment.show(activity.getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
    }
}
