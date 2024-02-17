package com.example.focofacil;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database (entities = {User.class}, version = 1, exportSchema =     false)
@TypeConverters({DateConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
