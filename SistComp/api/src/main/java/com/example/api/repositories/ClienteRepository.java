package com.example.api.repositories;

import com.example.api.models.Alimento;
import com.example.api.models.Cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmailAndSenha(String email, String senha);
}