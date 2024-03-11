package com.example.focofacil.adapters;

//import static android.support.v4.media.MediaBrowserCompatApi23.getItem;
//import static com.example.focofacil.Activity.HomeFragment.confirmarExclusao;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focofacil.Activity.EditarTarefaFragment;
import com.example.focofacil.Activity.HomeFragment;
import com.example.focofacil.Activity.TarefaFirebase;
import com.example.focofacil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TarefaFirebaseAdapter extends RecyclerView.Adapter<TarefaFirebaseAdapter.MyViewHolder> {
    private Context context;
    private List<TarefaFirebase> tarefaList;

    public void setSearchList(List<TarefaFirebase> tarefaFirebaseList) {
        this.tarefaList = tarefaFirebaseList;
        notifyDataSetChanged();
    }

    public TarefaFirebaseAdapter(Context context, List<TarefaFirebase> tarefaList) {
        this.context = context;
        this.tarefaList = tarefaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarefa, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtTarefaTitulo.setText(tarefaList.get(holder.getAdapterPosition()).getTitulo());
        holder.txtTarefaDescricao.setText(tarefaList.get(holder.getAdapterPosition()).getDescricao());
        holder.txtDataHora.setText(tarefaList.get(holder.getAdapterPosition()).getDataHora());

        holder.recyclerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Criar uma instância do Fragment que você deseja abrir
                EditarTarefaFragment editarTarefaFragment = new EditarTarefaFragment();

                // Passar os dados necessários para o Fragment (opcional)
                Bundle bundle = new Bundle();
                bundle.putString("Titulo", tarefaList.get(holder.getAdapterPosition()).getTitulo());
                bundle.putString("Descricao", tarefaList.get(holder.getAdapterPosition()).getDescricao());
                bundle.putString("Dia", tarefaList.get(holder.getAdapterPosition()).getDia());
                bundle.putString("Mes", tarefaList.get(holder.getAdapterPosition()).getMes());
                bundle.putString("Ano", tarefaList.get(holder.getAdapterPosition()).getAno());
                editarTarefaFragment.setArguments(bundle);

                Log.e("EditarTarefa", "Título: "+ tarefaList.get(holder.getAdapterPosition()).getTitulo());
                Log.e("EditarTarefa", "Descricao: "+ tarefaList.get(holder.getAdapterPosition()).getDescricao());
                Log.e("EditarTarefa", "Dia: "+ tarefaList.get(holder.getAdapterPosition()).getDia());
                Log.e("EditarTarefa", "Mes: "+ tarefaList.get(holder.getAdapterPosition()).getMes());
                Log.e("EditarTarefa", "Ano: "+ tarefaList.get(holder.getAdapterPosition()).getAno());

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Substitua R.id.fragment_container pelo ID do contêiner real na sua atividade
                fragmentTransaction.replace(R.id.fragment_container, new EditarTarefaFragment());

                fragmentTransaction.commit();


            }
        });

        holder.btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExclusao(tarefaList.get(holder.getAdapterPosition()), holder.itemView.getContext());
            }
        });

    }

    @Override
    public int getItemCount() {
        return tarefaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editTarefaTitlo, editTarefaDescricao, editHorario, editData;
        TextView txtTarefaTitulo, txtTarefaDescricao, txtDataHora, txtTarefaDia, txtTarefaMes, txtTarefaAno;
        CardView recyclerCard;
        Button btnApagar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTarefaTitulo = itemView.findViewById(R.id.txtTarefaTitulo);
            txtTarefaDescricao = itemView.findViewById(R.id.txtTarefaDescricao);
            txtDataHora = itemView.findViewById(R.id.txtDataHora);
            recyclerCard = itemView.findViewById(R.id.recyclerCard);

            editTarefaTitlo = itemView.findViewById(R.id.edtTitulo);
            editTarefaDescricao = itemView.findViewById(R.id.edtDescricao);
            txtTarefaDia = itemView.findViewById(R.id.txtTarefaDia);
            txtTarefaMes = itemView.findViewById(R.id.txtTarefaMes);
            txtTarefaAno = itemView.findViewById(R.id.txtTarefaAno);

            btnApagar = itemView.findViewById(R.id.btnApagar);

        }

    }

    public static void confirmarExclusao(TarefaFirebase tarefa, Context context) {
        // Inflar o layout do diálogo de confirmação
        View dialogView = LayoutInflater.from(context).inflate(R.layout.confirmar_exclusao, null);

        // Configurar o construtor do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        // Criar o AlertDialog
        AlertDialog dialog = builder.create();

        // Obter referências aos botões do diálogo
        Button confirmExcluir = dialogView.findViewById(R.id.confirm_excluir);
        Button cancelarExcluir = dialogView.findViewById(R.id.cancelar_excluir);

        // Configurar o clique no botão de confirmação
        confirmExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui você pode adicionar a lógica para excluir a tarefa
                // No exemplo, estamos apenas imprimindo uma mensagem
                System.out.println("Excluir tarefa: " + tarefa.getTitulo());

                // Obter a referência do nó no Firebase
                DatabaseReference tarefaRef = FirebaseDatabase.getInstance().getReference().child("Tarefas").child(tarefa.getIdTarefa());
                // Remover a tarefa do Firebase
                tarefaRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Tarefa removida com sucesso
                            Toast.makeText(context, "Tarefa removida", Toast.LENGTH_SHORT).show();
                        } else {
                            // Ocorreu um erro ao remover a tarefa
                            Toast.makeText(context, "Erro ao remover tarefa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Fechar o diálogo após a confirmação
                dialog.dismiss();
            }
        });

        // Configurar o clique no botão de cancelamento
        cancelarExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fechar o diálogo se o usuário cancelar
                dialog.dismiss();
            }
        });

        // Exibir o diálogo
        dialog.show();
    }

}