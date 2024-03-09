package com.example.focofacil.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class ActivityCadastrarDiaViewModel extends ViewModel {

    private MutableLiveData<Calendar> dataSelecionada = new MutableLiveData<>(Calendar.getInstance());



    public ActivityCadastrarDiaViewModel() {
        carregarDados();
    }

    public void carregarDados() {
        // Carregar dados do banco de dados ou de outra fonte

        // Atualizar a lista de atividades para a data atual
    }

    public void setDataSelecionada(int year, int month, int dayOfMonth) {
        Calendar data = Calendar.getInstance();
        data.set(year, month, dayOfMonth);

        dataSelecionada.setValue(data);

        // Atualizar a lista de atividades para a data selecionada
    }

    public MutableLiveData<Calendar> getDataSelecionada() {
        return dataSelecionada;
    }

    public void setHoraSelecionada(int hourOfDay, int minute) {
        Calendar dataSelecionada = getDataSelecionada().getValue();
        if (dataSelecionada != null) {
            dataSelecionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dataSelecionada.set(Calendar.MINUTE, minute);
            // Notifique os observadores sobre a mudança na data selecionada
        }
    }

}