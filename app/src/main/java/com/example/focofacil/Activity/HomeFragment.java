package com.example.focofacil.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.focofacil.DiaDaSemana;
import com.example.focofacil.R;
import com.example.focofacil.Tarefa;

import java.util.ArrayList;


import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<DiaDaSemana> listaDeDias;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void setupDiasDaSemana(View view) {
        // Obter o dia atual da semana
        Calendar calendar = Calendar.getInstance();
        int diaAtual = calendar.get(Calendar.DAY_OF_WEEK);

        // Iterar pelos dias da semana
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            // Configurar o dia da semana
            DiaDaSemana dia = new DiaDaSemana(obterNomeDia(i), obterListaDeTarefas());


            // Configurar o TextView correspondente ao dia
            int textViewId = getResources().getIdentifier("txt" + dia.getNomeDia(), "id", getContext().getPackageName());
            TextView textView = view.findViewById(textViewId);

            // Calcular a data correspondente ao dia da semana
            Calendar diaSelecionado = Calendar.getInstance();
            diaSelecionado.set(Calendar.DAY_OF_WEEK, i);

            // Adicionar essa data como extra para a DetalhesDiaActivity
            dia.setDataSelecionada(diaSelecionado.getTime());
            listaDeDias.add(dia);

            // Exibir o nome do dia e suas tarefas no TextView
            textView.setText(dia.getNomeDia());

            // Configurar o OnClickListener para o TextView correspondente ao dia
            configurarOnClickListener(textView, dia);

            // Exibir as informações de cada tarefa nos TextViews específicos
            exibirInformacoesTarefas(dia, view);
        }
    }

    private void exibirInformacoesTarefas(DiaDaSemana dia, View view) {
        // Suponha que você tenha TextViews para cada atributo de Tarefa
        //TextView txtDescricao = findViewById(R.id.txtDescricao);
        TextView txtAssunto = view.findViewById(R.id.txtAssunto);
        TextView txtDataHora = view.findViewById(R.id.txtDataHora);

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
                DetalhesDiaFragment detalhesDiaFragment = DetalhesDiaFragment.newInstance(dia);

                // Substituir o fragmento atual pelo DetalhesDiaFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detalhesDiaFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Criar instâncias de DiaDaSemana e adicioná-las à listaDeDias
        listaDeDias = new ArrayList<>();
        setupDiasDaSemana(view);

        return view;
    }



}