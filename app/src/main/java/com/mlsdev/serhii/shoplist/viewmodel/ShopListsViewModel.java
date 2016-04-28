package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.utils.Utils;
import com.mlsdev.serhii.shoplist.view.adapter.ShoppingListsAdapter;

public class ShopListsViewModel extends BaseViewModel {
    public static final String CHILD = "activeList";
    private Context context;
    private ShoppingListsAdapter adapter;


    public static ShopListsViewModel getNewInstance(Context context, ShoppingListsAdapter adapter) {
        return new ShopListsViewModel(context, adapter);
    }

    public ShopListsViewModel(Context context, ShoppingListsAdapter adapter) {
        this.adapter = adapter;
        this.context = context;
        initFirebaseListener();
    }

    public void addNewShopList(String title) {
        getFirebase().child(CHILD).push().setValue(new ShoppingList("Anonymous owner", title));
    }

    private void initFirebaseListener() {
        Firebase firebase = getFirebase().child(CHILD);
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String key) {
                Log.d("FIREBASE", dataSnapshot.toString());
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                adapter.addItem(shoppingList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String key) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String key) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
