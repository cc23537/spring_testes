package com.example.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlimentoDto {
    @JsonProperty("nomeAlimento")
    private String nomeAlimento;
    
    @JsonProperty("calorias")
    private Double calorias;
    
    @JsonProperty("cliente")
    private int cliente;

    public AlimentoDto(String nomeAlimento, Double calorias, int cliente) {
        this.nomeAlimento = nomeAlimento;
        this.calorias = calorias;
        this.cliente = cliente;
    }

    public String getNomeAlimento() {
        return nomeAlimento;
    }

    public Double getCalorias() {
        return calorias;
    }

    public int getCliente() {
        return cliente;
    }
}
