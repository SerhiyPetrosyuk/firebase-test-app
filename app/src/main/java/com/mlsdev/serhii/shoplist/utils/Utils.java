package com.mlsdev.serhii.shoplist.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mlsdev.serhii.shoplist.model.ItemUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Utils {

    public static void hideSoftKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(long time) {
        time *= 1000;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat.format(calendar.getTime());
    }

    public static long getCurrentDateTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static boolean isUserListOrItemOwner(String listOwnerEmail, String currentUserEmail) {
        if (listOwnerEmail == null || currentUserEmail == null)
            throw new IllegalArgumentException("Neither the list owner's email nor the current user's email " +
                    "can't be null");
        return listOwnerEmail.equals(currentUserEmail);
    }

    public static String getBuyerName(ItemUser currentUser, ItemUser buyer) {
        if (currentUser == null || buyer == null)
            throw new IllegalArgumentException("Neither the list owner's email nor the current user's email " +
                    "can't be null");
        return isUserListOrItemOwner(currentUser.getEmail(), buyer.getEmail()) ? "You" : buyer.getName();
    }

    public static boolean isUserInShop(List<ItemUser> usersInShop, String currentUserEmail) {
        if (usersInShop == null || currentUserEmail == null)
            throw new IllegalArgumentException("Neither the list user's email nor the current user's email " +
                    "can't be null");

        for (ItemUser userInShop : usersInShop)
            if (currentUserEmail.equals(userInShop.getEmail()))
                return true;

        return false;
    }
}
