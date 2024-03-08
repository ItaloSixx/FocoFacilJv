package com.example.focofacil;

import java.util.ArrayList;
import java.util.Date;

public class DiaDaSemana {
    private String nomeDia;
    private ArrayList<Tarefa> listaDeTarefas;
    private Date dataSelecionada;

    public DiaDaSemana(String nomeDia, ArrayList<Tarefa> listaDeTarefas) {
        this.nomeDia = nomeDia;
        this.listaDeTarefas = listaDeTarefas;
    }

    public DiaDaSemana(String nomeDia){
        this.nomeDia = nomeDia;
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
    public void setDataSelecionada(Date dataSelecionada){
        this.dataSelecionada = dataSelecionada;
    }
    public Date getDataSelecionada(){
        return dataSelecionada;
    }
}
