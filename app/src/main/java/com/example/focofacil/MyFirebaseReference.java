package com.example.focofacil;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseReference {

    private DatabaseReference ref;

    public MyFirebaseReference() {
        ref = FirebaseDatabase.getInstance().getReference();
    }
}
