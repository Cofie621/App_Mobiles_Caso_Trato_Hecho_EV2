package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.AlmacenamientoLocal
import com.example.myapplication.data.model.CotizacionItem
import com.example.myapplication.data.remote.CotizacionRequest
import com.example.myapplication.data.remote.RetrofitInstance

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
}
