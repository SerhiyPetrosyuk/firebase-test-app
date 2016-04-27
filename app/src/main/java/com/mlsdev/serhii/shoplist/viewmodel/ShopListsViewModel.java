package com.mlsdev.serhii.shoplist.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mlsdev.serhii.shoplist.model.ShoppingList;
import com.mlsdev.serhii.shoplist.utils.Utils;

public class ShopListsViewModel extends BaseViewModel {
    public static final String CHILD = "activeList";
    private Context context;
    public final ObservableField<String> listName;
    public final ObservableField<String> ownerName;
    public final ObservableField<String> dateLastEditedDate;

    public static ShopListsViewModel getNewInstance(Context context) {
        return new ShopListsViewModel(context);
    }

    public ShopListsViewModel(Context context) {
        listName = new ObservableField<>("TEST");
        ownerName = new ObservableField<>();
        dateLastEditedDate = new ObservableField<>();

        this.context = context;
        Firebase firebase = getFirebase().child(CHILD);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                if (shoppingList != null) {
                    listName.set(shoppingList.getListName());
                    ownerName.set(shoppingList.getOwner());
                    dateLastEditedDate.set(Utils.getFormattedDate(shoppingList.getDateLastChangedLong()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addNewShopList(String title) {

        getFirebase().child(CHILD).setValue(new ShoppingList("Anonymous owner", title));
    }

}
