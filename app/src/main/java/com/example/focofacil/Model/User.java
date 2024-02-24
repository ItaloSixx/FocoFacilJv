package com.example.focofacil.Model;
import com.example.focofacil.Utils.PasswordUtils;

import java.io.Serializable;
import java.util.UUID;


public class User implements Serializable {

    private String id;

    private String nome;

    private String email;

    private String senhaHashed;

    public User(){

    }

    public User(String nome, String email, String senhaHashed) {
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
