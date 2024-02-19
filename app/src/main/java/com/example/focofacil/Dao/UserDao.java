package com.example.focofacil.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.focofacil.Bd.Tarefa;
import com.example.focofacil.Bd.User;
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
    long insert(User user);

    @Insert
    void insertAll (List<User> user);
    @Update
    void update(User user);
    @Delete
    void delete(User user);

    default void insertUserToFirebase(User user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");
        String userId = usersRef.push().getKey();
        usersRef.child(userId).setValue(user);
    }

    @Query("SELECT * FROM User")
    List<User> getAllUsersFromFirebase();

    @Update
    void updateUserInFirebase(User user);

    @Delete
    void deleteUserFromFirebase(User user);

    // Métodos para a entidade Tarefa
    @Query("SELECT * FROM Tarefa")
    List<Tarefa> getAllTarefas();

    @Insert
    long insertTarefa(Tarefa tarefa);

    @Update
    void updateTarefa(Tarefa tarefa);

    @Delete
    void deleteTarefa(Tarefa tarefa);


}
