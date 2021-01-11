package com.yagocurvello.perfilprototype.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yagocurvello.perfilprototype.R;
import com.yagocurvello.perfilprototype.config.ConfiguracaoFirebase;
import com.yagocurvello.perfilprototype.helper.Base64Custom;
import com.yagocurvello.perfilprototype.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private TextView usuarioName;
    private TextView usuarioPoints;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference referenceUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioName = findViewById(R.id.textUsuarioName);
        usuarioPoints = findViewById(R.id.textUsuarioPoints);
        Button botaoIniciar = findViewById(R.id.buttonIniciar);
        Button botaoCadastro = findViewById(R.id.buttonCadastro);
        Button botaoConfig = findViewById(R.id.buttonConfig);

        botaoIniciar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, JogoActivity.class));
            }
        });

        botaoCadastro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
            }
        });

        botaoConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConfigActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (verificarUsuarioLogado()){
            recuperaUsuario();
        } else {
            usuarioName.setText("Não Cadastrado");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (verificarUsuarioLogado()){
            referenceUsuario.removeEventListener(valueEventListenerUsuario);
        }
    }

    public Boolean verificarUsuarioLogado(){

        return autenticacao.getCurrentUser() != null;
    }


    public void UIusuario (Usuario usu){

        usuarioName.setText(usu.getName());
        usuarioPoints.setText(String.format("Points: %s",usu.getPoints()));
        usuarioPoints.setVisibility(View.VISIBLE);

    }

    public void recuperaUsuario(){

        String IdUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        referenceUsuario = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios").child(IdUsuario);

        // recupera dados do usuario
        valueEventListenerUsuario = referenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuarioRecuperado = snapshot.getValue(Usuario.class);
                    Log.i("UsuarioRec", usuarioRecuperado.toString());
                    UIusuario(usuarioRecuperado);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("UsuarioRec", error.getMessage());
            }
        });
    }

}