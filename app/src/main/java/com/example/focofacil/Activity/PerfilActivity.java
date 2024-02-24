package com.example.focofacil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.focofacil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilActivity extends AppCompatActivity {
    private TextView txtNome, txtEmail, txtEditarNome, txtEditarSenha, txtEditarEmail;
    private ImageView imageFoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        imageFoto = findViewById(R.id.imageFoto);
        txtEditarEmail = findViewById(R.id.txtEditarEmail);
        txtEditarNome = findViewById(R.id.txtEditarNome);
        txtEditarSenha = findViewById(R.id.txtEditarSenha);

        txtEditarNome.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, EditarNomeActivity.class);
            startActivity(redirecionar);
        });

        txtEditarEmail.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, EditarEmailActivity.class);
            startActivity(redirecionar);
        });

        txtEditarSenha.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, EditarSenhaActivity.class);
            startActivity(redirecionar);
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nome = user.getDisplayName();
            String email = user.getEmail();
            txtNome.setText(nome);
            txtEmail.setText(email);

            Uri fotoUrl = user.getPhotoUrl();
            if (fotoUrl != null) {
                Glide.with(this).load(fotoUrl).into(imageFoto);
            } else {
                imageFoto.setImageResource(R.drawable.img_group142);
            }
        }
    }
}