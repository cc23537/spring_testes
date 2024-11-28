package com.example.appcomida.dataclass

import java.time.LocalDate

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

