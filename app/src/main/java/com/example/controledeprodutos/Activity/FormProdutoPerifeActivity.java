package com.example.controledeprodutos.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Perifericos;
import com.example.controledeprodutos.model.Produto;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FormProdutoPerifeActivity<GALERA> extends AppCompatActivity {

    private static final int REQUEST_GALERA = 100;
    private String caminhoImagem;
    private Bitmap imagem;

    ImageView imagem_produto;
    EditText edit_produto;
    EditText edit_quantidade;
    EditText edit_valor;
    EditText edit_valor_custo;

    private Perifericos perifericos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto_perife);

        iniciaComponentes();
        configClique ();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            perifericos = (Perifericos) bundle.getSerializable("perifericos");
            editProduto();


    }
}

    public void abrirGaleria(View view) {
        verificaPermissaoGaleria();
    }

    private  void configClique () {
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish() );
    }


    private void verificaPermissaoGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrindoGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormProdutoPerifeActivity.this, "Permissão Negada.", Toast.LENGTH_SHORT).show();

            }
        };

        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Aceita Permissao ?");
    }

    private void abrindoGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERA);
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg) {

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();

    }

    private void editProduto() {
        Picasso.get().load(perifericos.getUrlImagem()).into(imagem_produto);
        edit_produto.setText(perifericos.getNome());
        edit_quantidade.setText(String.valueOf(perifericos.getEstoque()));
        edit_valor.setText(String.valueOf(perifericos.getValor()));
        edit_valor_custo.setText(String.valueOf(perifericos.getValor_custo()));

    }

    public void salvarPerifericos(View view) {

        String nome = edit_produto.getText().toString();
        String quantidade = edit_quantidade.getText().toString();
        String valor = edit_valor.getText().toString();
        String valor_custo = edit_valor_custo.getText().toString();

        if (!nome.isEmpty()) {
            if (!quantidade.isEmpty()) {
                int qtd = Integer.parseInt(quantidade);
                if (qtd >= 1) {

                    if (!valor.isEmpty()) {
                        double valorproduto = Double.parseDouble(valor);

                        if (valorproduto > 0) {

                            if (perifericos == null) perifericos = new Perifericos();
                            perifericos.setNome(nome);
                            perifericos.setEstoque(qtd);
                            perifericos.setValor(valorproduto);
                            perifericos.setValor_custo(valorproduto); // linha precisa ser alterada cria uma variavel para valor de custo.

                            if (caminhoImagem != null){
                                salvarImagemProduto();
                            }else{
                                if (perifericos.getUrlImagem()!= null){
                                    perifericos.salvarPerifericos();
                                }else if (caminhoImagem == null){
                                    Toast.makeText(this, "Selecione uma imagem.", Toast.LENGTH_SHORT).show();
                                    perifericos.salvarPerifericos();
                                }
                            }


//                            if (caminhoImagem == null) {
//                                Toast.makeText(this, "Selecione uma imagem.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                salvarImagemProduto();
//                            }if (produto.getUrlImagem()!= null){
//                                produto.salvarProduto();
//                            }


                            finish();

                        } else {
                            edit_valor.requestFocus();
                            edit_valor.setError("Digite u valor válido");
                        }

                    } else {
                        edit_valor.requestFocus();
                        edit_valor.setError("Digite u valor válido");
                    }
                } else {
                    edit_quantidade.requestFocus();
                    edit_quantidade.setError("informe a quantidade");
                }
            } else {
                edit_quantidade.requestFocus();
                edit_quantidade.setError("informe a quantidade");

            }
        } else {
            edit_produto.requestFocus();
            edit_produto.setError("Informe o nome do produto");
        }
    }

    private void salvarImagemProduto () {
        StorageReference reference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("perifericos")
                .child(FirebaseHelper.getIdFirebase())
                .child(perifericos.getId() +".jpeg");

        UploadTask uploadTask = reference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnCompleteListener(task -> {

            perifericos.setUrlImagem(task.getResult().toString());
            perifericos.salvarPerifericos();

            finish();


        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void iniciaComponentes() {
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Cadastro de periféricos");

        edit_produto = findViewById(R.id.edit_produto);
        edit_quantidade = findViewById(R.id.edit_quantidade);
        edit_valor = findViewById(R.id.edit_valor);
        edit_valor_custo = findViewById(R.id.edit_valor_custo);
        imagem_produto = findViewById(R.id.imagem_produto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_GALERA){

                Uri localImagemSelecionada = data.getData();
                caminhoImagem = localImagemSelecionada.toString();

                if (Build.VERSION.SDK_INT <28){
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(),localImagemSelecionada);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(),localImagemSelecionada);
                    try {
                        imagem = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        imagem_produto.setImageBitmap(imagem);
    }
}
