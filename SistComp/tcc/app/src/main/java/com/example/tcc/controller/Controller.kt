package com.example.tcc.controller

import android.os.Build
import android.view.WindowInsetsAnimation
import androidx.annotation.RequiresApi
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.dataclass.Alimento
import com.example.tcc.dataclass.AlimentoASerComprado
import com.example.tcc.dataclass.AlimentoASerComprado20
import com.example.tcc.dataclass.AlimentoEstocado
import com.example.tcc.dataclass.AlimentoEstocadoDto
import com.example.tcc.dataclass.AlimentoGDTO
import com.example.tcc.dataclass.Cliente
import com.example.tcc.dataclass.ClientePft
import com.example.tcc.dataclass.Compra
import com.example.tcc.dataclass.CompraASerComprada
import retrofit2.Call
import retrofit2.Response


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


fun registrarCompra(alimentoASerComprado: AlimentoASerComprado20, quantidade: Int, clienteId: ClientePft) {
    val compra = CompraASerComprada(alimentoASerComprado, quantidade, clienteId)
    val service = getRetrofit().create(Rotas::class.java)
    service.registroCompras(compra).enqueue(object : retrofit2.Callback<Compra> {
        override fun onResponse(call: Call<Compra>, response: Response<Compra>) {
            if (response.isSuccessful) {
                println("Compra registrada com sucesso")
            } else {
                println("Erro ao registrar compra: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<Compra>, t: Throwable) {
            println("Erro ao registrar compra: ${t.message}")
        }
    })
}






suspend fun registrarAlimentoEstocado(alimentoEstocado: AlimentoEstocado) {
    val apiService = getRetrofit().create(Rotas::class.java)
    try {
        val response = apiService.resgistrarAlimentoEstocado(alimentoEstocado)
        if (response.isSuccessful) {
            val alimentoEstocadoCriado = response.body()
            println("Alimento estocado com sucesso: $alimentoEstocadoCriado")
        } else {
            println("Erro ao registrar alimento estocado: ${response.errorBody()?.string()}")
        }
    } catch (e: Exception) {
        println("Erro ao realizar a requisição: $e")
    }
}





suspend fun getQuantidade(clienteId: Int): Int {
    val apiService = getRetrofit().create(Rotas::class.java)
    var quantidade: Int = 0 // Inicialize com um valor padrão
    return try {
        val response: Response<Int> = apiService.getQuantidade(clienteId).execute()
        if (response.isSuccessful && response.body() != null) {
            response.body() ?: 0 // Use o valor retornado pela API
        } else {
            println("Erro: ${response.errorBody()?.string()}")
            0
        }
    } catch (e: Exception) {
        println("Exceção: $e")
        0
    }
}
suspend fun getprox(clienteId: Int): AlimentoEstocadoDto? {
    val apiService = getRetrofit().create(Rotas::class.java)
    return try {
        val response = apiService.getProxAlimentoValido(clienteId)
        if (response.isSuccessful) {
            response.body()
        } else {
            println("Erro ao registrar alimento estocado: ${response.errorBody()?.string()}")
            null
        }
    } catch (e: Exception) {
        println("Erro ao realizar a requisição: $e")
        null
    }
}













