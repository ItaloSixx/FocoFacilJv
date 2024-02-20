package com.example.focofacil.Bd;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigureBd {
    private static FirebaseAuth auth;

    public static FirebaseAuth FirebaseAutenticar(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
    public static void FirebasePersistirOffline() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
