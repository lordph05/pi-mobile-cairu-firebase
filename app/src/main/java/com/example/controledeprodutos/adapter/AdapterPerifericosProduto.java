package com.example.controledeprodutos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.model.Perifericos;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPerifericosProduto extends RecyclerView.Adapter<AdapterPerifericosProduto.MyViewHolder> {

    private List <Perifericos> perifericosList;
    private  OnClick onClick;

    public AdapterPerifericosProduto(List<Perifericos> perifericosList, OnClick onClick) {
        this.perifericosList = perifericosList;
        this.onClick = onClick;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View intemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent,false);
        return new MyViewHolder(intemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
Perifericos perifericos = perifericosList.get(position);
        Picasso.get().load(perifericos.getUrlImagem()).into(holder.img_descricao);
holder.textProduto.setText(perifericos.getNome());
holder.textEstoque.setText("EStoque: "+String.valueOf(perifericos.getEstoque()));
        holder.textValor.setText("R$: "+String.valueOf(perifericos.getValor()));
        holder.text_valor_custo.setText("Valor custo: "+String.valueOf(perifericos.getValor_custo()));


        holder.itemView.setOnClickListener(view -> onClick.onClickListener(perifericos));

    }

    @Override
    public int getItemCount() {
        return perifericosList.size();
    }

    public interface OnClick{
        void onClickListener (Perifericos perifericos);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_descricao;
        TextView textProduto, textEstoque, textValor, text_valor_custo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textProduto = itemView.findViewById(R.id.text_produto);
            textEstoque = itemView.findViewById(R.id.text_estoque);
            textValor = itemView.findViewById(R.id.text_valor);
            text_valor_custo = itemView.findViewById(R.id.text_valor_custo);
            img_descricao = itemView.findViewById(R.id.img_descricao);


        }
    }
}
