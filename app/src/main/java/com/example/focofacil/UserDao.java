package com.example.focofacil;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();
    @Query ("SELECT * FROM User WHERE nome LIKE :txtEmail")
    User findByEmail(String txtEmail);

    @Insert
    long insert(User usuario);

    @Insert
    void insertAll (List<User> usuarios);
    @Update
    void update(User usuario);
    @Delete
    void delete(User usuario);

    default void insertUserToFirebase(User user) {
        // Referência ao seu nó de usuários no Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");

        // Gerando uma chave única para o usuário no Firebase
        String userId = usersRef.push().getKey();

        // Definindo os dados do usuário no nó de usuários do Firebase usando a chave gerada
        usersRef.child(userId).setValue(user);
    }

    // Método para receber dados do Firebase e atualizar o banco de dados local
    @Query("SELECT * FROM User")
    List<User> getAllUsersFromFirebase();

    // Método para atualizar o Firebase com os dados locais
    @Update
    void updateUserInFirebase(User user);

    // Método para excluir um usuário do Firebase
    @Delete
    void deleteUserFromFirebase(User user);
}
