package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.utils.Utils;

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


    public ShoppingListViewModel(Intent initData) {
        this.initData = initData;
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
    }

    public void onEditListTitle(String newTitle) {
        Map<String, Object> dateLastChanged = new HashMap<>(1);
        dateLastChanged.put(ShoppingList.DATE_KEY, ServerValue.TIMESTAMP);
        shoppingList.setListName(newTitle);
        shoppingList.setDateLastChanged(dateLastChanged);
        getFirebase().child(Constants.ACTIVE_LISTS).child(key).setValue(shoppingList);
    }

    public void removeShoppingList() {
        getFirebase().child(Constants.ACTIVE_LISTS).child(key).removeValue();
    }

    public interface OnShoppingListRemovedListener {
        void onShoppingListRemoved();
    }

    public void setOnShoppingListRemovedListener(OnShoppingListRemovedListener onShoppingListRemovedListener) {
        this.onShoppingListRemovedListener = onShoppingListRemovedListener;
    }

}
