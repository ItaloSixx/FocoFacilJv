package com.example.focofacil.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.focofacil.Bd.Tarefa;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

@Dao
public interface TarefaDao {
    @Query("SELECT * FROM Tarefa")
    List<Tarefa> getAll();
    @Query ("SELECT * FROM Tarefa WHERE id LIKE :txtEmail")
    Tarefa findById(String txtEmail);

    @Insert
    long insert(Tarefa tarefa);

    @Insert
    void insertAll (List<Tarefa> tarefa);
    @Update
    void update(Tarefa tarefa);
    @Delete
    void delete(Tarefa tarefa);

    default void insertTarefaToFirebase(Tarefa user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Tarefa");
        String userId = usersRef.push().getKey();
        usersRef.child(userId).setValue(user);
    }

    @Query("SELECT * FROM User")
    List<Tarefa> getAllTarefasFromFirebase();

    @Update
    void updateTarefaInFirebase(Tarefa tarefa);

    @Delete
    void deleteTarefaFromFirebase(Tarefa Tarefa);

}
