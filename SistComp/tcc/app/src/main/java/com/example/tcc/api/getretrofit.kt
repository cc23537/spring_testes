package com.example.tcc.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.time.LocalDate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun getRetrofit(): Retrofit {
    // Configurar o GSON com adaptadores personalizados para LocalDate
    val gson = GsonBuilder()
        .registerTypeAdapter(
            LocalDate::class.java,
            com.google.gson.JsonSerializer<LocalDate> { src, _, _ ->
                com.google.gson.JsonPrimitive(src.toString()) // Serializa LocalDate para yyyy-MM-dd
            }
        )
        .registerTypeAdapter(
            LocalDate::class.java,
            com.google.gson.JsonDeserializer { json, _, _ ->
                LocalDate.parse(json.asString) // Desserializa yyyy-MM-dd para LocalDate
            }
        )
        .create()

    // Configuração do interceptor de logs
    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // Configuração do cliente HTTP inseguro (para teste)
    val client = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder()
        .addInterceptor(logging)
        .build()

    // Configuração do Retrofit com o GSON personalizado
    return Retrofit.Builder()
        .baseUrl("https://588d-187-106-37-122.ngrok-free.app")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson)) // Passa o GSON personalizado
        .addCallAdapterFactory(CoroutineCallAdapterFactory()) // Suporte para corrotinas
        .build()
}

object UnsafeOkHttpClient {
    fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Erro ao criar OkHttpClient inseguro", e)
        }
    }
}
