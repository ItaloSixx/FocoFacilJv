package com.example.focofacil.BroadcastReceiver;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.example.focofacil.R;


public class TaskNotificationHelper {
    public static final String CHANNEL_ID = "FocoFacilChannel";
    public static final int NOTIFICATION_ID = 123;

    public static void scheduleNotification(Context context, String taskTitle, long notificationTimeInMillis) {
        Log.d("TaskNotificationHelper", "Agendando notificação para: " + notificationTimeInMillis);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Task Notifications", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        Log.d("TaskNotificationHelper", "Canal de notificação criado");

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("task_title", taskTitle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Configurar o AlarmManager para disparar a notificação no tempo especificado
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTimeInMillis, pendingIntent);
        Log.d("TaskNotificationHelper", "Notificação agendada com sucesso.");
    }

    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String taskTitle = intent.getStringExtra("task_title");
            showNotification(context, taskTitle);
        }
    }

        static void showNotification(Context context, String taskTitle) {
            Log.d("TaskNotificationHelper", "Mostrando notificação para a tarefa: " + "taskTitle");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.img_logo)
                    .setContentTitle("Sua Tarefa o Espera em 5 Minutos")
                    .setContentText("Sua tarefa está chegando em breve, não perca!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            Log.d("TaskNotificationHelper", "Notificação mostrada com sucesso.");
        }


    }

