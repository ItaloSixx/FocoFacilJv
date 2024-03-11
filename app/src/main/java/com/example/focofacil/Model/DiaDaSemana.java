package com.example.focofacil.Model;

import com.example.focofacil.Activity.TarefaFirebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaDaSemana {
    private String nomeDia;
    private ArrayList<TarefaFirebase> listaDeTarefas;
    private Date dataSelecionada;

    public DiaDaSemana(String nomeDia, ArrayList<TarefaFirebase> listaDeTarefas) {
        this.nomeDia = nomeDia;
        this.listaDeTarefas = listaDeTarefas;
    }

    public DiaDaSemana(String nomeDia) {
        this.nomeDia = nomeDia;
        this.listaDeTarefas = new ArrayList<>(); // Inicializar a lista de tarefas
    }

    public String getNomeDia() {
        return nomeDia;
    }

    public void setNomeDia(String nomeDia) {
        this.nomeDia = nomeDia;
    }

    public ArrayList<TarefaFirebase> getListaDeTarefas() {
        return listaDeTarefas;
    }

    public void setListaDeTarefas(ArrayList<TarefaFirebase> listaDeTarefas) {
        this.listaDeTarefas = listaDeTarefas;
    }

    public void adicionarTarefa(TarefaFirebase tarefa) {
        if (listaDeTarefas == null) {
            listaDeTarefas = new ArrayList<>();
        }
        listaDeTarefas.add(tarefa);
    }

    public void removerTarefa(TarefaFirebase tarefa) {
        listaDeTarefas.remove(tarefa);
    }

    public void setDataSelecionada(Date dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

    public Date getDataSelecionada() {
        return dataSelecionada;
    }

    // Método para obter o dia da semana de uma tarefa
    public String obterDiaDaSemanaDaTarefa(TarefaFirebase tarefa) {
        // Configurar o calendário
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(tarefa.getAno()), Integer.parseInt(tarefa.getMes()) - 1, Integer.parseInt(tarefa.getDia()));

        // Obter o dia da semana em formato textual
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


}
