package com.example.myapplication.model

data class CotizacionItem(
    val descripcion: String,
    val precio: Double,
    val cantidad: Int = 1,
    val NombreCliente: String,
    val productoId: Int? = null     // id del producto en la tabla "productos"
)

data class CotizacionEstado(
    val moneda: String = "CLP",
    val items: List<CotizacionItem> = emptyList(),

    // Estado del envío a la API
    val enviando: Boolean = false,
    val mensajeEnvio: String? = null,

    // Productos que vienen de la API (tabla productos en MySQL)
    val productos: List<Producto> = emptyList(),
    val cargandoProductos: Boolean = false,
    val errorProductos: String? = null
)
