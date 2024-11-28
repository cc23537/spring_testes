package com.example.appcomida.dataclass

import java.time.LocalDate

data class a1(
    val idAlimento: Int,
    val nomeAlimento: String,
    val calorias: Double?,
    val cliente: Cliente
)

data class ae1(
    val idEstoque: Int,
    val alimentoASerEstocado: Alimento,
    val especificacoes: String?,
    val validade: LocalDate,
    val cliente: Cliente,
    val quantidadeEstoque: Int
)

data class c1(
    val idCliente: Int,
    val nomeCliente: String,
    val email: String,
    val senha: String,
    val peso: Double?,
    val altura: Double?
)