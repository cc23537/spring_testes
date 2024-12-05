package com.example.tcc.api

import com.example.tcc.dataclass.Cliente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Rotas {

    @GET("clientes/{email}/{senha}")
    suspend fun loginC(
        @Path("email") email: String,
        @Path("senha") password: String
    ): Response<String>

    @POST("clientes")
    suspend fun registerC(@Body cliente: Cliente): Response<Cliente>
}