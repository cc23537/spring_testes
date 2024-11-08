package com.example.api.models;


import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlimento;
    
    @Column(nullable = false)
    private String nomeAlimento;

    @Column(nullable = true)
    private Double calorias;
}
