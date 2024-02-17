package com.example.focofacil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setupDayClickListener(R.id.txtSegunda, "Segunda");
        setupDayClickListener(R.id.txtTerca, "Terça");
        setupDayClickListener(R.id.txtQuarta, "Quarta");
        setupDayClickListener(R.id.txtQuinta, "Quinta");
        setupDayClickListener(R.id.txtSexta, "Sexta");
        setupDayClickListener(R.id.txtSabado, "Sábado");
        setupDayClickListener(R.id.txtDomingo, "Domingo");
    }

    private void setupDayClickListener(int textViewId, final String diaDaSemana) {
        TextView textView = findViewById(textViewId);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar a DetalhesDia e passar o nome do dia como extra
                Intent intent = new Intent(HomeActivity.this, DetalhesDiaActivity.class);
                intent.putExtra("diaDaSemana", diaDaSemana);
                startActivity(intent);
            }
        });
    }*/

    private ArrayList<DiaDaSemana> listaDeDias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Criar instâncias de DiaDaSemana e adicioná-las à listaDeDias
        listaDeDias = new ArrayList<>();
        setupDiasDaSemana();
    }

    private void setupDiasDaSemana() {
        // Obter o dia atual da semana
        Calendar calendar = Calendar.getInstance();
        int diaAtual = calendar.get(Calendar.DAY_OF_WEEK);

        // Iterar pelos dias da semana
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            // Configurar o dia da semana
            DiaDaSemana dia = new DiaDaSemana(obterNomeDia(i), obterListaDeTarefas());
            listaDeDias.add(dia);

            // Configurar o TextView correspondente ao dia
            int textViewId = getResources().getIdentifier("txt" + dia.getNomeDia(), "id", getPackageName());
            TextView textView = findViewById(textViewId);

            // Exibir o nome do dia e suas tarefas no TextView
            textView.setText(dia.getNomeDia());

            // Configurar o OnClickListener para o TextView correspondente ao dia
            configurarOnClickListener(textView, dia);

            // Exibir as informações de cada tarefa nos TextViews específicos
            exibirInformacoesTarefas(dia);
        }
    }

    private void exibirInformacoesTarefas(DiaDaSemana dia) {
        // Suponha que você tenha TextViews para cada atributo de Tarefa
        //TextView txtDescricao = findViewById(R.id.txtDescricao);
        TextView txtAssunto = findViewById(R.id.txtAssunto);
        TextView txtDataHora = findViewById(R.id.txtDataHora);

        // Limpar os TextViews
        //txtDescricao.setText("");
        txtAssunto.setText("");
        txtDataHora.setText("");

        // Exibir informações da última tarefa (caso haja mais de uma)
        for (Tarefa tarefa : dia.getListaDeTarefas()) {
            // Preencher os TextViews com as informações da tarefa
            //txtDescricao.setText(tarefa.getDescricao());
            txtAssunto.setText(tarefa.getAssunto());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            txtDataHora.setText(sdf.format(tarefa.getDataHora()));
        }
    }

    private ArrayList<Tarefa> obterListaDeTarefas() {
        // Retorna uma lista fictícia de tarefas para cada dia
        // Você deve adaptar isso conforme a lógica do seu aplicativo
        ArrayList<Tarefa> listaDeTarefas = new ArrayList<>();
        listaDeTarefas.add(new Tarefa("Tarefa 1", "Assunto 1", new Date()));
        listaDeTarefas.add(new Tarefa("Tarefa 2", "Assunto 2", new Date()));
        // Adicione mais tarefas conforme necessário
        return listaDeTarefas;
    }

    private String obterNomeDia(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date(calendar.getTimeInMillis()));
    }

    private void configurarOnClickListener(TextView textView, final DiaDaSemana dia) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar a DetalhesDia e passar o nome do dia e a lista de tarefas como extras
                Intent intent = new Intent(HomeActivity.this, DetalhesDiaActivity.class);
                intent.putExtra("diaDaSemana", dia.getNomeDia());
                intent.putExtra("listaDeTarefas", dia.getListaDeTarefas());
                startActivity(intent);
            }
        });
    }
}