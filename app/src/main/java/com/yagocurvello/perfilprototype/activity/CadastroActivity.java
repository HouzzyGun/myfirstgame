package com.yagocurvello.perfilprototype.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.yagocurvello.perfilprototype.R;
import com.yagocurvello.perfilprototype.config.ConfiguracaoFirebase;
import com.yagocurvello.perfilprototype.helper.Base64Custom;
import com.yagocurvello.perfilprototype.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText usuarioEditText;
    private EditText emailEditText;
    private EditText senhaEditText;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Button botaoCadastrar = findViewById(R.id.buttonCadastrar);
        usuarioEditText = findViewById(R.id.editTextUsuario);
        emailEditText = findViewById(R.id.editTextEmail);
        senhaEditText = findViewById(R.id.editTextSenha);
        TextView login = findViewById(R.id.textLogin);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setName(usuarioEditText.getText().toString());
                usuario.setEmail(emailEditText.getText().toString());
                usuario.setSenha(senhaEditText.getText().toString());

                if (validaTexto(usuario)){
                    cadastroUsuario(usuario);
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroActivity.this, LoginActivity.class ));
                finish();
            }
        });

    }

    public void cadastroUsuario(final Usuario usuarioCadastrado){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarioCadastrado.getEmail(), usuarioCadastrado.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String IdUsuario = Base64Custom.codificarBase64(usuarioCadastrado.getEmail());
                    usuarioCadastrado.setIdUsuario(IdUsuario);
                    usuarioCadastrado.salvar();
                    finish();

                }else {
                    String error = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Senha muito fraca";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        error = "email inválido";
                    } catch (FirebaseAuthUserCollisionException e){
                        error = "email já foi cadastrado";
                    }catch (Exception e){
                        error = "Erro: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean validaTexto(Usuario usuarioValidacao){
        if (!usuarioValidacao.getName().isEmpty()){
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
        }else {
            Toast.makeText(getApplicationContext(), "Digite uma Usuario", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}