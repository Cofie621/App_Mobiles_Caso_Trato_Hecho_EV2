package com.example.myapplication.model

/**
 * Producto que viene de la API (tabla "productos" en MySQL / XAMPP).
 * Los nombres de los campos coinciden con el JSON de api/productos.php.
 */
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val stock: Int = 0
)
