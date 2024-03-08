package com.example.focofacil.Dao;

import android.net.Uri;

import com.example.focofacil.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserDao {
    FirebaseAuth auth;
    public void cadastrarUsuario(User user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");
        String userId = usersRef.push().getKey();
        usersRef.child(userId).setValue(user);
    }

}
