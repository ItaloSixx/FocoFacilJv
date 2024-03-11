package com.example.focofacil.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.focofacil.Dao.TarefaDAO;
import com.example.focofacil.Model.TarefaModel;
import com.example.focofacil.R;
import com.example.focofacil.Service.MeuServico;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CadastrarDiaFragment extends Fragment {
    private static final int NOTIFICATION_REQUEST_CODE = 200;
    private AlarmManager alarmManager;
    FirebaseDatabase database;
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
        Button time_in = view.findViewById(R.id.buttonOpenTimePickerDialog);
        txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        nomeUsuario();
        Button buttonSalvar = view.findViewById(R.id.buttonSalvar);
        buttonSalvar.setOnClickListener(v -> {
            String dataStr = date_in.getText().toString();
            String horaStr = time_in.getText().toString();

            // Verifica se a data está vazia
            if (dataStr.isEmpty()) {
                // Se a data estiver vazia, preenche com a data atual
                SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd/MM/yyyy");
                Calendar dataAtual = Calendar.getInstance();
                dataStr = sdfDate.format(dataAtual.getTime());
                date_in.setText(dataStr);
            }

            // Verifica se a hora também está vazia
            if (!horaStr.isEmpty()) {
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

                    // Redirecionar para a HomeFragment após salvar com sucesso
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new HomeFragment())
                            .addToBackStack(null)
                            .commit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, insira uma hora válida", Toast.LENGTH_SHORT).show();
            }
        });


        date_in.setOnClickListener(v -> showDateDialog(date_in));
        time_in.setOnClickListener(v -> showTimeDialog(time_in));
     return view;
    }
    public void persistirTarefa(int year, int month, int dayOfMonth, int selectedHourOfDay, int selectedMinute) {
        new Thread(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String idUsuario = user.getUid();
            String descricao = editTextAtividade.getText().toString();
            String titulo = editTextTituloAtividade.getText().toString();

            TarefaModel tarefa = new TarefaModel();
            tarefa.setIdUsuario(idUsuario);
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            tarefa.setDia(dayOfMonth);
            tarefa.setMes(month + 1);
            tarefa.setAno(year);
            tarefa.setHora(selectedHourOfDay);
            tarefa.setMinuto(selectedMinute);

            // Salvar no Firebase
            // Salvar no Firebase com chave gerada automaticamente
            DatabaseReference tarefaRef = FirebaseDatabase.getInstance().getReference("Tarefas").push();
            String idTarefa = tarefaRef.getKey(); // Obter a chave gerada automaticamente
            tarefa.setIdTarefa(idTarefa); // Definir a chave gerada como o ID da tarefa
            tarefaRef.setValue(tarefa);

            // Salvar localmente
            TarefaDAO tarefaDAO = new TarefaDAO(requireContext());
            long newRowId = tarefaDAO.inserirTarefa(tarefa);
            Log.d("Persistir Tarefa", "ID da nova linha: " + newRowId);

            // Exibir mensagem após a conclusão da operação na thread principal
            new Handler(Looper.getMainLooper()).post(() -> {
                if (newRowId != -1) {
                    Toast.makeText(requireContext(), "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction().remove(CadastrarDiaFragment.this).commit();

                    long notificationTimeInMillis = calcularTempoNotificacao(year, month, dayOfMonth, selectedHourOfDay, selectedMinute);
                    Log.d("Persistir Tarefa", "Agendando notificação para: " + new Date(notificationTimeInMillis).toString());
                    schedulesecNotification(year, month, dayOfMonth, selectedHourOfDay, selectedMinute);
                } else {
                    Toast.makeText(requireContext(), "Erro ao adicionar tarefa", Toast.LENGTH_SHORT).show();
                }
            });
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
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        Context context = new ContextThemeWrapper(requireContext(), android.R.style.Theme_DeviceDefault_Dialog);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Calendar para a data selecionada
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, selectedYear);
                        selectedDate.set(Calendar.MONTH, selectedMonth);
                        selectedDate.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);

                        // Verifica se a data selecionada é anterior à data atual
                        if (selectedDate.before(Calendar.getInstance())) {
                            // Data selecionada é anterior à data atual, exibir mensagem de erro
                            Toast.makeText(requireContext(), "Por favor, selecione uma data válida", Toast.LENGTH_SHORT).show();
                        } else {
                            // Data selecionada é válida, atualizar o campo de texto com a data selecionada
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
                            time_in.setText(simpleDateFormat.format(selectedDate.getTime()));
                        }
                    }
                },
                year, month, dayOfMonth
        );

        // Definir o comportamento para quando o botão de "Cancelar" for clicado
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Definir a data atual como a data selecionada
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        });

        datePickerDialog.show();
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
    public long calcularTempoNotificacao(int year, int month, int dayOfMonth, int selectedHourOfDay, int selectedMinute) {
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
        long notificationTimeInMillis = taskTime.getTimeInMillis() - (5 * 60 * 1000); // 10 minutos antes da tarefa
        Log.d("Calcular Notificação", "Tempo da notificação: " + new Date(notificationTimeInMillis).toString());
        return notificationTimeInMillis;
    }

    private void schedulesecNotification(int year, int month, int dayOfMonth, int selectedHourOfDay, int selectedMinute) {
        Intent notificationIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calcula o tempo de notificação 5 minutos antes da hora selecionada para a tarefa
        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(year, month, dayOfMonth, selectedHourOfDay, selectedMinute);
        notificationTime.add(Calendar.MINUTE, -5); // Subtrai 5 minutos

        long futureInMillis = notificationTime.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }
}