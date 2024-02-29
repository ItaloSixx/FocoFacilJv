package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditarSenhaActivity extends AppCompatActivity {

    EditText edtSenha;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_senha);

        edtSenha = findViewById(R.id.edtSenha);
        toolbar = findViewById(R.id.toolbar);


        toolbar.findViewById(R.id.check).setOnClickListener(v -> {
            String senha = edtSenha.getText().toString();
            mudarSenha(senha);

        });

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void mudarSenha(String senha) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        long tempoUltimoLogin = user.getMetadata().getLastSignInTimestamp();
        long tempoAtual = System.currentTimeMillis();

        boolean precisaReautenticar = (tempoAtual - tempoUltimoLogin) > (1000 * 60 * 10);
        if(precisaReautenticar){
            Toast.makeText(this, "Reauntenticação necessária", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();
            Intent redirecionar = new Intent(EditarSenhaActivity.this, LoginActivity.class);
            redirecionar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(redirecionar);
            finishAffinity();
        }else {
            if(user != null){
                user.updatePassword(senha)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditarSenhaActivity.this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EditarSenhaActivity.this, "Falha ao alterar senha" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "Falha senha: " + task.getException().getMessage());
                                }
                        }
                });
            }

        }
    }


}