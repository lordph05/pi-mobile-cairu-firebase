package com.example.controledeprodutos.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.helper.FirebaseHelper;

public class RecuperarContaActivity extends AppCompatActivity {

    private EditText edit_email;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_conta);


        iniciaComponentes();
        configClique();


    }

    public void recuperarSenha(View view) {
        String email = edit_email.getText().toString().trim();

        if (!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            enviarEmail(email);

        } else {
            edit_email.requestFocus();
            edit_email.setError("Informe seu email.");
        }
    }

    private void enviarEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Email enviado com sucesso", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void iniciaComponentes() {
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Recuperar senha");
        edit_email = findViewById(R.id.edit_email);
        progressBar = findViewById(R.id.progressBar);

    }

    private void configClique() { // eventos de cliques fora da origem.
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());


    }
}