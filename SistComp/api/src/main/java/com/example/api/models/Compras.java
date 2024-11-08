package com.example.api.models;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Tcc_Compras")
public class Compras {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int idCompra;
        
        @Column(nullable = false)
        private Alimento alimentoASerComprado;

        @Column(nullable = false)
        private int quantidade;
}
    