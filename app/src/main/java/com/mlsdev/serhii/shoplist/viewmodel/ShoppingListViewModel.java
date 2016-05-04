package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.firebase.client.annotations.Nullable;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.model.listeners.ShoppingListChildEventListener;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.utils.Utils;
import com.mlsdev.serhii.shoplist.view.adapter.BaseShoppingListAdapter;
import com.mlsdev.serhii.shoplist.view.fragment.ShoppingListDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class ShoppingListViewModel extends BaseViewModel {
    public final ObservableField<String> listName;
    public final ObservableField<String> ownerName;
    public final ObservableField<String> dateLastEditedDate;
    private Intent initData;
    private String key;
    private ShoppingList shoppingList;
    private OnShoppingListRemovedListener onShoppingListRemovedListener;
    private ShoppingListDialogFragment.OnCompleteListener onCompleteListener;
    private AppCompatActivity activity;
    private ShoppingListChildEventListener itemsChildEventListener;
    private BaseShoppingListAdapter adapter;

    public ShoppingListViewModel(AppCompatActivity activity, Intent initData,
                                 ShoppingListDialogFragment.OnCompleteListener onCompleteListener) {
        this.initData = initData;
        this.activity = activity;
        this.onCompleteListener = onCompleteListener;
        listName = new ObservableField<>();
        ownerName = new ObservableField<>();
        dateLastEditedDate = new ObservableField<>();
    }

    public ShoppingListViewModel(ShoppingList shoppingList) {
        listName = new ObservableField<>(shoppingList.getListName());
        ownerName = new ObservableField<>(shoppingList.getOwner());
        dateLastEditedDate = new ObservableField<>(Utils.getFormattedDate(shoppingList.getDateLastChangedLong()));
    }

    public void onStart() {
        key = initData.getStringExtra(Constants.KEY_LIST_ID);
        if (key == null)
            return;

        initFirebaseListeners();

        getFirebase().child(Constants.ACTIVE_LISTS).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null && onShoppingListRemovedListener != null) {
                    onShoppingListRemovedListener.onShoppingListRemoved();
                    return;
                }

                shoppingList = dataSnapshot.getValue(ShoppingList.class);
                listName.set(shoppingList.getListName());
                ownerName.set(shoppingList.getOwner());
                dateLastEditedDate.set(Utils.getFormattedDate(shoppingList.getDateLastChangedLong()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        getFirebase().child(Constants.ACTIVE_LIST_ITEMS).child(key).addChildEventListener(itemsChildEventListener);
    }

    public void onEditListItem(@Nullable String newTitle) {
        Map<String, Object> dateLastChanged = new HashMap<>(1);
        dateLastChanged.put(ShoppingList.DATE_KEY, ServerValue.TIMESTAMP);
        if (newTitle != null)
            shoppingList.setListName(newTitle);
        shoppingList.setDateLastChanged(dateLastChanged);
        getFirebase().child(Constants.ACTIVE_LISTS).child(key).setValue(shoppingList);
    }

    public void onCreateNewListItem(String title) {
        ShoppingListItem shoppingListItem = new ShoppingListItem(title);
        shoppingListItem.setOwner("Anonymous owner");
        getFirebase().child(Constants.ACTIVE_LIST_ITEMS).child(key).push().setValue(shoppingListItem);
    }

    public void onAddNewItemClicked(View view) {
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DIALOG_TYPE, Constants.DIALOG_TYPE_CREATING);
        ShoppingListDialogFragment dialog = ShoppingListDialogFragment.getNewInstance(args);
        dialog.setOnCompleteListener(onCompleteListener);
        dialog.show(activity.getSupportFragmentManager(), ShoppingListDialogFragment.class.getSimpleName());
    }

    public void removeShoppingList() {
        getFirebase().child(Constants.ACTIVE_LISTS).child(key).removeValue();
    }

    public void setAdapter(BaseShoppingListAdapter adapter) {
        this.adapter = adapter;
    }

    public interface OnShoppingListRemovedListener {
        void onShoppingListRemoved();
    }

    public void setOnShoppingListRemovedListener(OnShoppingListRemovedListener onShoppingListRemovedListener) {
        this.onShoppingListRemovedListener = onShoppingListRemovedListener;
    }

    private void initFirebaseListeners() {
        itemsChildEventListener = new ShoppingListChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                adapter.addItem(dataSnapshot, prevKey);
                onEditListItem(null);
            }
        };
    }

}
