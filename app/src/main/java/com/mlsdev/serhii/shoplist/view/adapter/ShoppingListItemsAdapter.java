package com.mlsdev.serhii.shoplist.view.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.mlsdev.serhii.shoplist.R;
import com.mlsdev.serhii.shoplist.databinding.ShoppingListItemBinding;
import com.mlsdev.serhii.shoplist.model.ItemUser;
import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.model.User;
import com.mlsdev.serhii.shoplist.model.UserSession;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.utils.Utils;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;
import com.mlsdev.serhii.shoplist.viewmodel.BaseViewModel;

/**
 * Created by serhii on 5/4/16.
 */
public class ShoppingListItemsAdapter extends BaseShoppingListAdapter<ShoppingListItemsAdapter.ViewHolder> {
    private AppCompatActivity activity;
    private User currentUser;

    public ShoppingListItemsAdapter(AppCompatActivity activity) {
        this.activity = activity;
        currentUser = UserSession.getInstance().getUser(activity);
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

        TextView title = holder.binding.tvListItemTitle;

        holder.binding.tvListItemTitle.setPaintFlags(shoppingListItem.getBought()
                ? (title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG)
                : (title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)));

        boolean isUserItemOwner = Utils.isUserListOrItemOwner(
                shoppingListItem.getOwner().getEmail(),
                currentUser.getEmail());

        if (shoppingListItem.getBought()) {
            ItemUser currentItemUser = new ItemUser(currentUser.getName(), currentUser.getEmail());
            String buyerName = Utils.getBuyerName(currentItemUser, shoppingListItem.getBuyer());
            holder.binding.tvBoughtByUser.setText(activity.getString(R.string.bought_by, buyerName));
            holder.binding.tvBoughtByUser.setVisibility(View.VISIBLE);

        } else {
            holder.binding.tvBoughtByUser.setVisibility(View.INVISIBLE);
        }

        holder.binding.setViewModel(new ShoppingListItemViewModel(shoppingListItem, position));
        holder.binding.btnRemoveItem.setVisibility(shoppingListItem.getBought() || !isUserItemOwner
                ? View.INVISIBLE : View.VISIBLE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,
            View.OnClickListener {
        private ShoppingListItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            binding.getViewModel().onItemBought(!binding.getViewModel().shoppingListItem.getBought());
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
                    databaseReference
                            .child(Constants.ACTIVE_LIST_ITEMS)
                            .child(getParentKey())
                            .child(shoppingListItem.getKey())
                            .setValue(shoppingListItem);
                    break;
                case Constants.DIALOG_TYPE_REMOVE_ITEM:
                    databaseReference
                            .child(Constants.ACTIVE_LIST_ITEMS)
                            .child(getParentKey())
                            .child(shoppingListItem.getKey())
                            .removeValue();
                    break;
                default:
                    break;
            }
        }

        public void onItemBought(boolean isBought) {
            User currentUser = UserSession.getInstance().getUser(activity);
            ItemUser buyer = isBought ? new ItemUser(currentUser.getName(), currentUser.getEmail()) : shoppingListItem.getBuyer();
            boolean isCurrentUserBuyer = Utils.isUserListOrItemOwner(buyer.getEmail(), currentUser.getEmail());

            if (!isBought && !isCurrentUserBuyer)
                return;

            shoppingListItem.setBought(isBought);
            shoppingListItem.setBuyer(isBought ? buyer : null);
            databaseReference.child(Constants.ACTIVE_LIST_ITEMS)
                    .child(getParentKey())
                    .child(shoppingListItem.getKey())
                    .setValue(shoppingListItem);
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
