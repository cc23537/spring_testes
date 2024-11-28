package com.example.appcomida.api

import com.example.appcomida.ApiService
import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.Compra
import com.example.appcomida.rotas.ComprasRotas
import getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

fun registrarCompra(alimento: Alimento, qntde: Int, clienteid: Int) {
    val compra = Compra(alimento,  qntde, clienteid)
    val apiService = getRetrofit().create(ComprasRotas::class.java)
    val response =   apiService.registroCompras(compra)
    if (response.isSuccessful) {
        val registerCompra = response.body()
        println("Registered Compra: $registerCompra")
    } else {
        val errorMessage = "Failed: ${response.code()} - ${response.errorBody()?.string()}"
        println(errorMessage)
    }
}