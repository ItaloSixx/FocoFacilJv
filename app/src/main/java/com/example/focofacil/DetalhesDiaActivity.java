package com.example.focofacil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetalhesDiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_dia);

        // Recuperar o nome do dia da semana do Intent
        String diaDaSemana = getIntent().getStringExtra("diaDaSemana");
        Date dataSelecionada = (Date) getIntent().getSerializableExtra("dataSelecionada");
        ArrayList<Tarefa> listaDeTarefas = (ArrayList<Tarefa>) getIntent().getSerializableExtra("listaDeTarefas");


        // Exibir os dados nos TextViews da DetalhesDiaActivity
        TextView txtDiaDaSemana = findViewById(R.id.txtDiaDaSemana);
        TextView txtDataSemana = findViewById(R.id.txtDataSemana);

        txtDiaDaSemana.setText("Dia da Semana: " + diaDaSemana);

        // Formatar a data no formato desejado
        SimpleDateFormat formatoData = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault());
        String dataFormatada = formatoData.format(dataSelecionada);

        // Exibir a data no TextView
        txtDataSemana.setText("Data: " + dataFormatada);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout linearLayoutTarefas = findViewById(R.id.linearLayoutTarefas);

        // Iterar sobre a lista de tarefas e criar Views para cada uma
        for (Tarefa tarefa : listaDeTarefas) {
            // Crie um novo LinearLayout para representar uma tarefa
            LinearLayout linearLayoutTarefa = new LinearLayout(this);
            linearLayoutTarefa.setOrientation(LinearLayout.HORIZONTAL);

            // Crie TextViews para exibir informações da tarefa
            TextView txtDescricao = new TextView(this);
            TextView txtTarefaAssunto = new TextView(this);
            TextView txtDataHora = new TextView(this);

            // Defina o texto nas TextViews com base nos atributos da tarefa
            txtDescricao.setText(tarefa.getDescricao());
            txtTarefaAssunto.setText(tarefa.getAssunto());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            txtDataHora.setText(sdf.format(tarefa.getDataHora()));

            // Adicione as TextViews ao LinearLayout da tarefa
            linearLayoutTarefa.addView(txtDescricao);
            linearLayoutTarefa.addView(txtTarefaAssunto);
            linearLayoutTarefa.addView(txtDataHora);

            // Adicione o LinearLayout da tarefa ao layout principal
            linearLayoutTarefas.addView(linearLayoutTarefa);
        }
    }
}