package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.model.listeners.ShoppingListChildEventListener;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.adapter.ShoppingListsAdapter;

public class ShopListsViewModel extends BaseViewModel {
    private Context context;
    private ShoppingListsAdapter adapter;
    private ShoppingListChildEventListener shoppingListChildEventListener;

    public static ShopListsViewModel getNewInstance(Context context, ShoppingListsAdapter adapter) {
        return new ShopListsViewModel(context, adapter);
    }

    public ShopListsViewModel(Context context, ShoppingListsAdapter adapter) {
        this.adapter = adapter;
        this.context = context;
        initFirebaseListener();
    }

    public void addNewShopList(String title) {
        getFirebase().child(Constants.ACTIVE_LISTS).push().setValue(new ShoppingList("Anonymous owner", title));
    }

    private void initFirebaseListener() {
        Firebase firebase = getFirebase().child(Constants.ACTIVE_LISTS);
        shoppingListChildEventListener = new ShoppingListChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String key) {
                adapter.addItem(dataSnapshot, key);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String key) {
                adapter.onItemChanged(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.onItemRemoved(dataSnapshot.getKey());
            }

        };

        firebase.addChildEventListener(shoppingListChildEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFirebase().removeEventListener(shoppingListChildEventListener);
    }
}
