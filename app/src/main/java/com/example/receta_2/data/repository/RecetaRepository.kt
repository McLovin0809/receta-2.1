package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Receta
import com.example.receta_2.data.remote.RetrofitInstance
import retrofit2.Response

class RecetaRepository {

    suspend fun getRecetas(): List<Receta> {
        return RetrofitInstance.apiRecetas.getRecetas()
    }

    suspend fun getRecetaPorId(id: Int): Receta {
        return RetrofitInstance.apiRecetas.getRecetaPorId(id)
    }

    suspend fun crearReceta(receta: Receta): Response<Receta> {
        return RetrofitInstance.apiRecetas.crearReceta(receta)
    }

    suspend fun actualizarReceta(id: Int, receta: Receta): Response<Receta> {
        return RetrofitInstance.apiRecetas.actualizarReceta(id, receta)
    }

    suspend fun eliminarReceta(id: Int): Response<Unit> {
        return RetrofitInstance.apiRecetas.eliminarReceta(id)
    }

    // Métodos equivalentes a los personalizados de tu backend
    suspend fun buscarPorTitulo(titulo: String): List<Receta> {
        return RetrofitInstance.apiRecetas.buscarPorTitulo(titulo)
    }

    suspend fun buscarPorUsuarioId(usuarioId: Int): List<Receta> {
        return RetrofitInstance.apiRecetas.buscarPorUsuarioId(usuarioId)
    }

    suspend fun buscarPorCategoriaId(categoriaId: Int): List<Receta> {
        return RetrofitInstance.apiRecetas.buscarPorCategoriaId(categoriaId)
    }

    suspend fun buscarPorSubcategoriaId(subcategoriaId: Int): List<Receta> {
        return RetrofitInstance.apiRecetas.buscarPorSubcategoriaId(subcategoriaId)
    }
}