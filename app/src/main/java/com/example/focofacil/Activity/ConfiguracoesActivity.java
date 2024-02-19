package com.example.focofacil.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Switch;

import com.example.focofacil.Bd.MyDatabase;
import com.example.focofacil.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    Switch notificarDia;
    Switch notificarHorario;
    Switch notificarSemana;
    MyDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        notificarDia = findViewById(R.id.Dia);
        notificarHorario = findViewById(R.id.Horario);
        notificarSemana = findViewById(R.id.Semana);

        // Definindo os Switches como true por padrão
        notificarDia.setChecked(true);
        notificarHorario.setChecked(true);
        notificarSemana.setChecked(true);
    }
}

