package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focofacil.Bd.ConfigureBd;
import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.intellij.lang.annotations.Pattern;

public class RecuperarSenhaActivity extends AppCompatActivity {

    EditText edtEmail;
    Button btnRecuperar;
    TextView txtCadTela;
    FirebaseAuth auth;
    ConfigureBd bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        auth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        btnRecuperar =findViewById(R.id.btnRecuperar);
        txtCadTela = findViewById(R.id.txtCadTela);


        btnRecuperar.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            recuperarSenha(email);
        });

        txtCadTela.setOnClickListener(v -> {
            Intent redirecionar = new Intent(this, CadastrarActivity.class);
            startActivity(redirecionar);
        });

    }

    private void recuperarSenha(String email) {

        if (email.isEmpty()) {
            Toast.makeText(this, "Campo email vazio", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "O email não é válido", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Verificação de Cadastro
        if (!isUserRegistered(email)) {
            Toast.makeText(this, "Conta não cadastrada", Toast.LENGTH_LONG).show();
            return;
        }*/

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarSenhaActivity.this, "Instruções encaminhadas para " + email, Toast.LENGTH_LONG).show();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("user-not-found")) {
                                Toast.makeText(RecuperarSenhaActivity.this, "Conta não cadastrada", Toast.LENGTH_LONG).show();
                            } else if (errorMessage.contains("network")) {
                                Toast.makeText(RecuperarSenhaActivity.this, "Falha na conexão de rede. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RecuperarSenhaActivity.this, "Falha ao enviar instruções" + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}