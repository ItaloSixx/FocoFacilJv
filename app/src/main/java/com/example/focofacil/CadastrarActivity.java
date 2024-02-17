package com.example.focofacil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.UUID;

public class CadastrarActivity extends AppCompatActivity {

    EditText edtNome, edtEmail, edtSenha, edtSenhaConfirm;
    Button btnCadastrar, btnCadGoogle;
    TextView txtLoginTela;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirm = findViewById(R.id.edtSenhaConfirm);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadGoogle = findViewById(R.id.btnCadGoogle);

        db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "FocoFacilBD").build();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String senhaConfirm = edtSenhaConfirm.getText().toString();

                if (senha.equals(senhaConfirm)) {
                    //Iniciando uma nova thread para operações no banco de dados (para não bloquear a UI)
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Capturando novamente os dados dos campos (dentro da thread secundária)
                            String nome = edtNome.getText().toString();
                            String email = edtEmail.getText().toString();
                            String senha = edtSenha.getText().toString();
                            String senhaConfirm = edtSenhaConfirm.getText().toString();

                            //Criando um novo objeto User com os dados fornecidos
                            User user = new User(0, nome, email, senha);

                            // Inserindo o usuário no banco de dados local
                            long userIdLocal = db.userDao().insert(user);

                            // Configurar o id do usuário com o id gerado pelo Room
                            user.setId((int) userIdLocal);

                            // Inserir usuário no Firebase
                            db.userDao().insertUserToFirebase(user);


                            //Atualizando a UI para limpar os campos de entrada (dentro da thread secundária)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edtNome.setText("");
                                    edtEmail.setText("");
                                    edtSenha.setText("");
                                    edtSenhaConfirm.setText("");
                                }
                            });
                        }
                    }).start();
                    Toast.makeText(CadastrarActivity.this, "Conta cadastrada", Toast.LENGTH_SHORT).show();
                    Intent redirecionar = new Intent(CadastrarActivity.this, LoginActivity.class);
                    startActivity(redirecionar);
                }else{
                    Toast.makeText(CadastrarActivity.this, "Senhas não correspondem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}