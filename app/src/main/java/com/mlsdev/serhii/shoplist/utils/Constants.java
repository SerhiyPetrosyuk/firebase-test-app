package com.mlsdev.serhii.shoplist.utils;

import com.mlsdev.serhii.shoplist.BuildConfig;

public class Constants {
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String EXTRA_LIST_ITEM_TITLE = "extra_new_item_title";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "dateLastChanged";
    public static final String KEY_LIST_NAME = "key_list_name";
    public static final String KEY_LIST_ID = "key_list_id";
    public static final String ACTIVE_LISTS = "active_lists";
    public static final int DIALOG_TYPE_CREATING = 0;
    public static final int DIALOG_TYPE_EDITING = 1;
    public static final int DIALOG_TYPE_REMOVE = 2;
    public static final String EXTRA_DIALOG_TYPE = "extra_dialog_type";
}
