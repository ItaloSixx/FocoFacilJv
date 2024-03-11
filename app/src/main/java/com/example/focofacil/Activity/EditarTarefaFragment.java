package com.example.focofacil.Activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.focofacil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class EditarTarefaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtNomeUsuario, txtDia, txtData;
    EditText edtTitulo, edtDescricao;
    Button btnEdit, btnHorario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_tarefa, container, false);

        //txtNomeUsuario = view.findViewById(R.id.txtNome1);
        txtDia = view.findViewById(R.id.txtDia);
        txtData = view.findViewById(R.id.txtDatapequeno);
        edtTitulo = view.findViewById(R.id.edtTitulo);
        edtDescricao = view.findViewById(R.id.edtDescricao);
        btnHorario = view.findViewById(R.id.btnHorario);
        btnEdit = view.findViewById(R.id.btnEdit);

        //mostrarPerfil();

        return view;
    }

    public EditarTarefaFragment() {
        // Required empty public constructor
    }

    public static EditarTarefaFragment newInstance(String param1, String param2) {
        EditarTarefaFragment fragment = new EditarTarefaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            txtNomeUsuario.setText(nome);
        }
    }

}