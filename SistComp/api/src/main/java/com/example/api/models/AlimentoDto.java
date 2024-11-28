package com.example.api.models;

public class AlimentoDto {
    private int idAlimento;
    private String nomeAlimento;
    private Double calorias;

    public AlimentoDto(int idAlimento, String nomeAlimento, Double calorias) {
        this.idAlimento = idAlimento;
        this.nomeAlimento = nomeAlimento;
        this.calorias = calorias;
    }

    // Getters e Setters
}