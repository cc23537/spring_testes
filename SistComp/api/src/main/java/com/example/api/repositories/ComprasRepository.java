package com.example.api.repositories;

import com.example.api.models.Cliente;
import com.example.api.models.Compras;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComprasRepository extends JpaRepository<Compras, Integer> {
    List<Compras> findByCliente(Cliente cliente);

}