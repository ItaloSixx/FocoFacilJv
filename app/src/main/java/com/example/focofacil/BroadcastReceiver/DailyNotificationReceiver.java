package com.example.focofacil.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.focofacil.R;


public class DailyNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Aqui você pode lidar com as ações de clique na notificação
        if (intent.getAction() != null && intent.getAction().equals("ACTION_NOTIFICATION_CLICK")) {
            // Ação de clique na notificação
            Toast.makeText(context, "Notificação clicada", Toast.LENGTH_SHORT).show();
        }
    }
}
