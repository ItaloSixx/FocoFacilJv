package com.example.focofacil.contracts;

import android.provider.BaseColumns;

public class TarefaContract {
    private TarefaContract() {}

    public static class TarefaEntry implements BaseColumns {
        public static final String TABLE_NAME = "tarefas";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_REPETICAO = "repeticao";
        public static final String COLUMN_DIA = "dia";
        public static final String COLUMN_MES = "mes";
        public static final String COLUMN_ANO = "ano";
        public static final String COLUMN_HORA = "hora";
        public static final String COLUMN_MINUTO = "minuto";
        public static final String COLUMN_ID_USUARIO = "idUsuario";


    }

}