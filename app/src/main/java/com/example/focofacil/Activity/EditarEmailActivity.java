package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditarEmailActivity extends AppCompatActivity {

    EditText edtEmail, edtEmailConfirm;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_email);

        toolbar = findViewById(R.id.toolbar);
        edtEmail = findViewById(R.id.edtEmail);
        edtEmailConfirm = findViewById(R.id.edtEmailConfirm);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String emailedt = user.getEmail();
        edtEmail.setText(emailedt);

        toolbar.findViewById(R.id.check).setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String emailConfirm = edtEmailConfirm.getText().toString();
            if(email.equals(emailConfirm)){
                atualizarEmail(email);
            }else{
                Toast.makeText(this, "Os emails não correspondem.", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void atualizarEmail(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        long tempoUltimoLogin = user.getMetadata().getLastSignInTimestamp();
        long tempoAtual = System.currentTimeMillis();

        boolean precisaReautenticar = (tempoAtual - tempoUltimoLogin) > (1000 * 60 * 10);
        if(precisaReautenticar){
            Toast.makeText(this, "Reauntenticação necessária", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();
            Intent redirecionar = new Intent(EditarEmailActivity.this, LoginActivity.class);
            redirecionar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(redirecionar);
            finishAffinity();
        }else{
            if(user != null){
                user.verifyBeforeUpdateEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditarEmailActivity.this, "Email de verificação enviado para " + email, Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent redirecionar = new Intent(EditarEmailActivity.this, LoginActivity.class);
                                    redirecionar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(redirecionar);
                                    finishAffinity();
                                }else{
                                    Toast.makeText(EditarEmailActivity.this, "Falha ao enviar" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}