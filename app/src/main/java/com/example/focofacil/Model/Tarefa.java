package com.example.focofacil.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"))
public class Tarefa implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String tarefaId;
    @NonNull
    private String userId;

    @ColumnInfo(name = "assunto")
    private String assunto;
    @ColumnInfo(name = "descricao")
    private String descricao;
    @ColumnInfo(name = "dataHora")
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

    @NonNull
    public String getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(@NonNull String tarefaId) {
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
