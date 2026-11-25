package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.LoginErrores
import com.example.myapplication.model.LoginUiState
import com.example.myapplication.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Repositorio que se comunica con la API (Retrofit)
    private val repository = LoginRepository()

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun onTenantIdChange(valor: String) {
        _estado.update { it.copy(tenantId = valor, errores = it.errores.copy(tenantId = null), mensajeError = null) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null), mensajeError = null) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null), mensajeError = null) }
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

    /**
     * Valida los campos y, si están bien, llama a la API de login.
     * El resultado queda en el estado (loginExitoso / mensajeError),
     * y la pantalla reacciona a esos cambios.
     */
    fun iniciarSesion() {
        if (!validarLogin()) return

        val actual = _estado.value
        _estado.update { it.copy(cargando = true, mensajeError = null) }

        viewModelScope.launch {
            val resultado = repository.login(actual.tenantId, actual.correo, actual.clave)

            resultado.onSuccess { respuesta ->
                if (respuesta.success) {
                    _estado.update {
                        it.copy(
                            cargando = false,
                            loginExitoso = true,
                            nombreUsuario = respuesta.nombreUsuario,
                            token = respuesta.token
                        )
                    }
                } else {
                    // El servidor respondió pero rechazó las credenciales
                    _estado.update {
                        it.copy(
                            cargando = false,
                            mensajeError = respuesta.mensaje ?: "Credenciales incorrectas"
                        )
                    }
                }
            }.onFailure { error ->
                // Error HTTP (401, 500...) o de conexión (sin internet, ngrok caído)
                _estado.update {
                    it.copy(
                        cargando = false,
                        mensajeError = error.message ?: "No se pudo conectar con el servidor"
                    )
                }
            }
        }
    }

    /** Se llama después de navegar, para que no vuelva a navegar al regresar al login. */
    fun consumirLoginExitoso() {
        _estado.update { it.copy(loginExitoso = false) }
    }
}