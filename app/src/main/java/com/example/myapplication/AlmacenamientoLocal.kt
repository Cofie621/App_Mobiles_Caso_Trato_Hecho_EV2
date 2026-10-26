package com.example.myapplication


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.myapplication.model.CotizacionItem

// Objeto singleton para manejar el almacenamiento local persistente con SharedPreferences
object AlmacenamientoLocal {

    // Nombre del archivo XML donde se guardarán las preferencias del sistema
    private const val PREF_NAME = "prismatec_prefs"

    // Clave única para identificar el conjunto de datos de cotizaciones
    private const val KEY_COTIZACIONES = "key_cotizaciones"

    // Función privada que inicializa y retorna la instancia de SharedPreferences
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Guarda la lista actual de cotizaciones convirtiéndola a formato JSON
    fun guardarCotizaciones(context: Context, lista: List<CotizacionItem>) {
        val prefs = getPrefs(context)
        val gson = Gson()
        val json = gson.toJson(lista) // Convierte la lista de objetos a texto JSON
        prefs.edit().putString(KEY_COTIZACIONES, json).apply() // Guarda el texto de forma asíncrona
    }

    // Recupera las cotizaciones guardadas previamente en el dispositivo
    fun obtenerCotizaciones(context: Context): List<CotizacionItem> {
        val prefs = getPrefs(context)
        // Lee el JSON almacenado; si está vacío, retorna una lista vacía por defecto
        val json = prefs.getString(KEY_COTIZACIONES, null) ?: return emptyList()

        val gson = Gson()
        // Define el tipo de dato exacto que se va a reconstruir (Lista de CotizacionItem)
        val type = object : TypeToken<List<CotizacionItem>>() {}.type

        // Convierte el texto JSON de vuelta a objetos de Kotlin y los devuelve
        return gson.fromJson(json, type) ?: emptyList()
    }
}