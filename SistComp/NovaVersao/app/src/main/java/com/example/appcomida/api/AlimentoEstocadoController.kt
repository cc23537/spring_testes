package com.example.appcomida.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.appcomida.ApiService
import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.AlimentoEstocado
import com.example.appcomida.rotas.AlimentoEstocadoRotas
import com.example.appcomida.rotas.ClienteRotas
import getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun registrarAlimentoEstocado(alimentoEstocado: AlimentoEstocado) {
    val apiService = getRetrofit().create(AlimentoEstocadoRotas::class.java)
    val response = apiService.resgistrarAlimentoEstocado(alimentoEstocado)
    if (response.isSuccessful) {
        val alimentoEstocadoCriado = response.body()
        println("Alimento estocado com sucesso: $alimentoEstocadoCriado")
    } else {
        println("Erro ao registrar alimento estocado: ${response.errorBody()?.string()}")
    }
}