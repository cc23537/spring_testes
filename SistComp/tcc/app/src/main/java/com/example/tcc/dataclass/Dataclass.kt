package com.example.tcc.dataclass

data class Cliente(
    val nomeCliente: String,
    val email: String,
    val senha: String,
    val peso: Double? = null,
    val altura: Double? = null
)
