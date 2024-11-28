package com.example.api.repositories;

import com.example.api.models.Alimento;
import com.example.api.models.Cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {
    List<Alimento> findByCliente(Cliente cliente);

    Optional<Alimento> findByNomeAlimento(String nomeAlimento);
}