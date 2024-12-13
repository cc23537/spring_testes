package com.example.api.repositories;

import com.example.api.models.AlimentoEstocado;
import com.example.api.models.Cliente;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentoEstocadoRepository extends JpaRepository<AlimentoEstocado, Integer> {
    List<AlimentoEstocado> findByCliente(Cliente cliente);
    Optional<AlimentoEstocado> findFirstByClienteOrderByValidadeAsc(Cliente cliente);
Optional<AlimentoEstocado> findByAlimentoASerEstocado_NomeAlimentoAndValidadeAndCliente_IdCliente(String nomeAlimento, LocalDate validade, Integer idCliente);
}
