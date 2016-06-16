package com.mlsdev.serhii.shoplist.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.serhii.shoplist.model.ItemUser;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.model.User;
import com.mlsdev.serhii.shoplist.model.UserSession;
import com.mlsdev.serhii.shoplist.model.listeners.ShoppingListChildEventListener;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.utils.Utils;
import com.mlsdev.serhii.shoplist.view.activity.IShopListsView;
import com.mlsdev.serhii.shoplist.view.adapter.BaseShoppingListAdapter;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;

public class ShoppingListViewModel extends BaseViewModel implements CompoundButton.OnCheckedChangeListener {
    public final ObservableField<String> listName;
    public final ObservableField<String> ownerName;
    public final ObservableField<String> dateLastEditedDate;
    public final ObservableField<Boolean> isUserInShop;
    public final ObservableField<String> usersInShop;
    public final ObservableField<String> shoppersNumber;
    private IShopListsView view;
    private String key;
    private ShoppingList shoppingList;
    private OnShoppingListRemovedListener onShoppingListRemovedListener;
    private ShoppingListDialogFragment.OnCompleteListener onCompleteListener;
    private ShoppingListChildEventListener itemsChildEventListener;
    private ValueEventListener valueEventListener;
    private BaseShoppingListAdapter adapter;
    private ShoppingListDialogFragment dialogFragment;
    private boolean isListItemInvolved = false;
    private User user;

    public ShoppingListViewModel(IShopListsView view, Bundle initData,
                                 ShoppingListDialogFragment.OnCompleteListener onCompleteListener) {
        super();
        this.view = view;
        this.onCompleteListener = onCompleteListener;
        listName = new ObservableField<>();
        ownerName = new ObservableField<>();
        dateLastEditedDate = new ObservableField<>();
        isUserInShop = new ObservableField<>(false);
        usersInShop = new ObservableField<>();
        shoppersNumber = new ObservableField<>();
        key = initData.getString(Constants.KEY_LIST_ID);
        initDialogFragment();
        initUser();
    }

    public ShoppingListViewModel(ShoppingList shoppingList) {
        super();
        listName = new ObservableField<>(shoppingList.getListName());
        ownerName = new ObservableField<>(shoppingList.getOwnerName());
        dateLastEditedDate = new ObservableField<>(Utils.getFormattedDate(shoppingList.getDateLastChanged()));
        isUserInShop = new ObservableField<>(false);
        usersInShop = new ObservableField<>();
        shoppersNumber = new ObservableField<>();
    }

    private void initUser() {
        user = UserSession.getInstance().getUser(view.getViewContext());
    }

    public void onEditListItem(@Nullable String newTitle) {
        if (newTitle != null) shoppingList.setListName(newTitle);

        shoppingList.setDateLastChanged(Utils.getCurrentDateTime());
        databaseReference.child(Constants.ACTIVE_LISTS).child(key).setValue(shoppingList);
    }

    public void onCreateNewListItem(String title) {
        ShoppingListItem shoppingListItem = new ShoppingListItem(title);
        ItemUser itemUser = new ItemUser(user.getName(), user.getEmail());
        shoppingListItem.setOwner(itemUser);
        databaseReference.child(Constants.ACTIVE_LIST_ITEMS).child(key).push().setValue(shoppingListItem);
    }

    private void initDialogFragment() {
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DIALOG_TYPE, Constants.DIALOG_TYPE_CREATING);
        dialogFragment = ShoppingListDialogFragment.getNewInstance(args);
        dialogFragment.setOnCompleteListener(onCompleteListener);
    }

    public void onAddNewItemClicked(View view) {
        dialogFragment.show(this.view.getViewActivity().getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
    }

    public void removeShoppingList() {
        databaseReference.child(Constants.ACTIVE_LISTS).child(key).removeValue();
    }

    public void removeBoundItems() {
        databaseReference.child(Constants.ACTIVE_LIST_ITEMS).child(key).removeValue();
    }

    public void setAdapter(BaseShoppingListAdapter adapter) {
        this.adapter = adapter;
        initFirebaseListeners();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        adapter.changeShoppingModeState(isChecked);

        if (Utils.isUserInShop(shoppingList.getUsersInShop(), user.getEmail()) && isChecked)
            return;

        if (isChecked)
            shoppingList.addUserToShop(user.getEmail(), user.getName());
        else
            shoppingList.removeUserFromShop(user.getEmail());

        databaseReference.child(Constants.ACTIVE_LISTS).child(key).setValue(shoppingList);
    }

    public interface OnShoppingListRemovedListener {
        void onShoppingListRemoved();
    }

    public void setOnShoppingListRemovedListener(OnShoppingListRemovedListener onShoppingListRemovedListener) {
        this.onShoppingListRemovedListener = onShoppingListRemovedListener;
    }

    private void initFirebaseListeners() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null && onShoppingListRemovedListener != null) {
                    removeBoundItems();
                    onShoppingListRemovedListener.onShoppingListRemoved();
                    return;
                }

                shoppingList = dataSnapshot.getValue(ShoppingList.class);
                initShoppingList();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        };

        itemsChildEventListener = new ShoppingListChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                adapter.addItem(dataSnapshot, prevKey);
                if (isListItemInvolved) {
                    isListItemInvolved = false;
                    onEditListItem(null);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.onItemRemoved(dataSnapshot.getKey());
                onEditListItem(null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String key) {
                adapter.onItemChanged(dataSnapshot);
                onEditListItem(null);
            }
        };

        adapter.setParentKey(key);
        databaseReference.child(Constants.ACTIVE_LISTS).child(key).addValueEventListener(valueEventListener);
        databaseReference.child(Constants.ACTIVE_LIST_ITEMS).child(key).addChildEventListener(itemsChildEventListener);
    }

    private void initShoppingList() {
        listName.set(shoppingList.getListName());
        ownerName.set(shoppingList.getOwnerName());
        if (user != null) {
            boolean userInShop = Utils.isUserInShop(shoppingList.getUsersInShop(), user.getEmail());
            isUserInShop.set(userInShop);
        }
        dateLastEditedDate.set(Utils.getFormattedDate(shoppingList.getDateLastChanged()));
        view.showEditingButtons(Utils.isUserListOrItemOwner(shoppingList.getOwnerEmail(), user.getEmail()));
        updatedShoppersList();
    }

    private void updatedShoppersList() {
        String shoppers = shoppingList.getShoppers(user.getEmail());
        usersInShop.set(shoppers);
    }

    @Override
    public void onStop() {
        databaseReference.removeEventListener(itemsChildEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onComplete(Bundle resultData) {

        int dialogType = resultData.getInt(Constants.EXTRA_DIALOG_TYPE);

        String title = resultData.getString(Constants.EXTRA_LIST_ITEM_TITLE);
        switch (dialogType) {
            case Constants.DIALOG_TYPE_EDITING:
                onEditListItem(title);
                break;
            case Constants.DIALOG_TYPE_CREATING:
                onCreateNewListItem(title);
                isListItemInvolved = true;
                break;
            case Constants.DIALOG_TYPE_REMOVE:
                removeShoppingList();
                break;
            default:
                break;
        }
    }

}
