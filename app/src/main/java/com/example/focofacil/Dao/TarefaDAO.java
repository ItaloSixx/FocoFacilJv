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

public interface TarefaDAO {
    List<Tarefa> listarTarefas();
    Tarefa buscarTarefaPorId(int id);
    void inserirTarefa(Tarefa tarefa);
    void atualizarTarefa(Tarefa tarefa);
    void deletarTarefa(int id);
}


