package com.example.tcc.dataclass

import java.time.LocalDate

data class Cliente(
    val nomeCliente: String,
    val email: String,
    val senha: String,
    val peso: Double? = null,
    val altura: Double? = null
)

data class ApiResponse(
    val idCompra: Int,
    val alimentoASerComprado: AlimentoApi,
    val quantidade: Int,
    val cliente: ClienteApi
)

data class AlimentoApi(
    val idAlimento: Int,
    val nomeAlimento: String,
    val calorias: Double,
    val cliente: ClienteApi
)

data class ClienteApi(
    val idCliente: Int,
    val nomeCliente: String,
    val email: String,
    val senha: String,
    val peso: Double?,
    val altura: Double?
)

data class AlimentoEstocadoResponse(
    val idEstoque: Int,
    val alimentoASerEstocado: AlimentoApi, // Usando a classe AlimentoApi
    val especificacoes: String,
    val validade: String, // O campo validade é uma String, pois a data está no formato "YYYY-MM-DD"
    val cliente: ClienteApi, // Usando a classe ClienteApi
    val quantidade: Int
)

data class AlimentoEstocado(
    val alimentoId: Int? = null,
    val alimentoASerEstocado: Alimento? = null,
    val especificacoes: String? = null,
    val validade: LocalDate,
    val clienteId: Int = 0
)

data class Alimento(
    val nomeAlimento: String,
    val calorias: Double? = null,
    val cliente: Int
)

data class Compra(
    val alimentoASerComprado: Alimento,
    val quantidade: Int,
    val clienteId: Int

)

data class AlimentoEstocadoDto(
    val nomeAlimento: String,
    val validade: LocalDate,
    val quantidadeEstoque: Int,
    val calorias: Double,
    val especificacoes: String
)