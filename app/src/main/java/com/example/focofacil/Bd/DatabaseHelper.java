package com.example.focofacil.Bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nome_do_banco_de_dados.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela de tarefas
    public static final String TABLE_TAREFAS = "tarefas";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRICAO = "descricao";
    public static final String COLUMN_REPETICAO = "repeticao";
    public static final String COLUMN_DIA = "dia";
    public static final String COLUMN_MES = "mes";
    public static final String COLUMN_ANO = "ano";
    public static final String COLUMN_HORA = "hora";
    public static final String COLUMN_MINUTO = "minuto";
    public static final String COLUMN_ID_USUARIO = "id_usuario";

    // SQL para criar a tabela de tarefas
    private static final String SQL_CREATE_TABLE_TAREFAS = "CREATE TABLE " + TABLE_TAREFAS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_TITULO + " TEXT," +
            COLUMN_DESCRICAO + " TEXT," +
            COLUMN_REPETICAO + " INTEGER," +
            COLUMN_DIA + " INTEGER," +
            COLUMN_MES + " INTEGER," +
            COLUMN_ANO + " INTEGER," +
            COLUMN_HORA + " INTEGER," +
            COLUMN_MINUTO + " INTEGER," +
            COLUMN_ID_USUARIO + " TEXT" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TAREFAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
