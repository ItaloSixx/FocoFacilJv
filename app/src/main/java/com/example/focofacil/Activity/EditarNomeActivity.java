package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditarNomeActivity extends AppCompatActivity {

    EditText edtNome;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nome);

         toolbar = findViewById(R.id.toolbar);
         edtNome = findViewById(R.id.edtNome);

         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

         String nome1 = user.getDisplayName();
         edtNome.setText(nome1);

         toolbar.findViewById(R.id.check).setOnClickListener(v -> {
             String nome = edtNome.getText().toString();
             alterarNome(nome);
         });

         toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void alterarNome(String nome) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest updatePerfil = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();
        user.updateProfile(updatePerfil)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                        }else{
                            Toast.makeText(EditarNomeActivity.this, "Ocorreu um erro ao alterar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}