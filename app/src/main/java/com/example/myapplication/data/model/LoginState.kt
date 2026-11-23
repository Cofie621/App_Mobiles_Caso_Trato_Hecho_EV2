package com.example.myapplication.data.model

data class LoginUiState(
    val tenantId: String = "",
    val correo: String = "",
    val clave: String = "",
    val errores: LoginErrores = LoginErrores()
)