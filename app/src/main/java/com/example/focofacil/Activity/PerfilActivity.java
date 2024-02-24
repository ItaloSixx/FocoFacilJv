package com.example.focofacil.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.focofacil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilActivity extends AppCompatActivity {
    private TextView txtNome, txtEmail;
    private ImageView imageFoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        imageFoto = findViewById(R.id.imageFoto);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nome = user.getDisplayName();
            String email = user.getEmail();

            if (nome != null) {
                txtNome.setText(nome);
            }
            if (email != null) {
                txtEmail.setText(email);
            }

            Uri fotoUrl = user.getPhotoUrl();
            if (fotoUrl != null) {
                Glide.with(this).load(fotoUrl).into(imageFoto);
            } else {
                // Se o usuário não tiver uma foto de perfil, você pode exibir uma imagem padrão
                imageFoto.setImageResource(R.drawable.img_group142);
            }
        }
    }
}