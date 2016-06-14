package com.mlsdev.serhii.shoplist.model;

import com.google.firebase.database.Exclude;

/**
 * Created by serhii on 5/4/16.
 */
public class ShoppingListItem {
    public static final String IS_BOUGHT_KEY = "bought";
    @Exclude
    private String key;
    private String title;
    private ItemUser owner;
    private ItemUser buyer;
    private boolean bought;

    public ShoppingListItem() {
    }

    public ShoppingListItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public ItemUser getOwner() {
        return owner;
    }

    public ItemUser getBuyer() {
        return buyer;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public boolean getBought() {
        return bought;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(ItemUser owner) {
        this.owner = owner;
    }

    public void setBuyer(ItemUser buyer) {
        this.buyer = buyer;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
