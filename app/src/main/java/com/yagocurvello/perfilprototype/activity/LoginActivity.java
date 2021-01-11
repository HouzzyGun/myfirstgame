package com.yagocurvello.perfilprototype.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.yagocurvello.perfilprototype.R;
import com.yagocurvello.perfilprototype.config.ConfiguracaoFirebase;
import com.yagocurvello.perfilprototype.model.Usuario;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private Usuario usuarioLogin;
    EditText emailLogin;
    EditText senhaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.editTextLoginEmail);
        senhaLogin = findViewById(R.id.editTextLoginSenha);
        Button botaoEntrar = findViewById(R.id.buttonLoginEntrar);
        TextView cadastrarText = findViewById(R.id.textCadastrar);



        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioLogin = new Usuario();
                usuarioLogin.setEmail(emailLogin.getText().toString());
                usuarioLogin.setSenha(senhaLogin.getText().toString());

                if (validaTexto(usuarioLogin)){
                    logarUsuario(usuarioLogin);
                }
            }
        });

        cadastrarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
                finish();
            }
        });
    }

    public boolean validaTexto(Usuario usuarioValidacao){
        if (!usuarioValidacao.getEmail().isEmpty()){
            if (!usuarioValidacao.getSenha().isEmpty()){
                return true;
            }else {
                Toast.makeText(getApplicationContext(), "Digite um Senha", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(getApplicationContext(), "Digite um Email", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void logarUsuario(Usuario usuarioDeslogado){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarioDeslogado.getEmail(), usuarioDeslogado.getSenha())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            String error = "";
                            try {
                               throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e ){
                                error = "E-mail e/ou Senha inválidos";
                            }catch (FirebaseAuthInvalidCredentialsException e ){
                                error = "Senha incorreta";
                            } catch (Exception e) {
                                error = "Erro: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                            Log.i("Erro login", error);
                        }
                    }
                });
    }
}