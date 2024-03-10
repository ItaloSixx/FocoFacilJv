package com.example.focofacil.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WeeklyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra("task_title");
        TaskNotificationHelper.showWeeklyNotification(context, taskTitle);
    }
}
