package com.yagocurvello.perfilprototype.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yagocurvello.perfilprototype.R;
import com.yagocurvello.perfilprototype.adapter.DicasAdapter;
import com.yagocurvello.perfilprototype.model.Carta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JogoActivity extends AppCompatActivity {

    private DatabaseReference referenceCarta = FirebaseDatabase.getInstance().getReference("cartas");
    private Carta carta = new Carta();
    private List<Carta> cartaList = new ArrayList<>();
    private TextView textTipo;
    private EditText editResposta;
    private RecyclerView recyclerDicas;
    public static boolean chuteRealizado = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        // Pega todas as referencias dos objetos da tela
        textTipo = findViewById(R.id.textTipo);
        recyclerDicas = findViewById(R.id.recyclerDicas);
        editResposta = findViewById(R.id.editTextResposta);

        Button botaoNovaCarta = findViewById(R.id.buttonCarta);
        Button botaoVoltar = findViewById(R.id.buttonVoltar);
        Button botaoResposta = findViewById(R.id.buttonResposta);

        // Cria a lista de cartas para jogar recuperando do banco de dados
        cartaList = listaCartasFireBase();

        // inicia a primeira carta como uma carta vazia
        CartaVazia();

        alertInicio(new AlertDialog.Builder(JogoActivity.this, R.style.AlertTheme));

        // Botão que gera uma nova carta
        botaoNovaCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartaAleatoria(cartaList);
            }
        });

        // Botao que envia a resposta
        botaoResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editResposta.getText().toString().isEmpty()){ //Verifica se a resposta está vazia
                    Toast.makeText(JogoActivity.this,
                            "Digite algo como Resposta", Toast.LENGTH_SHORT).show();
                }else{
                    //Verifica se um chute já foi realizado nessa rodada
                    if (!chuteRealizado){
                        String resposta = editResposta.getText().toString();
                        chuteRealizado = true;
                        if (resposta.toLowerCase().equals(carta.getResposta().toLowerCase())){
                            alertWin(new AlertDialog.Builder(JogoActivity.this, R.style.AlertTheme));
                        } else {alertLose ();}
                    } else {
                        Toast.makeText(JogoActivity.this,
                                "Só é permitido 1 chute por dica", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Botão que volta a tela inicial
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // Metodo que sorteia uma carta da lista
    public void cartaAleatoria(List<Carta> listaDeCarta){

        int pos = geraAleatorio(listaDeCarta.size()-1,0);
        carta = listaDeCarta.get(pos);

        DicasAdapter dicasAdapter = new DicasAdapter(carta);

        // Novas visualizações
        textTipo.setText(carta.getTipo());

        // Novo LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDicas.setLayoutManager(layoutManager);
        recyclerDicas.setAdapter(dicasAdapter);

    }

    // Metodo auxiliar para gerar um numero aleatorio dentro de um intervalo
    public static int geraAleatorio(int max, int min) {
        Random random = new Random();
        return (random.nextInt(max - (min - 1)) + min);
    }

    // Metodo que recupera a lista de cartas do banco de dados
    public List<Carta> listaCartasFireBase(){
        updateCartasFireBase();
        return cartaList;
    }

    // Metodo que atualiza a lista de cartas quando há uma alteração no banco de dados
    public void updateCartasFireBase(){
        referenceCarta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartaList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    cartaList.add(item.getValue(Carta.class));
                }
                Log.i("Update lista",cartaList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Erro Retorno", error.getMessage());
            }
        });
    }

    // Metodo que configura uma carta vazia
    public void CartaVazia (){
        Carta vazia = new Carta();
        vazia.setTipo("Objeto ");
        vazia.setResposta(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");
        vazia.addDica(" - ");

        // Configura toda visualização da carta
        textTipo.setText(vazia.getTipo());

        // Configurar Adapter
        DicasAdapter dicasAdapter = new DicasAdapter(vazia);

        // Configurar LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDicas.setLayoutManager(layoutManager);
        recyclerDicas.setAdapter(dicasAdapter);
        recyclerDicas.setHasFixedSize(true);
        recyclerDicas.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    //Metodo para criar uma caixa de alerta
    public void alertWin(AlertDialog.Builder alert){
        alert.setTitle("Parabéns!!");
        int points = 20;
        alert.setMessage("Você acertou! \nA resposta era: " + carta.getResposta() + "\nVocê fez " + (points - DicasAdapter.getNumeroDicas()) + " Pontos!" );
        alert.setPositiveButton("Proxima Carta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Novo Adapter
                cartaAleatoria(cartaList);
                editResposta.setText("");
            }
        });
        alert.setNegativeButton("Voltar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,int i) {
                finish();
            }
        });
        alert.create();
        alert.show();
    }

    public void alertLose (){
        Toast.makeText(JogoActivity.this,"Resposta Incorreta", Toast.LENGTH_SHORT).show();
    }

    public void alertInicio(AlertDialog.Builder alert){
        alert.setTitle("Novo Jogo");
        alert.setMessage("Clique OK para iniciar uma nova partida.");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cartaAleatoria(cartaList);
            }
        });
        alert.create();
        alert.show();
    }
}