package com.example.focofacil.Model;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Tarefa implements Serializable {

    private String tarefaId;
    private String userId;

    private String assunto;
    private String descricao;
    private Date dataHora;

    public Tarefa(){
        this.tarefaId = UUID.randomUUID().toString();
    }

    public Tarefa(@NonNull String userId, String descricao) {
        this.tarefaId = UUID.randomUUID().toString();
        this.userId = userId;
        this.assunto = assunto;
        this.descricao = descricao;
        this.dataHora = dataHora;
    }

    public String getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(String tarefaId) {
        this.tarefaId = tarefaId;
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

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}
