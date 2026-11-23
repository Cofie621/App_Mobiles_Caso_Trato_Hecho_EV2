package com.example.myapplication.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto singleton que configura y crea la instancia de Retrofit.
 * Esta es la "puerta de entrada" para comunicarnos con la API.
 */
object RetrofitInstance {

    /**
     * URL base de tu API.
     * Aquí debes poner la URL que te da ngrok (ejemplo: https://abc123.ngrok-free.app/)
     * Importante: debe terminar con "/"
     */
    private const val BASE_URL = "https://TU-URL-NGROK.ngrok-free.app/"

    /**
     * Interceptor que muestra en el Logcat todas las peticiones y respuestas.
     * Muy útil para depurar (ver qué se envía y qué responde el servidor).
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente HTTP personalizado.
     * Aquí configuramos el interceptor de logs y los tiempos de espera.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)                    // Agrega los logs
        .connectTimeout(30, TimeUnit.SECONDS)       // Tiempo máximo para conectar
        .readTimeout(30, TimeUnit.SECONDS)          // Tiempo máximo para recibir respuesta
        .build()

    /**
     * Instancia de ApiService lista para usar.
     * Se crea solo la primera vez que se necesita (lazy).
     */
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)                              // URL base de la API
            .client(client)                                 // Cliente HTTP configurado
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON ↔ objetos Kotlin
            .build()
            .create(ApiService::class.java)                 // Crea la implementación de ApiService
    }
}