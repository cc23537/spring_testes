package com.example.appcomida

import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.Compra
import com.example.appcomida.dataclass.Cliente
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("cliente/{email}/{senha}")
    suspend fun login(@Path("email") email: String, @Path("senha") password: String): Response<String>

    @POST("cliente")
    suspend fun register(@Body Cliente: Cliente): Response<Cliente>


    @GET("compras")
    fun listagemCompras(): Call<List<Compra>>



    @GET("/alimentos")
    fun getAllAlimentos(): Call<List<Alimento>>

    @GET("alimentos/{date}")
    suspend fun data(@Path("date") date: CalendarDay): Response<List<Alimento>>


    @DELETE("alimentos/{nome}/{date}")
    suspend fun removeAlimento(
        @Path("nome") nome: String,
        @Path("date") date: String
    ): Response<Void>

    @DELETE("compras/{nome}/{quantidade}")
    public fun removeCompra(
        @Path("nome") nome: String,
        @Path("quantidade") quantidade: Int
    ): Response<Void>

    @GET("/acharnome/{email}/{senha}")
    fun achanome(
        @Path("email") email : String,
        @Path("senha") senha: String
    ) : Response<String>
}