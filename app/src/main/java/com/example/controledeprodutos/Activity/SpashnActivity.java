package com.example.controledeprodutos.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.autenticacao.LoginActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;

public class SpashnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spashn);

        new Handler(Looper.getMainLooper()).postDelayed(this ::verificaLogin, 3000);

    }

    private void verificaLogin() {
        if (FirebaseHelper.getAutenticado()) {
            startActivity(new Intent(this, MainActivity.class));

        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}