package com.example.focofacil.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.focofacil.R;

public class PomodoroFragment extends Fragment {

    private TextView timerTextView;
    private ImageButton startButton;
    private ImageButton pauseButton;
    private ImageButton resetButton;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis;
    private long workTimeInMillis = 25 * 60 * 1000; //25min
    private long breakTimeInMillis = 5 * 60 * 1000; //5min
    private int cyclesCompleted = 0;

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
                    startTimer(); // Inicia automaticamente o próximo ciclo
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
}
