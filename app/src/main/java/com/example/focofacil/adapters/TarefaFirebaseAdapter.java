package com.example.focofacil.adapters;

//import static android.support.v4.media.MediaBrowserCompatApi23.getItem;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focofacil.Activity.EditarTarefaFragment;
import com.example.focofacil.Activity.HomeFragment;
import com.example.focofacil.Activity.TarefaFirebase;
import com.example.focofacil.R;

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
                bundle.putString("Titulo", tarefaList.get(position).getTitulo());
                bundle.putString("Descricao", tarefaList.get(position).getDescricao());
                bundle.putString("Dia", tarefaList.get(position).getDia());
                bundle.putString("Mes", tarefaList.get(position).getMes());
                bundle.putString("Ano", tarefaList.get(position).getAno());
                editarTarefaFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Substitua R.id.fragment_container pelo ID do contêiner real na sua atividade
                fragmentTransaction.replace(R.id.fragment_container, new EditarTarefaFragment());

                fragmentTransaction.commit();

                // Substituir o Fragment atual pelo novo Fragment
                //FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                //fragmentManager.beginTransaction()
                  //      .replace(R.id.fragment_editar_tarefa, editarTarefaFragment) // Substitua R.id.fragment_container pelo ID do seu contêiner de fragmento
                    //    .addToBackStack(null) // Adicione à pilha de retrocesso se necessário
                      //  .commit();
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

        }

    }
}
/*public TarefaFirebaseAdapter(@NonNull Context context, int resource, @NonNull List<TarefaFirebase> objects) {
        super(context, resource, objects);
        }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tarefa, parent, false);
        }

        TarefaFirebase tarefa = getItem(position);

        if (tarefa != null) {
            TextView txtTarefaTituloSegunda = convertView.findViewById(R.id.txtTarefaTituloSegunda);
            TextView txtTarefaDescricaoSegunda = convertView.findViewById(R.id.txtTarefaDescricaoSegunda);
            TextView txtDataHora = convertView.findViewById(R.id.txtDataHora);

            if (txtTarefaTituloSegunda != null) {
                txtTarefaTituloSegunda.setText(tarefa.getTitulo());

            } else{
                Log.d("TAG", "txtTarefaTituloSegunda é NULO");
            }

            if (txtTarefaDescricaoSegunda != null) {
                txtTarefaDescricaoSegunda.setText(tarefa.getDescricao());
            } else{
                Log.d("TAG", "txtTarefaDescricaoSegunda é NULO");
            }

            if (txtDataHora != null) {
                txtDataHora.setText("Data: " + tarefa.getDia() + "/" + tarefa.getMes() + "/" + tarefa.getAno() + " - " + tarefa.getHora() + ":" + tarefa.getMinuto());
            } else{
                Log.d("TAG", "txtDataHora é NULO");
            }
        }

        return convertView;
        }

}
*/