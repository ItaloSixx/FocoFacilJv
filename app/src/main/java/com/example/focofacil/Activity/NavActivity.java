package com.example.focofacil.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.focofacil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavActivity extends AppCompatActivity {

    TextView txtNome, txtEmail;
    ImageView fotoPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        txtNome = findViewById(R.id.txtNomeNav);
        txtEmail = findViewById(R.id.txtEmailNav);
        fotoPerfil = findViewById(R.id.fotoPerfilNav);

        mostrarPerfil();

    }

    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            String email = user .getEmail();
            txtNome.setText(nome);
            txtEmail.setText(email);

            Uri fotoUrl = user.getPhotoUrl();
            if(fotoUrl != null){
                Glide.with(this).load(fotoUrl).into(fotoPerfil);
            }else{
                fotoPerfil.setImageResource(R.drawable.fotopadrao);
            }
        }
    }
}