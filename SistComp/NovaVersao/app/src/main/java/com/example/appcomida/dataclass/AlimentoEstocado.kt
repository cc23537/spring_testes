package com.example.appcomida.dataclass

import java.time.LocalDate

data class AlimentoEstocado(
    val alimentoId: Int? = null,
    val alimentoASerEstocado: Alimento? = null,
    val especificacoes: String? = null,
    val validade: LocalDate,
    val clienteId: Int = 0
)

