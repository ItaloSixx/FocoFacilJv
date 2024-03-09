package com.example.focofacil.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.focofacil.Dao.TarefaDAO;
import com.example.focofacil.Model.TarefaModel;
import com.example.focofacil.R;
import com.example.focofacil.adapters.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CadastrarDiaFragment extends Fragment {
    private Toolbar toolbar;
    private ViewModelProvider viewModelProvider;
    FirebaseDatabase database;
    Button date_in;
    Button time_in;
    CheckBox checkBoxRepeat;
    private EditText editTextAtividade;
    private EditText editTextTituloAtividade;
    private Spinner spinnerRepeticao;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_dia, container, false);

        database = FirebaseDatabase.getInstance();
        viewModelProvider = new ViewModelProvider(this);
        toolbar = view.findViewById(R.id.toolbarToolbar);
        editTextAtividade = view.findViewById(R.id.editTextAtividade);
        editTextTituloAtividade = view.findViewById(R.id.editTexttituloAtividade);

        checkBoxRepeat = view.findViewById(R.id.repeatCheckbox);
        Button date_in = view.findViewById(R.id.buttonOpenCalendarDialog);
        Button time_in = view.findViewById(R.id.buttonOpenTimePickerDialog);

        Button buttonSalvar = view.findViewById(R.id.buttonSalvar);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataStr = date_in.getText().toString();
                String horaStr = time_in.getText().toString();

                if (!dataStr.isEmpty() && !horaStr.isEmpty()) {
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

                    try {
                        // Parse da data e hora
                        Calendar dataSelecionada = Calendar.getInstance();
                        dataSelecionada.setTime(sdfDate.parse(dataStr));

                        Calendar horaSelecionada = Calendar.getInstance();
                        horaSelecionada.setTime(sdfTime.parse(horaStr));

                        int ano = dataSelecionada.get(Calendar.YEAR);
                        int mes = dataSelecionada.get(Calendar.MONTH);
                        int dia = dataSelecionada.get(Calendar.DAY_OF_MONTH);
                        int hora = horaSelecionada.get(Calendar.HOUR_OF_DAY);
                        int minuto = horaSelecionada.get(Calendar.MINUTE);

                        // Chamar a função persistirTarefa() com os valores de data e hora
                        persistirTarefa(ano, mes, dia, hora, minuto);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(requireContext(), "Por favor, insira uma data e hora válidas", Toast.LENGTH_SHORT).show();
                }
            }
        });



        date_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(date_in);
            }
        });

        time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(time_in);
            }
        });

        // Configurar spinner com opções de repetição
        Spinner spinner = view.findViewById(R.id.spinnerRepeticao);
        List<String> items = new ArrayList<>();
        items.add("Nunca");
        items.add("Diáriamente");
        items.add("Semanalmente");
        items.add("Mensalmente");
        items.add("Personalisado");

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(requireActivity(), items);
        spinner.setAdapter(adapter);

        // Configurar calendarView

        // Observar data selecionada e atualizar tela


        return view;
    }


    public void persistirTarefa(int year, int month, int dayOfMonth, int selectedHourOfDay, int selectedMinute) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String idUsuario = user.getUid();

                String descricao = editTextAtividade.getText().toString();
                String titulo = editTextTituloAtividade.getText().toString();
                int repeticao = spinnerRepeticao.getSelectedItemPosition();

                TarefaModel tarefa = new TarefaModel();
                tarefa.setIdUsuario(idUsuario);
                tarefa.setTitulo(titulo);
                tarefa.setDescricao(descricao);
                tarefa.setRepeticao(repeticao);
                tarefa.setDia(dayOfMonth);
                tarefa.setMes(month + 1);
                tarefa.setAno(year);
                tarefa.setHora(selectedHourOfDay);
                tarefa.setMinuto(selectedMinute);

                // Salvar no Firebase
                DatabaseReference tarefaRef = FirebaseDatabase.getInstance().getReference("Tarefas").child(user.getUid()).push();
                tarefaRef.setValue(tarefa);

                // Salvar localmente
                TarefaDAO tarefaDAO = new TarefaDAO(requireContext());
                long newRowId = tarefaDAO.inserirTarefa(tarefa);

                // Exibir mensagem após a conclusão da operação na thread principal
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (newRowId != -1) {
                            Toast.makeText(requireContext(), "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().beginTransaction().remove(CadastrarDiaFragment.this).commit();
                        } else {
                            Toast.makeText(requireContext(), "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }



    private void showTimeDialog(final Button date_in) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    private void showDateDialog(final Button time_in) {
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    time_in.setText(simpleDateFormat.format(selectedDate.getTime()));
                }
            }
        };

        // Criar o DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Exibir o DatePickerDialog
        datePickerDialog.show();
    }
}