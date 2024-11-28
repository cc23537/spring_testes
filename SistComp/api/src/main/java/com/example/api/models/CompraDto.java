package com.example.api.models;

public class CompraDto {
    private int idCompra;
    private AlimentoDto alimentoASerComprado;
    private int quantidade;

    public CompraDto(int idCompra, AlimentoDto alimentoASerComprado, int quantidade) {
        this.idCompra = idCompra;
        this.alimentoASerComprado = alimentoASerComprado;
        this.quantidade = quantidade;
    }
}
