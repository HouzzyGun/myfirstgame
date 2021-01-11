package com.yagocurvello.perfilprototype.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yagocurvello.perfilprototype.R;
import com.yagocurvello.perfilprototype.config.ConfiguracaoFirebase;

public class ConfigActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Button botaoLogout, botaoVoltar, botaoRemoveAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        botaoLogout = findViewById(R.id.buttonConfig);
        botaoVoltar = findViewById(R.id.buttonVoltar);


        botaoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autenticacao.getCurrentUser() != null){
                    Toast.makeText(ConfigActivity.this,autenticacao.getCurrentUser().getEmail() + " Logout", Toast.LENGTH_SHORT).show();
                    autenticacao.signOut();
                }else {
                    Toast.makeText(ConfigActivity.this,"Nenhum Usuario Logado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}