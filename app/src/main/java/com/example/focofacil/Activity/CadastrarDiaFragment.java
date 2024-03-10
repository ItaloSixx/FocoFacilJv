package com.example.focofacil.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.focofacil.BroadcastReceiver.NotificationReceiver;
import com.example.focofacil.BroadcastReceiver.TaskNotificationHelper;
import com.example.focofacil.Dao.TarefaDAO;
import com.example.focofacil.Model.TarefaModel;
import com.example.focofacil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadastrarDiaFragment extends Fragment {
    private static final int NOTIFICATION_REQUEST_CODE = 200;
    private AlarmManager alarmManager;
    FirebaseDatabase database;
    CheckBox checkboxRepeticao;
    Button date_in;
    Button time_in;
    Button buttonSalvar;
    TextView txtNomeUsuario;
    private EditText editTextAtividade;
    private EditText editTextTituloAtividade;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_dia, container, false);
        database = FirebaseDatabase.getInstance();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        Toolbar toolbar = view.findViewById(R.id.toolbarToolbar);
        editTextAtividade = view.findViewById(R.id.editTextAtividade);
        editTextTituloAtividade = view.findViewById(R.id.editTexttituloAtividade);
        Button date_in = view.findViewById(R.id.buttonOpenCalendarDialog);
        Button time_in = view.findViewById(R.id.btnHorario);
        checkboxRepeticao = view.findViewById(R.id.repeatCheckbox);
        txtNomeUsuario = view.findViewById(R.id.txtNome1);
        nomeUsuario();
        buttonSalvar = view.findViewById(R.id.btnEdit);
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataStr = date_in.getText().toString();
                String horaStr = time_in.getText().toString();

                if (!dataStr.isEmpty() && !horaStr.isEmpty()) {
                    SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd/MM/yyyy");
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
        // Adicione esta linha para inicializar o checkboxRepeticao
        checkboxRepeticao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Marcar indica que a tarefa deve ser repetida diariamente
                    scheduleDailyNotification();
                } else {
                    // Desmarcar indica que a tarefa não deve ser repetida diariamente
                    cancelNotification();
                }
            }
        });

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
                boolean repeticao = checkboxRepeticao.isChecked(); // Verifica se o CheckBox está marcado

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
                DatabaseReference tarefaRef = FirebaseDatabase.getInstance().getReference("Tarefas").push();
                tarefaRef.setValue(tarefa);

                // Salvar localmente
                TarefaDAO tarefaDAO = new TarefaDAO(requireContext());
                long newRowId = tarefaDAO.inserirTarefa(tarefa);
                Log.d("Persistir Tarefa", "ID da nova linha: " + newRowId);

                // Exibir mensagem após a conclusão da operação na thread principal
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (newRowId != -1) {
                            Toast.makeText(requireContext(), "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().beginTransaction().remove(CadastrarDiaFragment.this).commit();

                            // Se o CheckBox estiver marcado, agende a repetição diária da tarefa
                            if (repeticao) {
                                long notificationTimeInMillis = calcularTempoNotificacao(year, month, dayOfMonth, selectedHourOfDay, selectedMinute);
                                Log.d("Persistir Tarefa", "Agendando notificação para: " + new Date(notificationTimeInMillis).toString());
                                TaskNotificationHelper.scheduleNotification(requireContext(), titulo, notificationTimeInMillis);
                            }
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
                    time_in.setText(simpleDateFormat.format(selectedDate.getTime()));
                }
            }
        };

        // Criar o DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Exibir o DatePickerDialog
        datePickerDialog.show();
    }

    private void scheduleDailyNotification() {
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Configurar o horário da notificação (por exemplo, 8:00 da manhã)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        // Se o horário já passou hoje, agende para amanhã
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Agendar a notificação para se repetir diariamente
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void cancelNotification() {
        if (alarmManager != null) {
            Intent intent = new Intent(requireActivity(), NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    public void nomeUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            txtNomeUsuario.setText(nome);

        }
    }

    private long calcularTempoNotificacao(int year, int month, int dayOfMonth, int selectedHourOfDay, int selectedMinute) {
        // Obter a data e hora atual
        Calendar currentTime = Calendar.getInstance();
        Log.d("Calcular Notificação", "Tempo atual: " + currentTime.getTime().toString());

        // Criar um objeto Calendar para a data e hora da tarefa
        Calendar taskTime = Calendar.getInstance();
        taskTime.set(year, month, dayOfMonth, selectedHourOfDay, selectedMinute);
        Log.d("Calcular Notificação", "Tempo da tarefa: " + taskTime.getTime().toString());

        // Calcular a diferença entre a hora atual e a hora da tarefa em milissegundos
        long diffInMillis = taskTime.getTimeInMillis() - currentTime.getTimeInMillis();
        Log.d("Calcular Notificação", "Diferença de tempo em milissegundos: " + diffInMillis);

        // Retornar o tempo da notificação (por exemplo, 10 minutos antes da tarefa)
        long notificationTimeInMillis = taskTime.getTimeInMillis() - (10 * 60 * 1000); // 10 minutos antes da tarefa
        Log.d("Calcular Notificação", "Tempo da notificação: " + new Date(notificationTimeInMillis).toString());
        return notificationTimeInMillis;
    }


}