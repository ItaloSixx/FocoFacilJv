package com.example.focofacil.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.focofacil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditarTarefaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TAREFA = "tarefa";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TarefaFirebase tarefa;

    TextView txtNomeUsuario, txtTarefaDia, txtTarefaMes, txtTarefaAno, txtData;
    EditText edtTitulo, edtDescricao;
    Button btnEdit, btnHorario, btnEdtCalendarDialog;

    public static EditarTarefaFragment newInstance(TarefaFirebase tarefa) {
        EditarTarefaFragment fragment = new EditarTarefaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAREFA, (Serializable) tarefa);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_tarefa, container, false);

        //txtNomeUsuario = view.findViewById(R.id.txtNome1);

        btnEdtCalendarDialog = view.findViewById(R.id.btnEdtCalendarDialog);

        txtTarefaDia = view.findViewById(R.id.txtTarefaDia);
        txtTarefaMes = view.findViewById(R.id.txtTarefaMes);
        txtTarefaAno = view.findViewById(R.id.txtTarefaAno);
        edtTitulo = view.findViewById(R.id.edtTitulo);
        edtDescricao = view.findViewById(R.id.edtDescricao);
        btnHorario = view.findViewById(R.id.btnHorario);
        btnEdit = view.findViewById(R.id.btnEdit);


        // Obter os argumentos passados para este fragmento, se houver
        Bundle bundle = getArguments();
        if (tarefa  != null) {
            btnEdtCalendarDialog.setText(tarefa.getDia() + "/" + tarefa.getMes() + "/" + tarefa.getAno());
            edtTitulo.setText(tarefa.getTitulo());
            edtDescricao.setText(tarefa.getDescricao());
            txtTarefaDia.setText(tarefa.getDia());
            txtTarefaMes.setText(tarefa.getMes());
            txtTarefaAno.setText(tarefa.getAno());
            btnHorario.setText(tarefa.getHora() + ":" + tarefa.getMinuto());
            btnHorario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeDialog(btnHorario);
                }
            });
            btnEdtCalendarDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog(btnEdtCalendarDialog);
                }
            });

                    /*String horaStr = btnHorario.getText().toString();

                    if (!horaStr.isEmpty()) {
                        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

                        try {
                            Calendar horaSelecionada = Calendar.getInstance();
                            horaSelecionada.setTime(sdfTime.parse(horaStr));

                            int hora = horaSelecionada.get(Calendar.HOUR_OF_DAY);
                            int minuto = horaSelecionada.get(Calendar.MINUTE);




                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Por favor, insira uma hora válida", Toast.LENGTH_SHORT).show();
                    }*/


        }

        // Configurar o botão de edição
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter o ID da tarefa
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                // 2. Obter uma referência para a localização específica da tarefa no banco de dados
                databaseReference.child("tarefas").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            TarefaFirebase tarefa = snapshot.getValue(TarefaFirebase.class);
                            // Supondo que Tarefa é uma classe que representa sua estrutura de dados
                            if (tarefa != null) {
                                String idTarefa = snapshot.child("idTarefa").getValue(String.class);


                                // Obter os novos valores dos campos de título, descrição, ano, mês, dia, hora e minuto
                                String novoTitulo = edtTitulo.getText().toString();
                                String novaDescricao = edtDescricao.getText().toString();
                                int novoAno = Integer.parseInt(txtTarefaAno.getText().toString());
                                int novoMes = Integer.parseInt(txtTarefaMes.getText().toString());
                                int novoDia = Integer.parseInt(txtTarefaDia.getText().toString());
                                String horaStr = btnHorario.getText().toString();
                                int novaHora = 0;
                                int novoMinuto = 0;
                                if (!horaStr.isEmpty()) {
                                    String[] horaMinuto = horaStr.split(":");
                                    novaHora = Integer.parseInt(horaMinuto[0]);
                                    novoMinuto = Integer.parseInt(horaMinuto[1]);
                                }

                                Log.d("TAG", "tarefaId da Tarefa: " + idTarefa);
                                Log.d("TAG", "novoTitulo da Tarefa: " + novoTitulo);
                                Log.d("TAG", "novaDescricao da Tarefa: " + novaDescricao);
                                Log.d("TAG", "novoAno da Tarefa: " + novoAno);
                                Log.d("TAG", "novoMes da Tarefa: " + novoMes);
                                Log.d("TAG", "novoDia da Tarefa: " + novoDia);
                                Log.d("TAG", "horaStr da Tarefa: " + horaStr);

                                DatabaseReference tarefaRef = databaseReference.child("tarefas").child(idTarefa);
                                tarefaRef.child("titulo").setValue(novoTitulo);
                                tarefaRef.child("descricao").setValue(novaDescricao);
                                tarefaRef.child("ano").setValue(novoAno);
                                tarefaRef.child("mes").setValue(novoMes);
                                tarefaRef.child("dia").setValue(novoDia);
                                tarefaRef.child("hora").setValue(novaHora);
                                tarefaRef.child("minuto").setValue(novoMinuto);

                                // Por exemplo, você pode obter os valores dos campos de texto (edtTitulo e edtDescricao) e atualizar a tarefa no banco de dados
                                // Após a edição, você pode exibir uma mensagem para o usuário informando que a tarefa foi editada com sucesso
                                Toast.makeText(requireContext(), "Tarefa editada com sucesso", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });

            }
        });

        return view;
    }


    // Método para editar a tarefa
    private void editarTarefa(String tarefaId, String novoTitulo, String novaDescricao, int novoAno, int novoMes, int novoDia, int novaHora, int novoMinuto) {
        // 1. Obter uma instância do banco de dados do Firebase




        // 3. Atualizar os campos da tarefa com os novos valores fornecidos pelo usuário

    }

    private void showTimeDialog(final Button new_time_task) {
        final Calendar calendar=Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                new_time_task.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }
    private void showDateDialog(final Button new_date_task) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Criar um objeto Calendar para a data selecionada
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Verificar se a data selecionada é anterior à data atual
                if (selectedDate.before(Calendar.getInstance())) {
                    // Data selecionada é anterior à data atual, exibir mensagem de erro
                    Toast.makeText(requireContext(), "Por favor, selecione uma data válida", Toast.LENGTH_SHORT).show();

                } else {
                    // Data selecionada é válida, atualizar o campo de texto com a data selecionada
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
                    new_date_task.setText(simpleDateFormat.format(selectedDate.getTime()));
                }
            }
        };
        // Criar o DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Exibir o DatePickerDialog
        datePickerDialog.show();
    }



    public EditarTarefaFragment() {
        // Required empty public constructor
    }

    public static EditarTarefaFragment newInstance(String param1, String param2) {
        EditarTarefaFragment fragment = new EditarTarefaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tarefa = (TarefaFirebase) getArguments().getSerializable(ARG_TAREFA);
        }
    }

    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            txtNomeUsuario.setText(nome);
        }
    }

}