package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Usuario
import com.example.receta_2.data.remote.RetrofitInstance
import com.example.receta_2.data.remote.UsuarioApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository(apiUsuario: UsuarioApiService) {
    suspend fun login(email: String, password: String): Response<Usuario> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.apiUsuario.login(mapOf("email" to email, "password" to password))
        }

    suspend fun registrar(nombre: String?, email: String, password: String): Response<Usuario> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.apiUsuario.registrar(Usuario(nombre = nombre, email = email, password = password))
        }
}
