package com.example.focofacil;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "senha_hashed")
    private String senhaHashed;

    public User() {
        // Construtor vazio necessário para o Room
    }

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        setSenhaHashed(senha);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHashed() {
        return senhaHashed;
    }

    public void setSenhaHashed(String senha) {
        if (senha != null) {
            this.senhaHashed = PasswordUtils.generateSecurePassword(senha);
        }
    }

    public boolean verifyPassword(String senha) {
        return senha != null && PasswordUtils.verifyPassword(senha, senhaHashed);
    }
}
