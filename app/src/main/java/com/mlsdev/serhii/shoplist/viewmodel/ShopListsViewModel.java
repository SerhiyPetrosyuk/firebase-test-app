package com.mlsdev.serhii.shoplist.viewmodel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.model.User;
import com.mlsdev.serhii.shoplist.model.UserSession;
import com.mlsdev.serhii.shoplist.model.listeners.ShoppingListChildEventListener;
import com.mlsdev.serhii.shoplist.utils.Constants;
import com.mlsdev.serhii.shoplist.view.activity.IShopListsView;
import com.mlsdev.serhii.shoplist.view.activity.MainActivity;
import com.mlsdev.serhii.shoplist.view.adapter.ShoppingListsAdapter;

public class ShopListsViewModel extends BaseViewModel {
    private IShopListsView view;
    private ShoppingListsAdapter adapter;
    private ShoppingListChildEventListener shoppingListChildEventListener;
    private User user;

    public static ShopListsViewModel getNewInstance(IShopListsView view, ShoppingListsAdapter adapter) {
        return new ShopListsViewModel(view, adapter);
    }

    public ShopListsViewModel(IShopListsView view, ShoppingListsAdapter adapter) {
        this.view = view;
        this.adapter = adapter;
        initFirebaseListener();
        initUser();
    }

    public void addNewShopList(String title) {
        ShoppingList shoppingList = new ShoppingList(user.getName(), user.getEmail(), title);
        FirebaseDatabase.getInstance().getReference().child(Constants.ACTIVE_LISTS)
                .push()
                .setValue(shoppingList);
    }

    private void initUser() {
        user = UserSession.getInstance().getUser(view.getViewContext());
        if (user == null) {
            ((MainActivity) view.getViewActivity()).logUserOut();
            return;
        }

        view.setTitle(user.getName());
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
