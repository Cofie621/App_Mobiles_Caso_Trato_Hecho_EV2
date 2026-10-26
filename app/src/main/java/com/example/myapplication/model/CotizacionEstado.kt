package com.example.myapplication.model

data class CotizacionItem(
    val descripcion: String,
    val precio: Double,
    val cantidad: Int = 1,
    val NombreCliente: String
)

data class CotizacionEstado(
    val moneda: String = "CLP",
    val items: List<CotizacionItem> = emptyList()
)
