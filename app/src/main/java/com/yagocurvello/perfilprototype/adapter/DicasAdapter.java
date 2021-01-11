package com.yagocurvello.perfilprototype.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yagocurvello.perfilprototype.R;
import com.yagocurvello.perfilprototype.activity.JogoActivity;
import com.yagocurvello.perfilprototype.model.Carta;

import java.util.ArrayList;
import java.util.List;

//Definir ViewHolder (para colocar em <>)
public class DicasAdapter extends RecyclerView.Adapter<DicasAdapter.MyViewHolder> {

    Carta carta;
    List<Integer> posDica = new ArrayList<>();
    private static Integer numeroDicas;

    public DicasAdapter(Carta carta) {
        this.carta = carta;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflar um xml para view
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_dicas, parent, false);


        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        //Aqui é onde os itens são mostrados

        //Recebe o texto que será exibido a partir de uma lista (Dicas)
        String dica = carta.getDicas().get(position);
        holder.dica.setText(dica);

        holder.dica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ao clicar em um item, salva a posição do item clicado numa lista posDica
                JogoActivity.chuteRealizado = false;
                posDica.add(position);
                numeroDicas = posDica.size();
                notifyDataSetChanged();

            }
        });

        //Se a posição está contida na lista, colocar bacground transparente, se não, deixar preto
        if (posDica.contains(position)){
            holder.dica.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            holder.dica.setTextColor(Color.parseColor("#000000"));
        }
        else {
            holder.dica.setBackgroundColor(Color.parseColor("#142440"));
            holder.dica.setTextColor(Color.parseColor("#142440"));
        }

    }

    @Override
    public int getItemCount() {
        return carta.getDicas().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView dica;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dica = itemView.findViewById(R.id.textDica);
            //Só dá para usar o find depois de fazer o inflate
        }
    }

    public static Integer getNumeroDicas() {
        return numeroDicas;
    }
}
