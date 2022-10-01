package com.example.controledeprodutos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.adapter.AdapterProduto;
import com.example.controledeprodutos.autenticacao.LoginActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPerifericosActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private AdapterProduto adapterProduto;
    private List<Produto> produtoList = new ArrayList<>();

    private SwipeableRecyclerView rvPerifericos;

    private ImageButton ibAdd;
    private ImageButton ib_voltar_cat;
    private ImageButton ibVerMais;
    private TextView text_info;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_perifericos);

        iniciaComponentes();


        ouvinteCliques();
        configRecyclerView();
    }

    private void ouvinteCliques() {
        ibAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, FormProdutoPerifeActivity.class));
        });
        ib_voltar_cat.setOnClickListener(view -> {
            startActivity(new Intent(this,InforProdutoActivity.class));
        });
        ibVerMais.setOnClickListener(view -> {

            PopupMenu popupMenu = new PopupMenu(this, ibVerMais);
            popupMenu.getMenuInflater().inflate(R.menu.menu_toolbar, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_sobre) {
                    Toast.makeText(this, "Cairu", Toast.LENGTH_SHORT).show();
                } else if (menuItem.getItemId() == R.id.menu_sair) {
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            });
            popupMenu.show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaProdutos();
    }

    private void recuperaProdutos() {
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("perifericos")
                .child(FirebaseHelper.getIdFirebase());
        produtosRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtoList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Produto produto = snapshot1.getValue(Produto.class);
                    produtoList.add(produto);
                }
                verificaQtdLista();
                Collections.reverse(produtoList);
                adapterProduto.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRecyclerView() {

        rvPerifericos.setLayoutManager(new LinearLayoutManager(this));
        rvPerifericos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtoList, this);
        rvPerifericos.setAdapter(adapterProduto);

        rvPerifericos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                Produto produto = produtoList.get(position);

                produtoList.remove(produto);
                produto.deletaProduto();
                adapterProduto.notifyItemRemoved(position);

                verificaQtdLista();

            }
        });
    }

    private void verificaQtdLista() {
        if (produtoList.size() == 0) {
            text_info.setText("Nenhum produto cadastrado.");
            text_info.setVisibility(View.VISIBLE);
        } else {
            text_info.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormProdutoPerifeActivity.class);
        intent.putExtra("perifericos", produto);
        startActivity(intent);
    }

    private void iniciaComponentes() {
        ibAdd = findViewById(R.id.ib_add);
        ibVerMais = findViewById(R.id.ib_ver_mais);
        rvPerifericos = findViewById(R.id.rvPerifericos);
        text_info = findViewById(R.id.text_info);
        progressBar = findViewById(R.id.progressBar);
        ib_voltar_cat = findViewById(R.id.ib_voltar_cat);
    }
}


