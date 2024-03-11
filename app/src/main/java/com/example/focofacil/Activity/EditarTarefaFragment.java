package com.example.focofacil.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focofacil.R;
import com.example.focofacil.adapters.TarefaFirebaseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class EditarTarefaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TAREFA = "tarefa";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TarefaFirebase tarefa;

    TextView txtNomeUsuario, txtTarefaDia, txtTarefaMes, txtTarefaAno, txtData;
    EditText edtTitulo, edtDescricao;
    Button btnEdit, btnHorario;

    public static EditarTarefaFragment newInstance(TarefaFirebase tarefa) {
        EditarTarefaFragment fragment = new EditarTarefaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAREFA, (Serializable) tarefa);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_tarefa, container, false);

        //txtNomeUsuario = view.findViewById(R.id.txtNome1);
        txtTarefaDia = view.findViewById(R.id.txtTarefaDia);
        txtTarefaMes = view.findViewById(R.id.txtTarefaMes);
        txtTarefaAno = view.findViewById(R.id.txtTarefaAno);
        edtTitulo = view.findViewById(R.id.edtTitulo);
        edtDescricao = view.findViewById(R.id.edtDescricao);
        btnHorario = view.findViewById(R.id.btnHorario);
        btnEdit = view.findViewById(R.id.btnEdit);


        // Obter os argumentos passados para este fragmento, se houver
        Bundle bundle = getArguments();
        if (tarefa  != null) {
            edtTitulo.setText(tarefa.getTitulo());
            edtDescricao.setText(tarefa.getDescricao());
            txtTarefaDia.setText(tarefa.getDia());
            txtTarefaMes.setText(tarefa.getMes());
            txtTarefaAno.setText(tarefa.getAno());

        }

        // Configurar o botão de edição
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aqui você pode adicionar a lógica para editar a tarefa
                // Por exemplo, você pode chamar um método em uma classe de gerenciamento de tarefas para atualizar a tarefa no banco de dados
                // Exemplo de chamada de método fictício:
                // editarTarefa();
            }
        });

        return view;
    }

    // Método para editar a tarefa
    private void editarTarefa() {
        // Aqui você implementa a lógica para editar a tarefa
        // Por exemplo, você pode obter os valores dos campos de texto (edtTitulo e edtDescricao) e atualizar a tarefa no banco de dados
        // Após a edição, você pode exibir uma mensagem para o usuário informando que a tarefa foi editada com sucesso
        Toast.makeText(requireContext(), "Tarefa editada com sucesso", Toast.LENGTH_SHORT).show();
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
            tarefa = (TarefaFirebase) getArguments().getSerializable(ARG_TAREFA);
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