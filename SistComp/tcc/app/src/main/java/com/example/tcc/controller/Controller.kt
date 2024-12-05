package com.example.tcc.controller

import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.dataclass.Cliente

suspend fun registrarCliente(nome: String, email: String, password: String, peso: Double?, altura : Double?) {
    val cliente = Cliente(nome, email, password, peso,altura)
    val apiService = getRetrofit().create(Rotas::class.java)
    val response = apiService.registerC(cliente)
    if (response.isSuccessful) {
        val registeredUser = response.body()
        println("Registered User: $registeredUser")
    } else {
        val errorMessage = "Failed: ${response.code()} - ${response.errorBody()?.string()}"
        println(errorMessage)
    }
}