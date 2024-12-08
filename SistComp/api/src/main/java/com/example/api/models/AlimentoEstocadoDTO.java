package com.example.api.models;

import java.time.LocalDate;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AlimentoEstocadoDTO {
    private String nomeAlimento;
    private LocalDate validade;
    private int quantidadeEstoque;
    private Double calorias;
    private String especificacoes;
}
