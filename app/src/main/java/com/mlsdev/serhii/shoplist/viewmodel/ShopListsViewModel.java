package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
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
        ShoppingList shoppingList = new ShoppingList("Anonymous owner", title);
        FirebaseDatabase.getInstance().getReference().child(Constants.ACTIVE_LISTS)
                .push()
                .setValue(shoppingList);
    }

    private void initFirebaseListener() {
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

        FirebaseDatabase.getInstance().getReference().child(Constants.ACTIVE_LISTS)
                .addChildEventListener(shoppingListChildEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().removeEventListener(shoppingListChildEventListener);
    }
}
