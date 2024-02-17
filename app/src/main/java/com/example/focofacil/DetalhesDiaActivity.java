package com.example.focofacil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetalhesDiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_dia);

        // Recuperar o nome do dia da semana do Intent
        String diaDaSemana = getIntent().getStringExtra("diaDaSemana");

        // Exibir os dados nos TextViews da DetalhesDiaActivity
        TextView txtDiaDaSemana = findViewById(R.id.txtDiaDaSemana);

        txtDiaDaSemana.setText("Dia da Semana: " + diaDaSemana);
    }
}