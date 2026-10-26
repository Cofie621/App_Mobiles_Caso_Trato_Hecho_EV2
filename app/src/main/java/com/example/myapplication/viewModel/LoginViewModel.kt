package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.LoginErrores
import com.example.myapplication.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun onTenantIdChange(valor: String) {
        _estado.update { it.copy(tenantId = valor, errores = it.errores.copy(tenantId = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun validarLogin(): Boolean {
        val actual = _estado.value
        val errores = LoginErrores(
            tenantId = if (actual.tenantId.isBlank()) "El identificador de empresa es obligatorio" else null,
            correo = if (!actual.correo.contains("@")) "Correo electrónico inválido" else null,
            clave = if (actual.clave.length < 4) "La contraseña debe tener al menos 4 caracteres" else null
        )

        val hayErrores = listOfNotNull(errores.tenantId, errores.correo, errores.clave).isNotEmpty()
        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}