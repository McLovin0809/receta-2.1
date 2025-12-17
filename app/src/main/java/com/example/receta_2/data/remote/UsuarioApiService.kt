// com/example/receta_2/data/remote/UsuarioApiService.kt
package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)

interface UsuarioApiService {
    @POST("api/usuarios")
    suspend fun registrar(@Body usuario: Usuario): Response<Usuario>

    @POST("api/usuarios/login")
    suspend fun login(@Body request: Map<String, String>): Response<Usuario>
}
