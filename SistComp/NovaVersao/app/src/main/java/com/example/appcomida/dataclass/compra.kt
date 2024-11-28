package com.example.appcomida.dataclass

data class Compra(
    val alimentoASerComprado: Alimento,
    val quantidade: Int,
    val clienteId: Int
)