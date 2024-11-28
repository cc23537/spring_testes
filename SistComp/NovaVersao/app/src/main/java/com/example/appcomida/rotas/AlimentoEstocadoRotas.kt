package com.example.appcomida.rotas

import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.AlimentoEstocado
import com.example.appcomida.dataclass.AlimentoEstocadoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlimentoEstocadoRotas{
    @POST("alimentosestocados")
    fun resgistrarAlimentoEstocado(@Body alimentoEstocado: AlimentoEstocado): Response<AlimentoEstocado>

    @GET("/clientes/{id}/alimentosestocados")
    suspend  fun getAllAlimentosEstocados(@Path("id") id:Int): Response<List<AlimentoEstocadoResponse>>
}