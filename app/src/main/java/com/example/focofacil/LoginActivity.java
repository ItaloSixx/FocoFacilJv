package com.example.focofacil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnLoginGoogle;;
    TextView txtCadTela, txtEsqueceuSenha;
    EditText edtEmail, edtSenha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLogGoogle);
        txtCadTela = findViewById(R.id.txtCadTela);
        txtEsqueceuSenha = findViewById(R.id.txtEsqueceuSenha);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);


        txtCadTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirecionar = new Intent(LoginActivity.this, CadastrarActivity.class);
                startActivity(redirecionar);
            }
        });





    }


}