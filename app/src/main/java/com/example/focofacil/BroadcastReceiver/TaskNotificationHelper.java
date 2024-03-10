package com.example.focofacil.BroadcastReceiver;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.focofacil.R;

public class TaskNotificationHelper {
    private static final String CHANNEL_ID = "task_notification_channel";
    private static final int NOTIFICATION_ID = 123;

    public static void scheduleNotification(Context context, String taskTitle, long notificationTimeInMillis) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Criar um canal de notificação para Android Oreo e versões posteriores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Task Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("task_title", taskTitle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Configurar o AlarmManager para disparar a notificação no tempo especificado
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTimeInMillis, pendingIntent);
    }

    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String taskTitle = intent.getStringExtra("task_title");
            showNotification(context, taskTitle);
        }

        private void showNotification(Context context, String taskTitle) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.img_logo)
                    .setContentTitle("Tarefa: " + taskTitle)
                    .setContentText("Sua tarefa está chegando!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
