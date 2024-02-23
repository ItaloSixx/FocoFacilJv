package com.example.focofacil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class CadastrarDiaFragment extends Fragment {
    private Toolbar toolbar;
    private TextView txtOl;
    private TextView txtNomeUsuario;
    private ImageView imageGroup142;
    private Spinner diaSpinner;
    private TextView txtSistemasEmbarc;
    private TextView txtHoras;
    private TextView txt10201200;
    private View lineLineOne;
    private View lineLineTwo;
    private TextView txtProtocolosWeb;
    private TextView txtHorasOne;
    private TextView txt15301710;
    private TextView txtAdicionarclass;
    private EditText etGroupTwentyNine;
    private EditText etGroupThirtyThree;
    private EditText etGroupThirtyOne;
    private AppCompatButton btnAdicionarClasseOne;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_dia, container, false);

        toolbar = view.findViewById(R.id.toolbarToolbar);
        txtOl = view.findViewById(R.id.txtOl);
        txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        imageGroup142 = view.findViewById(R.id.imageGroup142);
        diaSpinner = view.findViewById(R.id.diaSpinner);
        txtSistemasEmbarc = view.findViewById(R.id.txtSistemasEmbarc);
        txtHoras = view.findViewById(R.id.txtHoras);
        txt10201200 = view.findViewById(R.id.txt10201200);
        lineLineOne = view.findViewById(R.id.lineLineOne);
        lineLineTwo = view.findViewById(R.id.lineLineTwo);
        txtProtocolosWeb = view.findViewById(R.id.txtProtocolosWeb);
        txtHorasOne = view.findViewById(R.id.txtHorasOne);
        txt15301710 = view.findViewById(R.id.txt15301710);
        txtAdicionarclass = (AppCompatButton) view.findViewById(R.id.btnAdicionarClasseOne);// Corrigido para AppCompatButton
        etGroupTwentyNine = view.findViewById(R.id.etGroupTwentyNine);
        etGroupThirtyThree = view.findViewById(R.id.etGroupThirtyThree);
        etGroupThirtyOne = view.findViewById(R.id.etGroupThirtyOne);
        btnAdicionarClasseOne = view.findViewById(R.id.btnAdicionarClasseOne);

        return view;
    }
}