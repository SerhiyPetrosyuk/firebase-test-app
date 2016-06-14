package com.mlsdev.serhii.shoplist.model;

/**
 * Created by serhii on 6/13/16.
 */
public class ItemUser {
    private String name;
    private String email;

    public ItemUser() {
    }

    public ItemUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
