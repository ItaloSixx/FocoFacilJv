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

    // Métodos para a entidade Preferencias
    @Query("SELECT * FROM Preferencias")
    List<Preferencias> getAllPreferencias();

    @Insert
    long insertPreferencias(Preferencias preferencias);

    @Update
    void updatePreferencias(Preferencias preferencias);

    @Delete
    void deletePreferencias(Preferencias preferencias);

    @Query("SELECT * FROM Preferencias LIMIT 1")
    Preferencias getPreferencias();

}
