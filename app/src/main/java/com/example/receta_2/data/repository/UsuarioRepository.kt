package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Usuario
import com.example.receta_2.data.remote.RetrofitInstance
import retrofit2.Response

class UsuarioRepository {

    suspend fun crearUsuario(usuario: Usuario): Response<Usuario> {
        return RetrofitInstance.apiUsuario.crearUsuario(usuario)
    }

    suspend fun login(email: String, password: String): Usuario? {
        return try {
            val user = RetrofitInstance.apiUsuario.getUsuarioPorEmail(email)
            if (user.password == password) user else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun getUsuarioPorEmail(email: String): Usuario? {
        return try {
            RetrofitInstance.apiUsuario.getUsuarioPorEmail(email)
        } catch (e: Exception) {
            null
        }
    }
}