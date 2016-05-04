package com.mlsdev.serhii.shoplist.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.mlsdev.serhii.shoplist.model.ShoppingListItem;
import com.mlsdev.serhii.shoplist.view.adapter.BaseShoppingListAdapter;

/**
 * Created by serhii on 5/4/16.
 */
public class ShoppingListItemViewModel extends BaseViewModel {
    public final ObservableField<String> title;
    private BaseShoppingListAdapter adapter;

    public ShoppingListItemViewModel() {
        title = new ObservableField<>();
    }

    public ShoppingListItemViewModel(ShoppingListItem shoppingListItem) {
        this.title = new ObservableField<>(shoppingListItem.getTitle());
    }

    public void onRemoveItemClicked(View view) {
        // TODO: 5/4/16 implement the item removing
    }

}
