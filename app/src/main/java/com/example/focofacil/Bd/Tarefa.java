package com.example.focofacil.Bd;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
public class Tarefa implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "assunto")
    private String assunto;
    @ColumnInfo(name = "descricao")
    private String descricao;
    @ColumnInfo(name = "dataHora")
    private Date dataHora;

    public Tarefa(){
        this.id = UUID.randomUUID().toString();
    }

    public Tarefa(String descricao, String comentario, Date dataHora) {
        this.id = UUID.randomUUID().toString();
        this.descricao = descricao;
        this.assunto = comentario;
        this.dataHora = dataHora;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }
}
