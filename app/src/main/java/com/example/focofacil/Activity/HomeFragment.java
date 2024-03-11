package com.example.focofacil.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.focofacil.DiaDaSemana;
import com.example.focofacil.DiaSemanaAdapter;

import com.example.focofacil.R;
import com.example.focofacil.Tarefa;
import com.example.focofacil.Utils.MenuCam;
import com.example.focofacil.adapters.TarefaFirebaseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Clock;
import java.util.ArrayList;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewDiasSemana;
    private ListAdapter adapter;
    private ArrayList<String> taref;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

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

    private void exibirInformacoesTarefas(DiaDaSemana dia, View view) {


        int txtAssuntoId = getResources().getIdentifier("txtTarefaAssunto", "id", getContext().getPackageName());
        int txtDataHoraId = getResources().getIdentifier("txtTarefaDataHora", "id", getContext().getPackageName());
        int txtDescricaoId = getResources().getIdentifier("txtTarefaDescricao", "id", getContext().getPackageName());

        TextView txtAssunto = view.findViewById(txtAssuntoId);
        TextView txtDataHora = view.findViewById(txtDataHoraId);
        TextView txtDescricao = view.findViewById(txtDescricaoId);


        // Limpar os TextViews
        // Verificar se TextViews foram encontrados
        if (txtAssunto != null && txtDataHora != null) {
            // Limpar os TextViews

            txtAssunto.setText("");
            txtDataHora.setText("");
            txtDescricao.setText("");

            // Exibir informações da última tarefa (caso haja mais de uma)
            for (TarefaFirebase tarefa : dia.getListaDeTarefas()) {
                // Preencher os TextViews com as informações da tarefa
                txtAssunto.setText(tarefa.getTitulo());

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                txtDataHora.setText(sdf.format(tarefa.getDataHora()));

                txtDescricao.setText(tarefa.getDescricao());

                Log.e("TextView", "txtAssunto: " + txtAssunto + " | txtDataHora: " + txtDataHora + " | txtDescricao: " + txtDescricao);
            }
        } else {

            Log.e("TextView", "txtAssunto e txtDataHora são NULOS");

        }
    }

    private ArrayList<Tarefa> obterListaDeTarefas() {
        // Retorna uma lista fictícia de tarefas para cada dia

        ArrayList<Tarefa> listaDeTarefas = new ArrayList<>();
        listaDeTarefas.add(new Tarefa("Tarefa 1", "Assunto 1", new Date()));

        // Adicione mais tarefas conforme necessário.
        return listaDeTarefas;
    }

    private String obterNomeDia(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        // Formato desejado: txt_segunda_feira (substitui hífens por underscores)
        String dayName = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date(calendar.getTimeInMillis()));

        dayName = dayName
                .replaceAll("[áãâàä]", "a")
                .replaceAll("[éêèë]", "e")
                .replaceAll("[íîìï]", "i")
                .replaceAll("[óôõòö]", "o")
                .replaceAll("[úûùü]", "u")
                .replaceAll("[ç]", "c");

        return dayName.toLowerCase().replace("-", "_");
    }

    private void configurarOnClickListener(TextView textView, final DiaDaSemana dia) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalhesDiaFragment detalhesDiaFragment = DetalhesDiaFragment.newInstance(dia);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, detalhesDiaFragment).addToBackStack(null).commit();

            }
        });
    }

    private int obterDiaDaSemana(long dia, long mes, long ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set((int) ano, (int) mes - 1, (int) dia); // Mês começa do zero no Calendar
        int diaDaSemana = calendar.get(Calendar.DAY_OF_WEEK);

        // Ajustar para retornar um índice baseado em zero (0 para domingo, 1 para segunda, ..., 6 para sábado)
        int indice = (diaDaSemana + 6) % 7;

        // Ajustar para o índice correto se necessário
        return indice;
    }


    private DiaDaSemana obterDiaDaSemanaCorrespondente(String nomeDia) {
        for (DiaDaSemana dia : listaDeDias) {
            if (dia.getNomeDia().equalsIgnoreCase(nomeDia)) {
                Log.d(TAG, "Encontrado Dia: " + nomeDia);
                return dia;
            }
        }
        Log.d(TAG, "Dia não encontrado: " + nomeDia);

        // Se não encontrar, criar um novo e adicionar à lista
        DiaDaSemana novoDia = new DiaDaSemana(nomeDia);
        listaDeDias.add(novoDia);
        return novoDia;
    }

    private void configurarDiasDaSemana(View view) {
        // Adicione os dias da semana à listaDeDias
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            String nomeDia = obterNomeDiaDaSemana(i); // Método para obter o nome do dia da semana
            DiaDaSemana diaDaSemana = new DiaDaSemana(nomeDia);

            Log.d(TAG, "Dia: " + nomeDia);

            listaDeDias.add(diaDaSemana);
        }

    }

    private void passandoDiasDaSemanaNoRecyclerView(View view){
        // Configurar o RecyclerView para cada dia da semana
        configurarRecyclerView(view, Calendar.SUNDAY, R.id.recyclerViewDomingo);
        configurarRecyclerView(view, Calendar.MONDAY, R.id.recyclerViewSegunda);
        configurarRecyclerView(view, Calendar.TUESDAY, R.id.recyclerViewTerca);
        configurarRecyclerView(view, Calendar.WEDNESDAY, R.id.recyclerViewQuarta);
        configurarRecyclerView(view, Calendar.THURSDAY, R.id.recyclerViewQuinta);
        configurarRecyclerView(view, Calendar.FRIDAY, R.id.recyclerViewSexta);
        configurarRecyclerView(view, Calendar.SATURDAY, R.id.recyclerViewSabado);
    }

    private void configurarRecyclerView(View view, int diaDaSemana, int recyclerViewId) {
        // Configurar o RecyclerView para o dia da semana
        RecyclerView recyclerView = view.findViewById(recyclerViewId);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtenha o DiaDaSemana correspondente
        DiaDaSemana diaDaSemanaObj = obterDiaDaSemanaCorrespondente(obterNomeDiaDaSemana(diaDaSemana));
        Log.d(TAG, "configurarRecyclerView - diaDaSemanaObj : " + diaDaSemanaObj);
        // Configurar o adaptador com a lista de tarefas do dia da semana
        TarefaFirebaseAdapter tarefaAdapter = new TarefaFirebaseAdapter(getContext(), diaDaSemanaObj.getListaDeTarefas());
        Log.d(TAG, "configurarRecyclerView - diaDaSemanaObj.getListaDeTarefas() : " + diaDaSemanaObj.getListaDeTarefas());

        //Configurar o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tarefaAdapter);
    }

    private TarefaFirebaseAdapter obterAdapterParaDiaDaSemana(int diaDaSemana) {
        // Obter o DiaDaSemana correspondente
        DiaDaSemana diaDaSemanaObj = listaDeDias.get(diaDaSemana - 1); // -1 porque os dias começam do domingo (1)

        // Criar e retornar um adaptador com a lista de tarefas do dia da semana
        return new TarefaFirebaseAdapter(getContext(), diaDaSemanaObj.getListaDeTarefas());
    }

    // Método para obter o nome do dia da semana
    private String obterNomeDiaDaSemana(int diaDaSemana) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, diaDaSemana);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());

        return sdf.format(calendar.getTime());
    }


    private ListView listTarefa;

    private RecyclerView recyclerViewSegunda;

    private List<TarefaFirebase> tarefaList;
    private TarefaFirebaseAdapter tarefaAdapter;
    private TarefaFirebase androidTarefa;
    TextView txtNome;
    FloatingActionButton floatAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtNome = view.findViewById(R.id.txtNome12);
        floatAdd = view.findViewById(R.id.floatingBtnFloatingactionbutton);

        floatAdd.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Use uma animação personalizada para uma transição suave
            //fragmentTransaction.setCustomAnimations(
                    //R.anim.enter_from_right, // Animação para entrar no fragmento
                    //R.anim.exit_to_left,    // Animação para sair do fragmento
                    //R.anim.enter_from_left,  // Animação para retornar ao fragmento (opcional)
                    //R.anim.exit_to_right     // Animação para sair para o fragmento anterior (opcional)
           // );

            // Substitua o fragmento no container
            fragmentTransaction.replace(R.id.fragment_container, new CadastrarDiaFragment());
            fragmentTransaction.addToBackStack(null); // Adiciona à pilha de retrocesso se desejado
            fragmentTransaction.commit();
        });


        mostrarPerfil();

        listaDeDias = new ArrayList<>();
        configurarDiasDaSemana(view);

        Log.e("TarefaFirebase", "Iniciando leitura de tarefa");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();

        List<TarefaFirebase> listaDeTarefasFirebase = new ArrayList<>();

        if (user != null) {
            FirebaseUser finalUser = user;
            user.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uid = finalUser.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tarefas");

                    Log.d("TAG", "Iniciando consulta ao Firebase Realtime Database...");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Limpar as listas de tarefas em cada dia da semana
                            for (DiaDaSemana dia : listaDeDias) {
                                dia.getListaDeTarefas().clear();
                            }
                            Log.d(TAG, "Consulta ao Firebase Realtime Database concluída com sucesso!");
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d(TAG, "Verificando dados do firebase!");

                                String taskUserId = snapshot.child("idUsuario").getValue(String.class);
                                Log.d(TAG, "taskUserId: " + taskUserId);

                                if (taskUserId != null && taskUserId.equals(uid)) {
                                    Log.d(TAG, "taskUserId é diferente de null");

                                    // Esta tarefa pertence ao usuário logado
                                    // Faça o que você precisa com a tarefa aqui
                                    String idTarefa = snapshot.child("idTarefa").getValue(String.class);
                                    String taskTitle = snapshot.child("titulo").getValue(String.class);
                                    String taskDescription = snapshot.child("descricao").getValue(String.class);

                                    Long diaLong = snapshot.child("dia").getValue() != null ? snapshot.child("dia").getValue(Long.class) : 0L;
                                    Log.d("TAG", "ID da Tarefa: " + idTarefa);
                                    Log.d("TAG", "Título da Tarefa: " + taskTitle);
                                    Log.d("TAG", "Dia da Tarefa: " + diaLong);


                                   // Long repeticaoLong = snapshot.child("repeticao").getValue() != null ? snapshot.child("repeticao").getValue(Long.class) : 0L;
                                    Long repeticaoLong = (snapshot.child("repeticao").getValue() instanceof Long) ? (Long) snapshot.child("repeticao").getValue() : 0L;


                                    Long mesLong = snapshot.child("mes").getValue() != null ? snapshot.child("mes").getValue(Long.class) : 0L;

                                    Long anoLong = snapshot.child("ano").getValue() != null ? snapshot.child("ano").getValue(Long.class) : 0L;

                                    Long horaLong = snapshot.child("hora").getValue() != null ? snapshot.child("hora").getValue(Long.class) : 0L;

                                    Long minutoLong = snapshot.child("minuto").getValue() != null ? snapshot.child("minuto").getValue(Long.class) : 0L;

                                    Log.d("TAG", "Título da Tarefa: " + taskTitle);
                                    Log.d("TAG", "Descrição da Tarefa: " + taskDescription);
                                    Log.d("TAG", "Repetição da Tarefa: " + repeticaoLong);
                                    Log.d("TAG", "Dia da Tarefa: " + diaLong);
                                    Log.d("TAG", "Mês da Tarefa: " + mesLong);
                                    Log.d("TAG", "Ano da Tarefa: " + anoLong);
                                    Log.d("TAG", "Hora da Tarefa: " + horaLong);
                                    Log.d("TAG", "Minuto da Tarefa: " + minutoLong);

                                    String diaString = String.valueOf(diaLong);
                                    String repeticaoString = String.valueOf(repeticaoLong);
                                    String mesString = String.valueOf(mesLong);
                                    String anoString = String.valueOf(anoLong);
                                    String horaString = String.valueOf(horaLong);
                                    String minutoString = String.valueOf(minutoLong);


                                    TarefaFirebase tarefa_firebase = new TarefaFirebase(taskTitle, taskDescription, repeticaoString, diaString, mesString, anoString, horaString, minutoString);
                                    tarefa_firebase.setIdTarefa(idTarefa);

                                    // Associando a tarefa ao dia da semana correspondente

                                    int diaDaSemana = obterDiaDaSemana(diaLong, mesLong, anoLong); // Método para obter o dia da semana
                                    if (diaDaSemana >= 0 && diaDaSemana < listaDeDias.size()) {
                                        DiaDaSemana diaDaSemanaObj = listaDeDias.get(diaDaSemana);
                                        diaDaSemanaObj.adicionarTarefa(tarefa_firebase);
                                    } else {
                                        Log.e("ERROR", "Índice de diaDaSemana fora dos limites válidos.");
                                    }

                                    Log.d(TAG, "Posição do Dia da Semana: " + diaDaSemana);
                                    Log.d(TAG, "Tamanho da Lista de Dias: " + listaDeDias.size());


                                    passandoDiasDaSemanaNoRecyclerView(view);

                                    //    public TarefaFirebase(titulo, descricao, repeticao, dia, mes, ano,hora, minuto) {
                                    Log.d("TAG", "Objeto tarefa_firebase, Titulo: " + tarefa_firebase.getTitulo());
                                    Log.d("TAG", "Data: " + tarefa_firebase.getDia() + "/" + tarefa_firebase.getMes() + "/" + tarefa_firebase.getAno() + " - " + tarefa_firebase.getHora() + ":" + tarefa_firebase.getMinuto());
                                    tarefa_firebase.setDataHora("Data: " + tarefa_firebase.getDia() + "/" + tarefa_firebase.getMes() + "/" + tarefa_firebase.getAno() + " - " + tarefa_firebase.getHora() + ":" + tarefa_firebase.getMinuto());
                                    listaDeTarefasFirebase.add(tarefa_firebase);
                                    // Adicione o código para processar os outros campos da tarefa conforme necessário
                                    Log.d("TAG", "Tarefa: " + tarefa_firebase);
                                    Log.d("TAG", "ListaDeTarefasFirebase: " + listaDeTarefasFirebase + " | Tamanho do Array: " + listaDeTarefasFirebase.size());

                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "Erro ao consultar o Firebase Realtime Database: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "Falha ao recarregar o usuário: ", task.getException());
                }
            });
        } else {
            Log.e(TAG, "Usuário atual é nulo.");
        }

        // --------------------------------------------------------------------------------

        return view;
    }

    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            txtNome.setText(nome);
        }
    }

}
