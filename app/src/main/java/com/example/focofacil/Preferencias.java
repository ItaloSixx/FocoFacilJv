package com.example.focofacil;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;
@Entity
public class Preferencias implements Serializable {
    @ColumnInfo(name = "notificarDia")
    private Boolean notificarDia;
    @ColumnInfo(name = "notificarSemana")
    private Boolean notificarSemana;
    @ColumnInfo(name = "notificarHorario")
    private Boolean notificarHorario;

    public Preferencias(Boolean notificarDia, Boolean notificarSemana, Boolean notificarHorario) {
        this.notificarDia = notificarDia;
        this.notificarSemana = notificarSemana;
        this.notificarHorario = notificarHorario;
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
