package com.mlsdev.serhii.shoplist.viewmodel;

import com.firebase.client.Firebase;
import com.mlsdev.serhii.shoplist.utils.Constants;

public abstract class BaseViewModel {

    protected Firebase getFirebase() {
        return new Firebase(Constants.FIREBASE_URL);
    }

}
