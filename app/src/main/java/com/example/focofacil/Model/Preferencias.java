package com.example.focofacil.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Preferencias implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "notificarDia") //Assim que o dia virar notificar todas as tarefas
    private Boolean notificarDia;
    @ColumnInfo(name = "notificarSemana") //Assim que a semana virar notificar todas as tarefas
    private Boolean notificarSemana;
    @ColumnInfo(name = "notificarHorario") //Assim que proximo do horario da tarefa, notificar
    private Boolean notificarHorario;

    public Preferencias(Boolean notificarDia, Boolean notificarSemana, Boolean notificarHorario) {
        this.notificarDia = notificarDia;
        this.notificarSemana = notificarSemana;
        this.notificarHorario = notificarHorario;
    }

    public Preferencias() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getNotificarDia() {
        return notificarDia;
    }

    public void setNotificarDia(Boolean notificarDia) {
        this.notificarDia = notificarDia;
    }

    public Boolean getNotificarSemana() {
        return notificarSemana;
    }

    public void setNotificarSemana(Boolean notificarSemana) {
        this.notificarSemana = notificarSemana;
    }

    public Boolean getNotificarHorario() {
        return notificarHorario;
    }

    public void setNotificarHorario(Boolean notificarHorario) {
        this.notificarHorario = notificarHorario;
    }
}
