package com.example.appcomida.dataclass

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