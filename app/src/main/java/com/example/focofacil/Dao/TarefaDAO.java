package com.example.focofacil.Dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.focofacil.Bd.DatabaseHelper;
import com.example.focofacil.Model.TarefaModel;
import com.example.focofacil.contracts.TarefaContract;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public TarefaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void abrir() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void fechar() {
        dbHelper.close();
    }

    public long inserirTarefa(TarefaModel tarefa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID_USUARIO, tarefa.getIdUsuario());
        values.put(DatabaseHelper.COLUMN_TITULO, tarefa.getTitulo());
        values.put(DatabaseHelper.COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(DatabaseHelper.COLUMN_REPETICAO, tarefa.getRepeticao());
        values.put(DatabaseHelper.COLUMN_DIA, tarefa.getDia());
        values.put(DatabaseHelper.COLUMN_MES, tarefa.getMes());
        values.put(DatabaseHelper.COLUMN_ANO, tarefa.getAno());
        values.put(DatabaseHelper.COLUMN_HORA, tarefa.getHora());
        values.put(DatabaseHelper.COLUMN_MINUTO, tarefa.getMinuto());

        long newRowId = db.insert(DatabaseHelper.TABLE_TAREFAS, null, values);
        return newRowId;
    }

    public List<TarefaModel> listarTarefas() {
        List<TarefaModel> tarefas = new ArrayList<>();
        String[] projection = {
                TarefaContract.TarefaEntry._ID,
                TarefaContract.TarefaEntry.COLUMN_TITULO,
                TarefaContract.TarefaEntry.COLUMN_DESCRICAO,
                // Adicione outras colunas conforme necessário
        };

        Cursor cursor = db.query(
                TarefaContract.TarefaEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                TarefaModel tarefa = new TarefaModel();
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_TITULO)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_DESCRICAO)));
                // Configure outros atributos da tarefa
                tarefas.add(tarefa);
            }
            cursor.close();
        }

        return tarefas;
    }

    public TarefaModel buscarTarefaPorId(long id) {
        String[] projection = {
                TarefaContract.TarefaEntry._ID,
                TarefaContract.TarefaEntry.COLUMN_TITULO,
                TarefaContract.TarefaEntry.COLUMN_DESCRICAO,
                TarefaContract.TarefaEntry.COLUMN_ANO,
                TarefaContract.TarefaEntry.COLUMN_MES,
                TarefaContract.TarefaEntry.COLUMN_DIA,
                TarefaContract.TarefaEntry.COLUMN_HORA,
                TarefaContract.TarefaEntry.COLUMN_MINUTO
        };

        String selection = TarefaContract.TarefaEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                TarefaContract.TarefaEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        TarefaModel tarefa = null;
        if (cursor != null && cursor.moveToFirst()) {
            tarefa = new TarefaModel();
            tarefa.setIdUsuario(String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry._ID))));
            tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_TITULO)));
            tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_DESCRICAO)));
            tarefa.setAno(cursor.getInt(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_ANO)));
            tarefa.setMes(cursor.getInt(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_MES)));
            tarefa.setDia(cursor.getInt(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_DIA)));
            tarefa.setHora(cursor.getInt(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_HORA)));
            tarefa.setMinuto(cursor.getInt(cursor.getColumnIndexOrThrow(TarefaContract.TarefaEntry.COLUMN_MINUTO)));

            cursor.close();
        }

        return tarefa;
    }
}
