package com.example.tcc.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.dataclass.Alimento
import com.example.tcc.dataclass.AlimentoEstocado
import com.example.tcc.dataclass.Cliente
import com.example.tcc.dataclass.Compra


val apiService = getRetrofit().create(Rotas::class.java)
suspend fun registrarCliente(nome: String, email: String, password: String, peso: Double?, altura : Double?) {
    val cliente = Cliente(nome, email, password, peso,altura)

    val response = apiService.registerC(cliente)
    if (response.isSuccessful) {
        val registeredUser = response.body()
        println("Registered User: $registeredUser")
    } else {
        val errorMessage = "Failed: ${response.code()} - ${response.errorBody()?.string()}"
        println(errorMessage)
    }
}

suspend fun getClienteNome(clienteId: Int): String? {
    val service = getRetrofit().create(Rotas::class.java)
    val response = service.achaCliente(clienteId)
    return if (response.isSuccessful) {
        response.body()?.nomeCliente
    } else {
        null
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun registrarCompra(alimento: Alimento, qntde: Int, clienteid: Int) {
    val compra = Compra(alimento,  qntde, clienteid)
    val apiService = getRetrofit().create(Rotas::class.java)
    val response =   apiService.registroCompras(compra)
    if (response.isSuccessful) {
        val registerCompra = response.body()
        println("Registered Compra: $registerCompra")
    } else {
        val errorMessage = "Failed: ${response.code()} - ${response.errorBody()?.string()}"
        println(errorMessage)
    }
}

fun registrarAlimentoEstocado(alimentoEstocado: AlimentoEstocado) {
    val apiService = getRetrofit().create(Rotas::class.java)
    val response = apiService.resgistrarAlimentoEstocado(alimentoEstocado)
    if (response.isSuccessful) {
        val alimentoEstocadoCriado = response.body()
        println("Alimento estocado com sucesso: $alimentoEstocadoCriado")
    } else {
        println("Erro ao registrar alimento estocado: ${response.errorBody()?.string()}")
    }
}