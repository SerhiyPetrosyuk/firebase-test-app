package com.mlsdev.serhii.shoplist.model;

/**
 * Created by serhii on 5/4/16.
 */
public class ShoppingListItem {
    private String title;
    private String owner;
    private String parentKey;

    public ShoppingListItem() {
    }

    public ShoppingListItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }
}
