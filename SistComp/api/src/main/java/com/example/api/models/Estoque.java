package com.example.api.models;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Tcc_Alimentos")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEstoque;
    
    @Column(nullable = false)
    private Alimento alimentoASerEstocado;

    @Column(nullable = true)
    private String especificacoes;

    @Column(nullable = true)
    private String validade;
}
