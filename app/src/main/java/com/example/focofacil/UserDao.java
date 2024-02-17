package com.example.focofacil;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();
    @Query ("SELECT * FROM User WHERE nome LIKE :txtEmail")
    User findByEmail(String txtEmail);

    @Insert
    void insert(User usuario);
    @Insert
    void insertAll (List<User> usuarios);
    @Update
    void update(User usuario);
    @Delete
    void delete(User usuario);

    @Insert
    void insertUserToFirebase(User user);

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
