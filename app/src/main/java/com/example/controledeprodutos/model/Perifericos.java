package com.example.controledeprodutos.model;

import com.example.controledeprodutos.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Perifericos implements Serializable {
    private String id;
    private String nome;
    private int estoque;
    private double valor;
    private  double valor_custo;
    private String urlImagem;

    public Perifericos() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }

    public void salvarPerifericos() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("perifericos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.id);
        reference.setValue(this);
    }
public void deletaPerifericos(){
    DatabaseReference reference = FirebaseHelper.getDatabaseReference() // << deleta o produto no firebase
            .child("perifericos")
            .child(FirebaseHelper.getIdFirebase())
            .child(this.id);
    reference.removeValue();

    StorageReference storageReference = FirebaseHelper.getStorageReference() // << deleta o imagem no firebase
            .child("imagens")
            .child("perifericos")
            .child(FirebaseHelper.getIdFirebase())
            .child(this.id +".jpeg");
    storageReference.delete();
}
    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public double getValor_custo() {
        return valor_custo;
    }

    public void setValor_custo(double valor_custo) {
        this.valor_custo = valor_custo;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

}
