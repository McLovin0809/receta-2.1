package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface UsuarioApiService {

    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("api/usuarios/email/{email}")
    suspend fun getUsuarioPorEmail(@Path("email") email: String): Usuario

}