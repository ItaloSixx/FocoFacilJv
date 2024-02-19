package com.example.focofacil.Dao;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.focofacil.Model.Tarefa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    default void insertTarefaToFirebase(Tarefa tarefa) {
        DatabaseReference tarefasRef = FirebaseDatabase.getInstance().getReference("Tarefa");
        String tarefaId = tarefasRef.push().getKey();
        tarefasRef.child(tarefaId).setValue(tarefa);
    }
    default void deleteTarefaFromFirebase(String tarefaId) {
        DatabaseReference tarefasRef = FirebaseDatabase.getInstance().getReference("Tarefa");
        tarefasRef.child(tarefaId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Tarefa excluída com sucesso
                        Log.d(TAG, "Tarefa excluída do Firebase: " + tarefaId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocorreu um erro ao excluir a tarefa
                        Log.e(TAG, "Erro ao excluir tarefa do Firebase: " + e.getMessage());
                    }
                });
    }



    @Query("SELECT * FROM Tarefa")
    List<Tarefa> getAllTarefasFromFirebase();

    @Update
    void updateTarefaInFirebase(Tarefa tarefa);

    @Delete
    void deleteTarefaFromFirebase(Tarefa Tarefa);

}
