package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.focofacil.Model.TarefaFirebase;
import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class EditarEmailActivity extends AppCompatActivity {

    EditText edtEmail, edtEmailConfirm;
    Toolbar toolbar;
    Button btnprocurar;
    private ArrayList<String> taref;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private static final String TAG = "PerfilFragment";

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


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = mAuth.getCurrentUser();
        final FirebaseUser finalUser = FirebaseAuth.getInstance().getCurrentUser();
        btnprocurar = findViewById(R.id.btnprocurar);
        btnprocurar.setOnClickListener(v -> {
            Toast.makeText(EditarEmailActivity.this, "SDSADASDASD", Toast.LENGTH_SHORT).show();
            if (finalUser != null) {
                finalUser.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = finalUser.getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tarefas");

                        Log.d("TAG", "Iniciando consulta ao Firebase Realtime Database...");

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, "Consulta ao Firebase Realtime Database concluída com sucesso!");
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String taskUserId = snapshot.child("idUsuario").getValue(String.class);
                                    if (taskUserId != null && taskUserId.equals(uid)) {
                                        // Esta tarefa pertence ao usuário logado
                                        // Faça o que você precisa com a tarefa aqui
                                        String taskTitle = snapshot.child("titulo").getValue(String.class);
                                        String taskDescription = snapshot.child("descricao").getValue(String.class);
                                        Long diaLong = snapshot.child("dia").getValue(Long.class);

                                        Log.d("TAG", "Título da Tarefa: " + taskTitle);
                                        Toast.makeText(EditarEmailActivity.this, "Tarefa:" + taskTitle, Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "Descrição da Tarefa: " + taskDescription);
                                        Log.d("TAG", "Dia da Tarefa: " + diaLong);
                                        // Adicione o código para processar os outros campos da tarefa conforme necessário
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(TAG, "Erro ao consultar o Firebase Realtime Database: " + databaseError.getMessage());
                                Toast.makeText(EditarEmailActivity.this, "Falha ao consultar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e(TAG, "Falha ao recarregar o usuário: ", task.getException());
                    }
                });
            } else {
                Log.e(TAG, "Usuário atual é nulo.");
            }
        });




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