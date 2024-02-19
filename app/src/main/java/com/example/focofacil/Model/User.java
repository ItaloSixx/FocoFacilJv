package com.example.focofacil.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.focofacil.Utils.PasswordUtils;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class User implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "senha_hashed")
    private String senhaHashed;

    public User() {
        // Construtor vazio necessário para o Room
        this.id = UUID.randomUUID().toString();
    }

    public User(String nome, String email, String senhaHashed) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.email = email;
        this.senhaHashed = senhaHashed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
