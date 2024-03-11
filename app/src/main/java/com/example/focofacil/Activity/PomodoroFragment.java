package com.example.focofacil.Activity;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.focofacil.BroadcastReceiver.AlarmReceiver;
import com.example.focofacil.R;

public class PomodoroFragment extends Fragment {

    private TextView timerTextView;
    private ImageButton startButton;
    private ImageButton pauseButton;
    private ImageButton resetButton;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private MediaPlayer mediaPlayer;
    private long timeLeftInMillis;
    private long workTimeInMillis = 25 * 60 * 1000; //25min
    private long breakTimeInMillis = 5 * 60 * 1000; //5min
    private int cyclesCompleted = 0;
    private static final int ALARM_REPEAT_INTERVAL = 1000;
    private long timeLeftOnPause = 0; // tempo restante quando o timer é pausado

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        timerTextView = view.findViewById(R.id.timerTextView);
        startButton = view.findViewById(R.id.startButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        resetButton = view.findViewById(R.id.resetButton);
        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
        mediaPlayer = MediaPlayer.create(requireContext(),R.raw.alarm_sound);

        updateCountdownText();

        return view;
    }

    private void startTimer() {
        if (!isTimerRunning) {
            //timer pausado -> retome a partir do tempo restante
            if (timeLeftOnPause > 0) {
                timeLeftInMillis = timeLeftOnPause;
            } else {
                if (cyclesCompleted == 4) {
                    return;
                } else if (cyclesCompleted % 2 == 0) {
                    timeLeftInMillis = workTimeInMillis; // 25 minutos de foco
                } else {
                    timeLeftInMillis = breakTimeInMillis; // 5 minutos de pausa
                }
            }

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateCountdownText();
                }

                @Override
                public void onFinish() {
                    isTimerRunning = false;
                    cyclesCompleted++;
                    updateCountdownText();
                    long remainingTimeInMillis = timeLeftInMillis;
                    startTimer(); // Inicia automaticamente o próximo ciclo
                    startAlarm(remainingTimeInMillis); // Dispara o alarme ao fim da contagem
                    showStopAlarmPopup();
                }
            }.start();
            isTimerRunning = true;
            updateCountdownText();
        }
    }
    private void pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            timeLeftOnPause = timeLeftInMillis; // Armazene o tempo restante
            isTimerRunning = false;
            updateCountdownText();
        }
    }
    private void resetTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
        timeLeftInMillis = workTimeInMillis;
        timeLeftOnPause = 0; // Resetar o tempo restante na pausa
        cyclesCompleted = 0;
        updateCountdownText();
    }
    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);

        if (isTimerRunning) {
            pauseButton.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.INVISIBLE);
            resetButton.setVisibility(View.INVISIBLE);
        } else {
            pauseButton.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.VISIBLE);
        }
    }
    private void startAlarm(long remainingTimeInMillis) {
        Log.d(TAG, "startAlarm called");

        if (!isAdded()) {
            Log.e(TAG, "Fragment not attached to activity");
            return;
        }
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        //dispara quando o tempo restante for zero
        long triggerTime = System.currentTimeMillis() + remainingTimeInMillis;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

        // Reinicializa o MediaPlayer com o som do alarme
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm_sound);

        // Verifiqua se o MediaPlayer foi inicializado com sucesso
        if (mediaPlayer != null) {
            try {
                // Repita a reprodução do som do alarme em intervalos regulares
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Erro ao iniciar a reprodução do MediaPlayer: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Falha ao inicializar MediaPlayer para o som do alarme");
        }
    }
    private void showStopAlarmPopup() {
        if (mediaPlayer != null) {
            Log.d(TAG, "stopAlarmPOPUP called");
            AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog)));
            builder.setMessage("Alarme tocando. Deseja parar?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", (dialog, id) -> {
                        Log.d(TAG, "Botão 'Sim' clicado");
                        stopAlarm(mediaPlayer);
                    })
                    .setNegativeButton("Cancelar", (dialog, id) -> {
                        // Cancelar a pop-up
                        dialog.cancel();
                        Log.d(TAG, "stopAlarmPOPUP called");
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Log.e(TAG, "MediaPlayer é nulo");
        }
    }
    private void stopAlarm(MediaPlayer mediaPlayer) {
        Log.d(TAG, "stopAlarmRingtone chamado");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.d(TAG, "Parando o MediaPlayer");
            mediaPlayer.stop();
            mediaPlayer.release();
        } else {
            Log.e(TAG, "MediaPlayer é nulo ou não está reproduzindo");
        }
    }
    public static Ringtone getAlarmRingtone(Context context) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            // Caso o som padrão de alarme não esteja disponível, usa o toque padrão do dispositivo.
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        return RingtoneManager.getRingtone(context, alarmSound);
    }
}
