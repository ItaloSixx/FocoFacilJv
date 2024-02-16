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
}
