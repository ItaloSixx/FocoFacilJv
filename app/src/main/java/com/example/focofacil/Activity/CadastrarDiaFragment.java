package com.example.focofacil.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.focofacil.R;

public class CadastrarDiaFragment extends Fragment {
     Toolbar toolbar;
     private TextView txtOl;
     private TextView txtNomeUsuario;
     ImageView imageGroup142;
     Spinner diaSpinner;
     private EditText etGroupTwentyNine;
     private EditText etGroupThirtyThree;
     private EditText etGroupThirtyOne;
     private Button btnAdicionarTarefa;

     public CadastrarDiaFragment(){

     }

    public static CadastrarDiaFragment newInstance() {
        return new CadastrarDiaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_cadastrar_dia, container, false);

        // Inicializa os elementos do layout
        toolbar = view.findViewById(R.id.toolbarToolbar);
        txtOl = view.findViewById(R.id.txtOl);
        txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        imageGroup142 = view.findViewById(R.id.imageGroup142);
        diaSpinner = view.findViewById(R.id.diaSpinner);
        etGroupTwentyNine = view.findViewById(R.id.txtAssunto);
        etGroupThirtyThree = view.findViewById(R.id.txtDescricao);
        etGroupThirtyOne = view.findViewById(R.id.txtHora);
        btnAdicionarTarefa = view.findViewById(R.id.btnAddTarefa);

        return view;
    }



}