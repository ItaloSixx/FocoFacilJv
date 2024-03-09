package com.example.focofacil.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.focofacil.Bd.DatabaseHelper;
import com.example.focofacil.R;
import com.example.focofacil.adapters.CustomSpinnerAdapter;
import com.example.focofacil.contracts.TarefaContract;
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
    private ActivityCadastrarDiaViewModel viewModel;
    Button date_in;
    Button time_in;
    private EditText editTextAtividade;
    private EditText editTextTituloAtividade;
    private Spinner spinnerRepeticao;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_dia, container, false);
        database = FirebaseDatabase.getInstance();
        viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(ActivityCadastrarDiaViewModel.class);
        toolbar = view.findViewById(R.id.toolbarToolbar);
        editTextAtividade = view.findViewById(R.id.editTextAtividade);
        editTextTituloAtividade = view.findViewById(R.id.editTexttituloAtividade);

        spinnerRepeticao = view.findViewById(R.id.spinnerRepeticao);
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

        viewModel.getDataSelecionada().observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                // Atualizar a tela com as atividades do dia selecionado
            }
        });
        viewModel.getDataSelecionada().observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                // Atualizar a tela com as atividades do dia selecionado
            }
        });
        viewModel.carregarDados();

        // Recuperar preferências compartilhadas
        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        return view;
    }


    public void persistirTarefa(int year, int month, int dayOfMonth, int selectedHourOfDay, int selectedMinute) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario = user.getUid();

        String descricao = editTextAtividade.getText().toString();
        String titulo = editTextTituloAtividade.getText().toString();
        int repeticao = spinnerRepeticao.getSelectedItemPosition();

        Map<String, Object> atividade = new HashMap<>();
        atividade.put("titulo", titulo);
        atividade.put("descricao", descricao);
        atividade.put("repeticao", repeticao);
        atividade.put("dia", dayOfMonth);
        atividade.put("mes", month + 1); // Lembrando que Janeiro é 0, Fevereiro é 1, e assim por diante
        atividade.put("ano", year);
        atividade.put("hora", selectedHourOfDay);
        atividade.put("minuto", selectedMinute);
        atividade.put("idUsuario", idUsuario);

        // Gerar um novo nó com ID único para a tarefa
        DatabaseReference tarefaRef = FirebaseDatabase.getInstance().getReference("Tarefas").child(user.getUid()).push();

        // Definir os valores da tarefa no novo nó
        tarefaRef.setValue(atividade)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(requireContext(), "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().beginTransaction().remove(CadastrarDiaFragment.this).commit();

                        } else {
                            Toast.makeText(requireContext(), "Tente novamente"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Utilizando uma nova Thread para inserir dados no SQLite
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(TarefaContract.TarefaEntry.COLUMN_TITULO, titulo);
                values.put(TarefaContract.TarefaEntry.COLUMN_DESCRICAO, descricao);
                values.put(TarefaContract.TarefaEntry.COLUMN_REPETICAO, repeticao);
                values.put(TarefaContract.TarefaEntry.COLUMN_DIA, dayOfMonth);
                values.put(TarefaContract.TarefaEntry.COLUMN_MES, month + 1);
                values.put(TarefaContract.TarefaEntry.COLUMN_ANO, year);
                values.put(TarefaContract.TarefaEntry.COLUMN_HORA, selectedHourOfDay);
                values.put(TarefaContract.TarefaEntry.COLUMN_MINUTO, selectedMinute);
                values.put(TarefaContract.TarefaEntry.COLUMN_ID_USUARIO, idUsuario);

                long newRowId = db.insert(TarefaContract.TarefaEntry.TABLE_NAME, null, values);

                if (newRowId != -1) {
                    // Notificar o usuário sobre o sucesso da inserção
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().beginTransaction().remove(CadastrarDiaFragment.this).commit();
                        }
                    });
                } else {
                    // Notificar o usuário sobre o erro na inserção
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                db.close();
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






