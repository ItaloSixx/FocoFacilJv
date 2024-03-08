package com.example.focofacil.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.focofacil.R;
import com.example.focofacil.adapters.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastrarDiaFragment extends Fragment {
    private static final String TAG = "CadastrarDiaFragment";

    private Toolbar toolbar;
    private ViewModelProvider viewModelProvider;
    FirebaseDatabase database;
    private ActivityCadastrarDiaViewModel viewModel;

    private EditText editTextAtividade;
    private EditText editTextTituloAtividade;
    private Spinner spinnerRepeticao;

    private Button buttonOK;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_dia, container, false);
        database = FirebaseDatabase.getInstance();
        viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(ActivityCadastrarDiaViewModel.class);
        toolbar = view.findViewById(R.id.toolbarToolbar);
        editTextAtividade = view.findViewById(R.id.editTextAtividade);
        editTextTituloAtividade = view.findViewById(R.id.editTexttituloAtividade);
        spinnerRepeticao = view.findViewById(R.id.spinnerRepeticao);



        Button buttonOpenCalendarDialog = view.findViewById(R.id.buttonOpenCalendarDialog);
        Button buttonOpenTimePickerDialog = view.findViewById(R.id.buttonOpenTimePickerDialog);

        view.findViewById(R.id.buttonSalvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistirTarefa();

            }
        });

        buttonOpenCalendarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.show(getParentFragmentManager(), "datePicker");
            }
        });

        buttonOpenTimePickerDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
                timePickerDialogFragment.show(getParentFragmentManager(), "timePicker");
            }
        });

        // Configurar spinner com opções de repetição
        Spinner spinner = view.findViewById(R.id.spinnerRepeticao);
        List<String> items = new ArrayList<>();
        items.add("Nunca");
        items.add("Diáriamente");
        items.add("Semanalmente");
        items.add("Mensalmente");
        items.add("Personalisado");

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(requireActivity(), items);
        spinner.setAdapter(adapter);

        // Configurar calendarView

        // Observar data selecionada e atualizar tela

        viewModel.getDataSelecionada().observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                // Atualizar a tela com as atividades do dia selecionado
            }
        });
        viewModel.carregarDados();

        // Recuperar preferências compartilhadas
        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        return view;
    }

    private void persistirTarefa() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario = user.getUid();

        Calendar dataSelecionada = viewModel.getDataSelecionada().getValue();
        if (dataSelecionada != null) {
            String descricao = editTextAtividade.getText().toString();
            String titulo = editTextTituloAtividade.getText().toString();
            int repeticao = spinnerRepeticao.getSelectedItemPosition();

            Map<String, Object> atividade = new HashMap<>();
            atividade.put("titulo", titulo);
            atividade.put("descricao", descricao);
            atividade.put("repeticao", repeticao);
            atividade.put("dataSelecionada", dataSelecionada.getTime());
            atividade.put("idUsuario", idUsuario);

            // Gerar um novo nó com ID único para a tarefa
            DatabaseReference tarefaRef = FirebaseDatabase.getInstance().getReference("Tarefas").child(user.getUid()).push();

            // Definir os valores da tarefa no novo nó
            tarefaRef.setValue(atividade)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(requireContext(), "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().beginTransaction().remove(CadastrarDiaFragment.this).commit();

                            } else {
                                Toast.makeText(requireContext(), "Tente novamente"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



    private MutableLiveData<Calendar> dataSelecionada;
    public LiveData<Calendar> getDataSelecionada() {
        if (dataSelecionada == null) {
            dataSelecionada = new MutableLiveData<>();
            dataSelecionada.setValue(Calendar.getInstance());
        }
        return dataSelecionada;
    }


}
