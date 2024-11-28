package com.example.api.repositories;

import com.example.api.models.AlimentoEstocado;
import com.example.api.models.Cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentoEstocadoRepository extends JpaRepository<AlimentoEstocado, Integer> {
    List<AlimentoEstocado> findByCliente(Cliente cliente);
}
