package com.mlsdev.serhii.shoplist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class ShoppingList {
    public static final String DATE_KEY = "date";
    private String owner;
    private String listName;
    private Map<String, Object> dateCreated;
    private Map<String, Object> dateLastChanged;

    public ShoppingList() {
    }

    public ShoppingList(String owner, String listName) {
        this.owner = owner;
        this.listName = listName;
        dateLastChanged = new HashMap<>(1);
        dateLastChanged.put(DATE_KEY, ServerValue.TIMESTAMP);
    }

    public String getOwner() {
        return owner;
    }

    public String getListName() {
        return listName;
    }

    public Map<String, Object> getDateLastChanged() {
        return dateLastChanged;
    }

    public Map<String, Object> getDateCreated() {
        if (dateCreated == null) {
            dateCreated = new HashMap<>(1);
            dateCreated.put(DATE_KEY, ServerValue.TIMESTAMP);
        }
        return dateCreated;
    }

    @JsonIgnore
    public long getDateLastChangedLong() {
        return (long) dateLastChanged.get(DATE_KEY);
    }

    @JsonIgnore
    public long getDateCreatedLong() {
        return (long) dateCreated.get(DATE_KEY);
    }
}
