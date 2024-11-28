package com.example.appcomida.rotas

import com.example.appcomida.dataclass.ApiResponse
import com.example.appcomida.dataclass.Compra
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ComprasRotas{

    @POST("compras")
    public fun registroCompras(@Body compra: Compra): Response<Compra>

    @GET("compras/{idCliente}")
    fun listagemCompras(@Path("idCliente") idCliente: Int): Call<List<ApiResponse>>
}