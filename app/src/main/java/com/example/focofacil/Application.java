package com.example.focofacil;

import com.example.focofacil.Bd.ConfigureBd;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigureBd.FirebasePersistirOffline();
    }

}
