package com.example.focofacil.adapters;

//import static android.support.v4.media.MediaBrowserCompatApi23.getItem;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.txtTarefaTitulo.setText(tarefaList.get(position).getTitulo());
        holder.txtTarefaDescricao.setText(tarefaList.get(position).getDescricao());
        holder.txtDataHora.setText(tarefaList.get(position).getDataHora());

    }

    @Override
    public int getItemCount() {
        return tarefaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTarefaTitulo, txtTarefaDescricao, txtDataHora;
        CardView recyclerCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTarefaTitulo = itemView.findViewById(R.id.txtTarefaTitulo);
            txtTarefaDescricao = itemView.findViewById(R.id.txtTarefaDescricao);
            txtDataHora = itemView.findViewById(R.id.txtDataHora);
            recyclerCard = itemView.findViewById(R.id.recyclerCard);
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