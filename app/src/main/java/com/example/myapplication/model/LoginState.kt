package com.example.myapplication.model

data class LoginUiState(
    val tenantId: String = "",
    val correo: String = "",
    val clave: String = "",
    val errores: LoginErrores = LoginErrores()
)