package com.mlsdev.serhii.shoplist.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.mlsdev.serhii.shoplist.utils.Utils;

@IgnoreExtraProperties
public class ShoppingList {
    public static final String DATE_KEY = "date";
    private String owner;
    private String listName;
    private long dateCreated;
    private long dateLastChanged;

    public ShoppingList() {
    }

    public ShoppingList(String owner, String listName) {
        this.owner = owner;
        this.listName = listName;
        dateLastChanged = Utils.getCurrentDateTime();
    }

    public String getOwner() {
        return owner;
    }

    public String getListName() {
        return listName;
    }


    public long getDateLastChanged() {
        return dateLastChanged;
    }

    public long getDateCreated() {
        if (dateCreated == 0) dateCreated = Utils.getCurrentDateTime();
        return dateCreated;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateLastChanged(long dateLastChanged) {
        this.dateLastChanged = dateLastChanged;
    }
}
