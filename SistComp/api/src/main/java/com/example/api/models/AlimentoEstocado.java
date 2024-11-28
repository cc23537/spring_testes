package com.example.api.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Tcc_Alimentos")
public class AlimentoEstocado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEstoque;
    
    @ManyToOne
    @JoinColumn(name = "idAlimento", nullable = false)
    private Alimento alimentoASerEstocado;

    @Column(nullable = true)
    private String especificacoes;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validade;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private int quantidadeEstoque;

}
