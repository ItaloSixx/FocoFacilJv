package com.example.focofacil.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focofacil.Model.DiaDaSemana;
import com.example.focofacil.R;
import com.example.focofacil.Model.Tarefa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalhesDiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalhesDiaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalhesDiaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dia Parameter 1.
     * @return A new instance of fragment DetalhesDiaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalhesDiaFragment newInstance(DiaDaSemana dia) {
        DetalhesDiaFragment fragment = new DetalhesDiaFragment();
        Bundle args = new Bundle();
        args.putString("diaDaSemana", dia.getNomeDia());
        args.putSerializable("listaDeTarefas", dia.getListaDeTarefas());
        args.putSerializable("dataSelecionada", dia.getDataSelecionada());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhes_dia, container, false);

        Toast.makeText(getActivity(), "Chegamos em Detalhes Dia", Toast.LENGTH_SHORT).show();

        // Recuperar o nome do dia da semana do Intent
        String diaDaSemana = getArguments().getString("diaDaSemana");
        Date dataSelecionada = (Date) getArguments().getSerializable("dataSelecionada");
        ArrayList<Tarefa> listaDeTarefas = (ArrayList<Tarefa>) getArguments().getSerializable("listaDeTarefas");


        // Exibir os dados nos TextViews da DetalhesDiaActivity
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txtDiaDaSemana = view.findViewById(R.id.txtDiaDaSemana);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txtDataSemana = view.findViewById(R.id.txtDataSemana);

        txtDiaDaSemana.setText("Dia da Semana: " + diaDaSemana);

        // Formatar a data no formato desejado
        SimpleDateFormat formatoData = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault());
        String dataFormatada = formatoData.format(dataSelecionada);

        // Exibir a data no TextView
        txtDataSemana.setText("Data: " + dataFormatada);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout linearLayoutTarefas = view.findViewById(R.id.linearLayoutTarefas);

        // Iterar sobre a lista de tarefas e criar Views para cada uma
        for (Tarefa tarefa : listaDeTarefas) {
            // Crie um novo LinearLayout para representar uma tarefa
            LinearLayout linearLayoutTarefa = new LinearLayout(requireContext());
            linearLayoutTarefa.setOrientation(LinearLayout.HORIZONTAL);

            // Crie TextViews para exibir informações da tarefa
            TextView txtDescricao = new TextView(requireContext());
            TextView txtTarefaAssunto = new TextView(requireContext());
            TextView txtDataHora = new TextView(requireContext());

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

        return view;
    }
}