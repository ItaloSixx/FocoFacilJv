package com.example.focofacil.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.focofacil.Bd.MyDatabase;
import com.example.focofacil.Dao.TarefaDao;
import com.example.focofacil.Bd.MyDatabase;
import com.example.focofacil.Model.Tarefa;
import com.example.focofacil.R;

import java.util.Date;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText editTextDescricao;
    private EditText editTextAssunto;
    private Button buttonAdicionar;
    private Button buttonExcluir;

    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        editTextDescricao = findViewById(R.id.edtDescricao);
        buttonAdicionar = findViewById(R.id.btnAdd);
        buttonExcluir = findViewById(R.id.btnDeletar);

        db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "FocoFacilBD").build();

        String userId = "1cd21507-9bc2-45e8-95cf-32144b8dd9d2";


                buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String descricao = editTextDescricao.getText().toString();
                        Tarefa tarefa = new Tarefa(userId, descricao);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                db.tarefaDao().insert(tarefa);
                                db.tarefaDao().insertTarefaToFirebase(tarefa);
                            }
                        }).start();

                    }
                });



            }



    }
