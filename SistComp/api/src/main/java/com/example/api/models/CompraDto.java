package com.example.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompraDto {
    @JsonProperty("alimentoASerComprado")
    private AlimentoDto alimentoASerComprado;
    
    @JsonProperty("quantidade")
    private int quantidade;
    
    @JsonProperty("clienteId")
    private int clienteId;

    public CompraDto(AlimentoDto alimentoASerComprado, int quantidade, int clienteId) {
        this.alimentoASerComprado = alimentoASerComprado;
        this.quantidade = quantidade;
        this.clienteId = clienteId;
    }

    public AlimentoDto getAlimentoASerComprado() {
        return alimentoASerComprado;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getClienteId() {
        return clienteId;
    }
}
