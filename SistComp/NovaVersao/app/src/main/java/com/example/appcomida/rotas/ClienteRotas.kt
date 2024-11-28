package com.example.appcomida.rotas

import com.example.appcomida.dataclass.Cliente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClienteRotas {

    @GET("clientes/{email}/{senha}")
    suspend fun login(
        @Path("email") email: String,
        @Path("senha") password: String
    ): Response<String>

    @POST("clientes")
    suspend fun register(@Body cliente: Cliente): Response<Cliente>
}