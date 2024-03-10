package com.example.focofacil.Activity;

import androidx.room.ColumnInfo;

public class TarefaFirebase {
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "descricao")
    private String descricao;
    @ColumnInfo(name = "repeticao")
    private String repeticao;
    @ColumnInfo(name = "dia")
    private String dia;
    @ColumnInfo(name = "mes")
    private String mes;
    @ColumnInfo(name = "ano")
    private String ano;
    @ColumnInfo(name = "hora")
    private String hora;
    @ColumnInfo(name = "minuto")
    private String minuto;

    public TarefaFirebase(String titulo, String descricao, String repeticao, String dia, String mes, String ano, String hora, String minuto) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.repeticao = repeticao;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
    }
    public TarefaFirebase(){

    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRepeticao() {
        return repeticao;
    }

    public void setRepeticao(String repeticao) {
        this.repeticao = repeticao;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }
}
