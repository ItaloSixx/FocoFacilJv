package com.example.focofacil.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.focofacil.R;

public class MeuServico extends Service {

    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "task_notification_channel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Configurar a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Meu Serviço em Execução")
                .setContentText("O serviço está em execução em primeiro plano")
                .setSmallIcon(R.drawable.ic_notification);

        startForeground(NOTIFICATION_ID, builder.build());

        // Fazer o trabalho em segundo plano aqui

        // Retornar START_STICKY para que o serviço seja reiniciado automaticamente
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

