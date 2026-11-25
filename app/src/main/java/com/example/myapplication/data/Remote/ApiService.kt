package com.example.myapplication.data.remote

import com.example.myapplication.model.CotizacionItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Clase que representa los datos que enviamos al servidor
 * cuando el usuario intenta iniciar sesión.
 */
data class LoginRequest(
    val tenantId: String,   // Identificador de la empresa
    val correo: String,     // Correo del usuario
    val clave: String       // Contraseña del usuario
)

/**
 * Clase que representa la respuesta que nos devuelve el servidor
 * después de intentar hacer login.
 */
data class LoginResponse(
    val success: Boolean,           // true si el login fue exitoso
    val mensaje: String? = null,    // Mensaje de error o éxito
    val nombreUsuario: String? = null, // Nombre del usuario logueado
    val token: String? = null       // Token de autenticación (si el backend lo usa)
)

/**
 * Clase que representa los datos de una cotización
 * que vamos a enviar al servidor para guardarla.
 */
data class CotizacionRequest(
    val tenantId: String,               // Empresa a la que pertenece
    val moneda: String,                 // CLP, UF o USD
    val items: List<CotizacionItem>,    // Lista de productos de la cotización
    val subtotal: Double,               // Suma de los productos
    val impuesto: Double,               // IVA (19%)
    val total: Double                   // Total final (subtotal + impuesto)
)

/**
 * Interfaz que define los endpoints (URLs) de nuestra API.
 * Retrofit se encarga de convertir estas funciones en llamadas HTTP reales.
 */
interface ApiService {

    /**
     * Endpoint para iniciar sesión.
     * Envía un POST a /api/login con los datos del usuario.
     */
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * Endpoint para guardar una cotización en el servidor.
     * Envía un POST a /api/cotizaciones.
     */
    @POST("api/cotizaciones")
    suspend fun guardarCotizacion(@Body request: CotizacionRequest): Response<Map<String, Any>>

    /**
     * Endpoint para obtener las cotizaciones guardadas en el servidor.
     * Hace un GET a /api/cotizaciones.
     */
    @GET("api/cotizaciones")
    suspend fun obtenerCotizaciones(): Response<List<CotizacionItem>>
}