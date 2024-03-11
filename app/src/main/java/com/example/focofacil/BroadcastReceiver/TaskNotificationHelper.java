package com.example.focofacil.BroadcastReceiver;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.focofacil.R;
public class TaskNotificationHelper {
    public static final String CHANNEL_ID = "FocoFacilChannel";
    public static final int NOTIFICATION_ID = 123;
    private MediaPlayer mediaPlayer;

    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String taskTitle = intent.getStringExtra("task_title");
            showNotification(context, taskTitle);
        }
    }
        static void showNotification(Context context, String taskTitle) {
            Log.d("TaskNotificationHelper", "Mostrando notificação para a tarefa: " + "taskTitle");
            Uri soundUri = Settings.System.DEFAULT_NOTIFICATION_URI;

            // Configuração da vibração
            long[] pattern = {0, 1000, 1000}; // Padrão de vibração (inicia com pausa, vibração, pausa, vibração...)
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, -1); // -1 para repetição

            // Obtém o serviço de vibração
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(vibrationEffect); // Inicia a vibração
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.img_logo)
                    .setContentTitle("Sua tarefa o espera em 5 Minutos")
                    .setContentText("Sua tarefa está chegando em breve, não perca!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(soundUri)
                    .setVibrate(pattern);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            Log.d("TaskNotificationHelper", "Notificação mostrada com sucesso.");
        }

    static void showDailyNotification(Context context, String taskTitle) {
        Log.d("TaskNotificationHelper", "Mostrando notificação para a tarefa: " + "taskTitle");

        Uri soundUri = Settings.System.DEFAULT_NOTIFICATION_URI;

        // Configuração da vibração
        long[] pattern = {0, 1000, 1000}; // Padrão de vibração (inicia com pausa, vibração, pausa, vibração...)
        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, -1); // -1 para repetição

        // Obtém o serviço de vibração
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(vibrationEffect); // Inicia a vibração
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_logo)
                .setContentTitle("Bom dia!!")
                .setContentText("Suas tarefas estão esperando para mais um dia")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)
                .setVibrate(pattern);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        Log.d("TaskNotificationHelper", "Notificação mostrada com sucesso.");
    }

    static void showWeeklyNotification(Context context, String taskTitle) {
        Log.d("TaskNotificationHelper", "Mostrando notificação para a tarefa: " + "taskTitle");
        Log.d("TaskNotificationHelper", "Mostrando notificação para a tarefa: " + "taskTitle");

        Uri soundUri = Settings.System.DEFAULT_NOTIFICATION_URI;

        // Configuração da vibração
        long[] pattern = {0, 1000, 1000}; // Padrão de vibração (inicia com pausa, vibração, pausa, vibração...)
        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, -1); // -1 para repetição

        // Obtém o serviço de vibração
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(vibrationEffect); // Inicia a vibração
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_logo)
                .setContentTitle("Bom dia!!")
                .setContentText("Mais uma semana Começando com várias tarefas a serem realizadas")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)
                .setVibrate(pattern);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        Log.d("TaskNotificationHelper", "Notificação mostrada com sucesso.");
    }


    }