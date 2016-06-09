package com.mlsdev.serhii.shoplist.viewmodel;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseViewModel {
    protected DatabaseReference databaseReference;

    public BaseViewModel() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void onDestroy() {

    }

    public void onStart() {

    }

    public void onStop() {

    }

    public void onComplete(Bundle resultData) {

    }

}
