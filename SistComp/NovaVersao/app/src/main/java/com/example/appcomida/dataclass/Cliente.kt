package com.example.appcomida.dataclass

data class Cliente(
    val nomeCliente: String,
    val email: String,
    val senha: String,
    val peso: Double? = null,
    val altura: Double? = null
)
