package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.AlmacenamientoLocal
import com.example.myapplication.model.CotizacionItem
import com.example.myapplication.model.Producto
import com.example.myapplication.data.remote.CotizacionRequest
import com.example.myapplication.data.remote.LoginRequest
import com.example.myapplication.data.remote.LoginResponse
import com.example.myapplication.data.remote.RetrofitInstance

class LoginRepository {
    private val api = RetrofitInstance.api

    suspend fun login(tenantId: String, correo: String, clave: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(tenantId, correo, clave)
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class CotizacionRepository(private val context: Context) {
    // Instancia de la API
    private val api = RetrofitInstance.api

    /**
     * Guarda una cotización.
     * Primero la guarda localmente (para que no se pierda)
     * y después intenta enviarla al servidor.
     *
     * @return Result<Unit> → success si se envió al servidor, failure si solo se guardó local
     */

    suspend fun guardarCotizacion(
        tenantId: String,
        moneda: String,
        items: List<CotizacionItem>,
        subtotal: Double,
        impuesto: Double,
        total: Double
    ): Result<Unit>{
        // 1. Siempre guardamos localmente primero (seguridad)
        AlmacenamientoLocal.guardarCotizaciones(context, items)

        // 2. Intentamos enviar al servidor
        return try{
            val request = CotizacionRequest(tenantId, moneda, items, subtotal, impuesto, total)

            val response = api.guardarCotizacion(request)

            if (response.isSuccessful){
                // Se guardó correctamente en el servidor
                Result.success(Unit)
            } else {
                // El servidor respondió con error
                Result.failure(Exception("Error servidor: ${response.code()}"))
            }
        }catch (e: Exception){
            // Falló la conexión (sin internet, ngrok caído, etc.)
            // Pero la cotización ya quedó guardada localmente
            Result.failure(e)
        }
    }

    /**
     * Obtiene las cotizaciones guardadas en el dispositivo (SharedPreferences).
     * Esta función es síncrona porque lee datos locales.
     */
    fun obtenerCotizaciones(): List<CotizacionItem> {
        return AlmacenamientoLocal.obtenerCotizaciones(context)
    }

    /**
     * Obtiene los productos de la tabla "productos" (GET api/productos.php).
     */
    suspend fun obtenerProductos(): Result<List<Producto>> {
        return try {
            val response = api.obtenerProductos()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene las cotizaciones guardadas en el servidor (GET api/cotizaciones.php).
     */
    suspend fun obtenerCotizacionesServidor(): Result<List<CotizacionItem>> {
        return try {
            val response = api.obtenerCotizaciones()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}