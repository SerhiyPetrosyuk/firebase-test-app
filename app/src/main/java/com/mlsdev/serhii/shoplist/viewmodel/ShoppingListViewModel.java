package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.utils.Utils;

public class ShoppingListViewModel extends BaseViewModel {
    public final ObservableField<String> listName;
    public final ObservableField<String> ownerName;
    public final ObservableField<String> dateLastEditedDate;
    public Intent initData;

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
        String listKey = initData.getStringExtra(Constants.KEY_LIST_ID);
        if (listKey == null)
            return;

        getFirebase().child(Constants.ACTIVE_LISTS).child(listKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                listName.set(shoppingList.getListName());
                ownerName.set(shoppingList.getOwner());
                dateLastEditedDate.set(Utils.getFormattedDate(shoppingList.getDateLastChangedLong()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
