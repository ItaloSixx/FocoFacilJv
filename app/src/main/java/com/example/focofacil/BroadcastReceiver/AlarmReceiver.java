package com.example.focofacil.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Exibir uma mensagem para indicar que o alarme foi disparado
        Toast.makeText(context, "Alarme disparado!", Toast.LENGTH_SHORT).show();
    }
}