package com.example.focofacil.Dao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.focofacil.Bd.DatabaseHelper;
import com.example.focofacil.Model.Tarefa;

import java.util.List;

public class TarefaSQLiteDAO implements TarefaDAO {
    private SQLiteDatabase db;

    public TarefaSQLiteDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public List<Tarefa> listarTarefas() {
        // Implemente a lógica para listar as tarefas do banco de dados SQLite
        return null;
    }

    @Override
    public Tarefa buscarTarefaPorId(int id) {
        // Implemente a lógica para buscar uma tarefa por ID no banco de dados SQLite
        return null;
    }

    @Override
    public void inserirTarefa(Tarefa tarefa) {
        // Implemente a lógica para inserir uma tarefa no banco de dados SQLite
    }

    @Override
    public void atualizarTarefa(Tarefa tarefa) {
        // Implemente a lógica para atualizar uma tarefa no banco de dados SQLite
    }

    @Override
    public void deletarTarefa(int id) {
        // Implemente a lógica para deletar uma tarefa do banco de dados SQLite
    }
}
