package com.example.focofacil.Dao;

import static android.content.ContentValues.TAG;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.focofacil.Model.Tarefa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TarefaDao {

    public void insertTarefaToFirebase(Tarefa tarefa) {
        DatabaseReference tarefasRef = FirebaseDatabase.getInstance().getReference("Tarefa");
        String tarefaId = tarefasRef.push().getKey();
        tarefasRef.child(tarefaId).setValue(tarefa);
    }
    public void deleteTarefaFromFirebase(String tarefaId) {
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


}
