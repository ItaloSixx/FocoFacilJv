package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificarEmailActivity extends AppCompatActivity {

    Button btnReenviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_email);

        btnReenviar = findViewById(R.id.btnReenviar);

        btnReenviar.setOnClickListener(v -> {
            reenviarEmail();
        });
    }

    private void reenviarEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(VerificarEmailActivity.this, "Email de verificação enviado", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(VerificarEmailActivity.this, "Falha ao enviar, tente novamente em 15 segundos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}