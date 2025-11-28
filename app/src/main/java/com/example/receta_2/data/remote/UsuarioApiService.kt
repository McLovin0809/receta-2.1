package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {
    @POST("api/usuarios/login")
    suspend fun login(@Body body: Map<String, String>): Response<Usuario>

    @POST("api/usuarios")
    suspend fun registrar(@Body usuario: Usuario): Response<Usuario>

    @GET("api/usuarios/{id}")
    suspend fun obtenerPorId(@Path("id") id: Int): Response<Usuario>
}
