package com.mlsdev.serhii.shoplist.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.mlsdev.serhii.shoplist.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class ShoppingList {
    private String ownerName;
    private String ownerEmail;
    private String listName;
    private long dateCreated;
    private long dateLastChanged;
    private List<ItemUser> usersInShop = new ArrayList<>();

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

    public List<ItemUser> getUsersInShop() {
        return usersInShop;
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

    public void setUsersInShop(List<ItemUser> usersInShop) {
        this.usersInShop = usersInShop;
    }

    public void addUserToShop(String usersEmail, String userName) {
        for (ItemUser user : usersInShop)
            if (user.getEmail().equals(usersEmail))
                return;

        ItemUser shopper = new ItemUser(userName, usersEmail);
        usersInShop.add(shopper);
    }

    public void removeUserFromShop(String userEmail) {
        for (ItemUser user : usersInShop)
            if (user.getEmail().equals(userEmail)) {
                usersInShop.remove(user);
                break;
            }
    }

    public String getShoppers(String currentUserEmail) {
        String shoppers = "";
        boolean isCurrentUserShopper = Utils.isUserInShop(usersInShop, currentUserEmail);

        if (usersInShop.size() == 1 && !isCurrentUserShopper) {
            shoppers += usersInShop.get(0).getName() + " is shopping";
        } else if (usersInShop.size() == 2 && !isCurrentUserShopper) {
            shoppers += usersInShop.get(0).getName() + " and " + usersInShop.get(1).getName() + " are shopping";
        } else if (usersInShop.size() == 2 && isCurrentUserShopper) {
            for (ItemUser user : usersInShop)
                if (!user.getEmail().equals(currentUserEmail)) {
                    shoppers += user.getName() + " is shopping";
                    break;
                }
        } else if ((usersInShop.size() > 2 && !isCurrentUserShopper) || (usersInShop.size() > 3 && isCurrentUserShopper)) {
            shoppers += "More than two users are shopping";
        } else if (usersInShop.size() == 3 && isCurrentUserShopper) {
            List<ItemUser> shopperList = new ArrayList<>();
            shopperList.addAll(usersInShop);
            for (ItemUser user : shopperList)
                if (user.getEmail().equals(currentUserEmail)) {
                    shopperList.remove(user);
                    break;
                }
            shoppers += shopperList.get(0).getName() + " and " + shopperList.get(1).getName() + " are shopping";
        }

        return shoppers;
    }
}
