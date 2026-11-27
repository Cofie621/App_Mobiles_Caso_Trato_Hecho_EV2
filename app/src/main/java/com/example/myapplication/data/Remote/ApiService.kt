package com.example.myapplication.data.remote

import com.example.myapplication.model.CotizacionItem
import com.example.myapplication.model.Producto
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
 *
 * La API está hecha en PHP y corre en XAMPP (carpeta htdocs/trato_hecho).
 * Las rutas se suman a BASE_URL, por ejemplo:
 *   https://xxxx.ngrok-free.dev/trato_hecho/ + api/productos.php
 */
interface ApiService {

    /**
     * Endpoint para iniciar sesión.
     * Envía un POST a api/login.php con los datos del usuario.
     */
    @POST("api/login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * Endpoint para obtener los productos de la tabla "productos".
     * Hace un GET a api/productos.php.
     */
    @GET("api/productos.php")
    suspend fun obtenerProductos(): Response<List<Producto>>

    /**
     * Endpoint para guardar una cotización en el servidor.
     * Envía un POST a api/cotizaciones.php.
     */
    @POST("api/cotizaciones.php")
    suspend fun guardarCotizacion(@Body request: CotizacionRequest): Response<Map<String, Any>>

    /**
     * Endpoint para obtener los productos cotizados guardados en el servidor.
     * Hace un GET a api/cotizaciones.php.
     */
    @GET("api/cotizaciones.php")
    suspend fun obtenerCotizaciones(): Response<List<CotizacionItem>>
}