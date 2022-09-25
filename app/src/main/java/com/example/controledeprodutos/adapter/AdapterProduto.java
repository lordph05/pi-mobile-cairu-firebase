package com.example.controledeprodutos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduto  extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> {

    private List <Produto> produtoList;
    private  OnClick onClick;

    public AdapterProduto(List<Produto> produtoList, OnClick onClick) {
        this.produtoList = produtoList;
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
Produto produto = produtoList.get(position);
        Picasso.get().load(produto.getUrlImagem()).into(holder.img_descricao);
holder.textProduto.setText(produto.getNome());
holder.textEstoque.setText("EStoque: "+String.valueOf(produto.getEstoque()));
        holder.textValor.setText("R$: "+String.valueOf(produto.getValor()));
        holder.text_valor_custo.setText("Valor custo: "+String.valueOf(produto.getValor_custo()));


        holder.itemView.setOnClickListener(view -> onClick.onClickListener(produto));


    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClick{
        void onClickListener (Produto produto);
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
