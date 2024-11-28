package com.example.appcomida.rotas

import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.AlimentoEstocado
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AlimentoEstocadoRotas{
    @POST("alimentosestocados")
    fun resgistrarAlimentoEstocado(@Body alimentoEstocado: AlimentoEstocado): Response<AlimentoEstocado>

    @GET("alimentosestocados")
    fun getAllAlimentosEstocados(): Response<List<AlimentoEstocado>>
}