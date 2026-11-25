package com.example.myapplication.model

data class LoginUiState(
    val tenantId: String = "",
    val correo: String = "",
    val clave: String = "",
    val errores: LoginErrores = LoginErrores(),

    // Estado de la llamada a la API
    val cargando: Boolean = false,          // true mientras esperamos al servidor
    val mensajeError: String? = null,       // Error devuelto por la API o de conexión
    val loginExitoso: Boolean = false,      // true cuando el servidor aceptó el login
    val nombreUsuario: String? = null,      // Nombre que devuelve el servidor
    val token: String? = null               // Token de autenticación (si el backend lo usa)
)