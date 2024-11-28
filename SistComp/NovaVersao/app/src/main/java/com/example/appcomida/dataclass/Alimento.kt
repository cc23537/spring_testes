package com.example.appcomida.dataclass



data class Alimento(
    val nomeAlimento: String,
    val calorias: Double? = null,
    val cliente: Int
)