package com.example.focofacil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class ConfiguracoesActivity extends AppCompatActivity {

    Switch notificarDia;
    Switch notificarHorario;
    Switch notificarSemana;
    MyDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        notificarDia = findViewById(R.id.Dia);
        notificarHorario = findViewById(R.id.Horario);
        notificarSemana = findViewById(R.id.Semana);

        // Definindo os Switches como true por padrão
        notificarDia.setChecked(true);
        notificarHorario.setChecked(true);
        notificarSemana.setChecked(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Criando uma instância de Preferencias com os valores padrão
                Preferencias preferencias = new Preferencias(true, true, true);

                // Inicializando o banco de dados
                db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "FocoFacilBD").build();

                // Definindo o listener para o Switch notificarDia
                notificarDia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Atualizando a preferência notificarDia com o estado do Switch
                        preferencias.setNotificarDia(isChecked);
                        // Salvando as preferências atualizadas no banco de dados
                        new SalvarPreferenciasTask().execute(preferencias);
                    }
                });

                // Definindo o listener para o Switch notificarHorario
                notificarHorario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Atualizando a preferência notificarHorario com o estado do Switch
                        preferencias.setNotificarHorario(isChecked);
                        // Salvando as preferências atualizadas no banco de dados
                        new SalvarPreferenciasTask().execute(preferencias);
                    }
                });

                // Definindo o listener para o Switch notificarSemana
                notificarSemana.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Atualizando a preferência notificarSemana com o estado do Switch
                        preferencias.setNotificarSemana(isChecked);
                        // Salvando as preferências atualizadas no banco de dados
                        new SalvarPreferenciasTask().execute(preferencias);
                    }
                });
            }
        }).start();

    }

    // Método para salvar as preferências no banco de dados
    private class SalvarPreferenciasTask extends AsyncTask<Preferencias, Void, Void> {
        @Override
        protected Void doInBackground(Preferencias... preferencias) {
            UserDao userDao = db.userDao();
            // Verifique se as preferências já existem no banco de dados
            Preferencias preferenciasExistente = userDao.getPreferencias();
            if (preferenciasExistente != null) {
                userDao.updatePreferencias(preferencias[0]);
            } else {
                // Se as preferências ainda não existirem, insira-as no banco de dados
                userDao.insertPreferencias(preferencias[0]);
            }
            return null;
        }
    }
}

