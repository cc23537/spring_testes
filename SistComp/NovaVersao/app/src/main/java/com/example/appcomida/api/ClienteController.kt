package com.example.appcomida.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.appcomida.ApiService
import com.example.appcomida.dataclass.Cliente
import com.example.appcomida.rotas.ClienteRotas
import getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.O)
suspend fun registrarCliente(nome: String, email: String, password: String, peso: Double?, altura : Double?) {
    val cliente = Cliente(nome, email, password, peso,altura)
    val apiService = getRetrofit().create(ClienteRotas::class.java)
    val response = apiService.register(cliente)
    if (response.isSuccessful) {
        val registeredUser = response.body()
        println("Registered User: $registeredUser")
    } else {
        val errorMessage = "Failed: ${response.code()} - ${response.errorBody()?.string()}"
        println(errorMessage)
    }
}