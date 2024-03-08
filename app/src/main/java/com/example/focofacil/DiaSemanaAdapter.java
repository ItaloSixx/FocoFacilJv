package com.example.focofacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.focofacil.DiaDaSemana;
import com.example.focofacil.R;
import com.example.focofacil.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DiaSemanaAdapter extends RecyclerView.Adapter<DiaSemanaAdapter.DiaSemanaViewHolder> {

    private final List<DiaDaSemana> listaDeDias;
    private final LayoutInflater inflater;

    public DiaSemanaAdapter(Context context, List<DiaDaSemana> listaDeDias) {
        this.inflater = LayoutInflater.from(context);
        this.listaDeDias = listaDeDias;
    }

    @NonNull
    @Override
    public DiaSemanaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dia_da_semana, parent, false);
        return new DiaSemanaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaSemanaViewHolder holder, int position) {
        DiaDaSemana dia = listaDeDias.get(position);

        // Atualizar as Views do ViewHolder conforme necessário
        holder.txtNomeDia.setText(dia.getNomeDia());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Supondo que haja apenas uma tarefa por dia para simplificar
        if (!dia.getListaDeTarefas().isEmpty()) {
            Tarefa tarefa = dia.getListaDeTarefas().get(0);
            holder.txtTarefaAssunto.setText(tarefa.getAssunto());
            holder.txtTarefaDataHora.setText(sdf.format(tarefa.getDataHora()));
            holder.txtTarefaDescricao.setText(tarefa.getDescricao());
        } else {
            // Se não houver tarefa, você pode ocultar ou exibir uma mensagem indicando que não há tarefa.
            holder.txtTarefaAssunto.setVisibility(View.GONE);
            holder.txtTarefaDataHora.setVisibility(View.GONE);
            holder.txtTarefaDescricao.setText("Nenhuma tarefa para este dia");
        }
    }

    @Override
    public int getItemCount() {
        return listaDeDias != null ? listaDeDias.size() : 0;
    }

    public class DiaSemanaViewHolder extends RecyclerView.ViewHolder {

        TextView txtNomeDia;
        TextView txtTarefaAssunto;
        TextView txtTarefaDataHora;
        TextView txtTarefaDescricao;


        public DiaSemanaViewHolder(@NonNull View itemView) {
            super(itemView);


            txtNomeDia = itemView.findViewById(R.id.txtNomeDia);
            txtTarefaAssunto = itemView.findViewById(R.id.txtTarefaAssunto);
            txtTarefaDataHora = itemView.findViewById(R.id.txtTarefaDataHora);
            txtTarefaDescricao = itemView.findViewById(R.id.txtTarefaDescricao);
        }
    }
}
