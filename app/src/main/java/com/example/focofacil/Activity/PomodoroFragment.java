package com.example.focofacil.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.focofacil.R;

public class PomodoroFragment extends Fragment {

    // Declaração de variáveis
    private TextView timerTextView;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 25 * 60 * 1000; // Tempo de foco inicial
    private long workTimeInMillis = 25 * 60 * 1000; // Tempo de foco
    private long breakTimeInMillis = 5 * 60 * 1000; // Tempo de pausa
    private int cyclesCompleted = 0;
    private int progress = 0; // Progresso da barra de progresso

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inicialização dos elementos de interface do usuário
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        timerTextView = view.findViewById(R.id.timerTextView);
        startButton = view.findViewById(R.id.startButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        resetButton = view.findViewById(R.id.resetButton);
        progressBar = view.findViewById(R.id.progressBar);

        // Configuração dos listeners dos botões
        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());

        updateCountdownText();

        return view;
    }

    private void startTimer() {
        // Lógica do timer
        if (cyclesCompleted == 4) {
            timeLeftInMillis = 15 * 60 * 1000; // Tempo de pausa estendido
        } else if (cyclesCompleted % 2 == 0) {
            timeLeftInMillis = workTimeInMillis; // Tempo de foco
        } else {
            timeLeftInMillis = breakTimeInMillis; // Tempo de pausa
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
                updateProgressBar(); // Atualizar ProgressBar a cada tick
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updateCountdownText();
                cyclesCompleted++;
                startTimer(); // Iniciar próximo ciclo
            }
        }.start();

        isTimerRunning = true;
        updateCountdownText();
    }

    private void pauseTimer() {
        // Lógica de pausa do timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
            updateCountdownText();
            updateProgressBar(); // Atualizar ProgressBar ao pausar
        }
    }

    private void resetTimer() {
        // Lógica de reinício do timer
        timeLeftInMillis = workTimeInMillis;
        cyclesCompleted = 0;
        updateCountdownText();
        updateProgressBar(); // Atualizar ProgressBar ao reiniciar
    }

    private void updateCountdownText() {
        // Lógica de atualização do texto do timer
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

    private void updateProgressBar() {
        // Lógica de atualização da barra de progresso
        if (cyclesCompleted % 2 == 0) {
            progress = (int) (((float) (workTimeInMillis - timeLeftInMillis) / workTimeInMillis) * 100);
        } else {
            progress = (int) (((float) (breakTimeInMillis - timeLeftInMillis) / breakTimeInMillis) * 100);
        }
        progressBar.setProgress(progress);
    }
}