package com.example.focofacil.Activity;
import static androidx.core.app.ActivityCompat.finishAffinity;
import static androidx.fragment.app.FragmentManager.TAG;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.focofacil.BroadcastReceiver.DailyReceiver;
import com.example.focofacil.BroadcastReceiver.NotificationReceiver;
import com.example.focofacil.BroadcastReceiver.WeeklyReceiver;
import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Calendar;

public class ConfigFragment extends Fragment {

    Switch switchSemanal, switchDiario;
    TextView txtNome;
    Button btnExcluir;
    private static final String SHARED_PREFS = "sharedPrefs";
    // Identificador único para as notificações diárias e semanais
    private static final int DAILY_NOTIFICATION_ID = 2;
    private static final int WEEKLY_NOTIFICATION_ID = 3;
    private static final String SWITCH_DIARIO_STATE = "switchDiarioState";
    private static final String SWITCH_SEMANAL_STATE = "switchSemanalState";
    public ConfigFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        switchDiario = view.findViewById(R.id.switchDiario);
        switchSemanal = view.findViewById(R.id.switchSemanal);
        txtNome = view.findViewById(R.id.txtNome12);
        btnExcluir = view.findViewById(R.id.btnExcluir);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        switchDiario.setChecked(sharedPreferences.getBoolean(SWITCH_DIARIO_STATE, false));
        switchSemanal.setChecked(sharedPreferences.getBoolean(SWITCH_SEMANAL_STATE, false));
        mostrarPerfil();
        switchDiario.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                scheduleDailyNotification();
                Toast.makeText(requireContext(), "As Notificações Diárias Estão Ativadas", Toast.LENGTH_SHORT).show();
            } else {
                cancelDailyNotification();
                Toast.makeText(requireContext(), "As Notificações Diárias Estão Desativadas", Toast.LENGTH_SHORT).show();
            }
        });
        switchSemanal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                scheduleWeeklyNotification();
                Toast.makeText(requireContext(), "As notificações Semanais Estão Ativadas", Toast.LENGTH_SHORT).show();
            } else {
                cancelWeeklyNotification();
                Toast.makeText(requireContext(), "As notificações Semanais Estão Desativadas", Toast.LENGTH_SHORT).show();
            }
        });

        btnExcluir.setOnClickListener(v -> excluirConta());

        return view;
    }
    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nome = user.getDisplayName();
            txtNome.setText(nome);
        }
    }
    private void cancelDailyNotification() {
        // Obter o AlarmManager do sistema
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        // Intent para o BroadcastReceiver que irá lidar com a notificação diária
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), DAILY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Cancelar a notificação diária
        alarmManager.cancel(pendingIntent);
    }
    private void scheduleWeeklyNotification() {
        // Obter o AlarmManager do sistema
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Intent para o BroadcastReceiver que irá lidar com a notificação semanal
        Intent intent = new Intent(requireContext(), WeeklyReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), WEEKLY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calcular o tempo para a notificação semanal (por exemplo, todas as segundas-feiras às 6:00 AM)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Agendar a notificação semanal
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }


    private void cancelWeeklyNotification() {
        // Obter o AlarmManager do sistema
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Intent para o BroadcastReceiver que irá lidar com a notificação semanal
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), WEEKLY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancelar a notificação semanal
        alarmManager.cancel(pendingIntent);
    }

    private void scheduleDailyNotification() {
        // Obter o AlarmManager do sistema
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Intent para o BroadcastReceiver que irá lidar com a notificação diária
        Intent intent = new Intent(requireContext(), DailyReceiver.class);
        // Passar a mensagem da notificação como um extra para a Intent
        intent.putExtra("notification_message", "Bom dia!! Vamos para mais um dia abençoado com muitas tarefas");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), DAILY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calcular o tempo para a notificação diária (por exemplo, todas as 8:00 da manhã)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Agendar a notificação diária
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    public void onPause() {
        super.onPause();
        // Salvar o estado atual dos switches quando o fragmento não está mais visível
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH_DIARIO_STATE, switchDiario.isChecked());
        editor.putBoolean(SWITCH_SEMANAL_STATE, switchSemanal.isChecked());
        editor.apply();
    }

    private void excluirConta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmação");
        builder.setMessage("Tem certeza que deseja excluir sua conta? Esta ação é irreversível.");

        // Adiciona botões "Sim" e "Cancelar" ao diálogo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // O usuário confirmou a exclusão da conta, proceda com a exclusão
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Conta deletada.");
                                // Redireciona para a tela de login após excluir a conta
                                Intent redirecionar = new Intent(getContext(), LoginActivity.class);
                                redirecionar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(redirecionar);
                                finishAffinity(getActivity());
                            } else {
                                Log.e("TAG", "Falha ao excluir conta.");
                            }
                        }
                    });
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // O usuário cancelou a exclusão da conta, não faça nada
                dialog.dismiss();
            }
        });

        // Cria e exibe o diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
