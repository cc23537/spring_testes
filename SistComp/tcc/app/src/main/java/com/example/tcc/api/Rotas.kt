package com.example.tcc.api

import com.example.tcc.dataclass.AlimentoEstocado
import com.example.tcc.dataclass.AlimentoEstocadoDto
import com.example.tcc.dataclass.AlimentoEstocadoResponse
import com.example.tcc.dataclass.ApiResponse
import com.example.tcc.dataclass.Cliente
import com.example.tcc.dataclass.Compra
import com.example.tcc.dataclass.CompraASerComprada
import com.example.tcc.dataclass.Recipe
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Rotas {

    @GET("clientes/{email}/{senha}")
    suspend fun loginC(
        @Path("email") email: String,
        @Path("senha") password: String
    ): Response<String>

    @POST("clientes")
    suspend fun registerC(@Body cliente: Cliente): Response<Cliente>

    @GET("/clientes/{id}")
    suspend fun achaCliente(@Path("id") id: Int): Response<Cliente>

    @POST("alimentosestocados")
    suspend fun resgistrarAlimentoEstocado(@Body alimentoEstocado: AlimentoEstocado): Response<AlimentoEstocadoResponse>

    @GET("/clientes/{id}/alimentosestocados")
    suspend  fun getAllAlimentosEstocados(@Path("id") id:Int): Response<List<AlimentoEstocadoDto>>


    @POST("compras")
    fun registroCompras(@Body compra: CompraASerComprada): Call<Compra>


    @GET("compras/{idCliente}")
    fun listagemCompras(@Path("idCliente") idCliente: Int): Call<List<ApiResponse>>

    @DELETE("/compras/{id}")
    fun DeletaCompra(@Path("id") idCompra: Int): Call<Void>



    @GET("/alimentosestocados/cliente/{id}")
    fun getAlimentoEstocadoC(@Path("id") id: Int): retrofit2.Call<List<String>>

    @GET("/alimentosestocados/{id}")
    fun getQuantidade(@Path("id") id: Int): Call<Int>

    @GET("/alimentosestocados/proxdatavalidade/{id}")
    suspend fun getProxAlimentoValido(@Path("id") id:Int): Response<AlimentoEstocadoDto>

    @DELETE("/delete/{nomealimento}/{validade}/{id}")
    suspend fun removeAlimento(
        @Path("id") id:Int,
        @Path("nomealimento") nomealimento: String,
        @Path("validade") validade: String
    ): Response<Void>
}