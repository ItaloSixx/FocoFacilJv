package com.example.focofacil;

import java.util.ArrayList;

public class DiaDaSemana {
    private String nomeDia;
    private ArrayList<Tarefa> listaDeTarefas;

    public DiaDaSemana(String nomeDia, ArrayList<Tarefa> listaDeTarefas) {
        this.nomeDia = nomeDia;
        this.listaDeTarefas = listaDeTarefas;
    }
    public String getNomeDia() {

        return nomeDia;
    }
    public void setNomeDia(String nomeDia) {
        this.nomeDia = nomeDia;
    }
    public ArrayList<Tarefa> getListaDeTarefas() {
        return listaDeTarefas;
    }
    public void setListaDeTarefas(ArrayList<Tarefa> listaDeTarefas) {
        this.listaDeTarefas = listaDeTarefas;
    }
}
