package com.yagocurvello.perfilprototype.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Carta {

    private String tipo;
    private List<String> listaDicas = new ArrayList<>();
    private String resposta;

    public Carta() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getDicas() {
        return listaDicas;
    }

    public void setDicas(List<String> dicas) {
        this.listaDicas = dicas;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public void addDica(String dica){
        listaDicas.add(dica);
    }

    @Override
    public String toString() {
        return "Carta " + resposta;
    }
}
