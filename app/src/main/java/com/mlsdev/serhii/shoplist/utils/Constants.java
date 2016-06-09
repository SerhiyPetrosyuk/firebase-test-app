package com.mlsdev.serhii.shoplist.utils;

import com.mlsdev.serhii.shoplist.BuildConfig;

public class Constants {
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String USER_MODEL_KEY = "user_model_key";
    public static final String EXTRA_LIST_ITEM_TITLE = "extra_new_item_title";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "dateLastChanged";
    public static final String KEY_LIST_NAME = "key_list_name";
    public static final String KEY_LIST_ID = "key_list_id";
    public static final String ACTIVE_LISTS = "active_lists";
    public static final String ACTIVE_LIST_ITEMS = "active_list_items";
    public static final String USER = "user";
    public static final int DIALOG_TYPE_CREATING = 0;
    public static final int DIALOG_TYPE_EDITING = 1;
    public static final int DIALOG_TYPE_REMOVE = 2;
    public static final int DIALOG_TYPE_EDITING_ITEM = 3;
    public static final int DIALOG_TYPE_REMOVE_ITEM = 4;
    public static final String EXTRA_DIALOG_TYPE = "extra_dialog_type";
    public static final String SESSION_TOKEN_KEY = "session_token";
    public static final String SESSION_EXPIRES_KEY = "session_expires";
}
