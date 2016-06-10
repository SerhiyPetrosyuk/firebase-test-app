package com.mlsdev.serhii.shoplist.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.mlsdev.serhii.shoplist.utils.Utils;

@IgnoreExtraProperties
public class ShoppingList {
    public static final String DATE_KEY = "date";
    private String ownerName;
    private String ownerEmail;
    private String listName;
    private long dateCreated;
    private long dateLastChanged;

    public ShoppingList() {
    }

    public ShoppingList(String ownerName, String ownerEmail, String listName) {
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.listName = listName;
        dateLastChanged = Utils.getCurrentDateTime();
    }

    public String getOwnerName() {
        return ownerName;
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
