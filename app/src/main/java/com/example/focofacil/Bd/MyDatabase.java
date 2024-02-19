package com.example.focofacil.Bd;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.focofacil.Dao.TarefaDao;
import com.example.focofacil.Dao.UserDao;
import com.example.focofacil.Model.Preferencias;
import com.example.focofacil.Model.Tarefa;
import com.example.focofacil.Model.User;
import com.example.focofacil.Utils.DateConverter;

@Database (entities = {User.class, Tarefa.class, Preferencias.class}, version = 5, exportSchema =     false)
@TypeConverters({DateConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TarefaDao tarefaDao();
}
