package com.example.myapplication.repository

import com.example.myapplication.data.remote.RetrofitInstance
import com.example.myapplication.data.remote.LoginResponse
import com.example.myapplication.data.remote.LoginRequest

class LoginRepository {
    // Obtenemos la instancia de la API desde RetrofitInstance
    private val api = RetrofitInstance.api


/**
 * Función que intenta iniciar sesión.
 * @param tenantId Identificador de la empresa
 * @param correo Correo del usuario
 * @param clave Contraseña
 * @return Result<LoginResponse> → success si todo salió bien, failure si hubo error
 */
suspend fun login(tenantid: String, correo: String, clave: String): Result<LoginResponse> {
        return try{
            // Creamos el objeto con los datos del login
            val request = LoginRequest(tenantid, correo, clave)

            // Hacemos la llamada a la API
            val response = api.login(request)

            // Verificamos si la respuesta fue exitosa (código 200-299)
            if (response.isSuccessful) {
                // Si fue exitosa, retornamos el cuerpo de la respuesta
                Result.success(response.body()!!)
            } else {
                // El servidor respondió con error (400, 401, 500, etc.)
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        }catch (e: Exception){
            // Error de red, timeout, o cualquier otra excepción
            Result.failure(e)
        }
    }
}