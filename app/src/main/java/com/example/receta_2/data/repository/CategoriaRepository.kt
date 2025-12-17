// com/example/receta_2/data/repository/CategoriaRepository.kt
package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Categoria
import com.example.receta_2.data.remote.CategoriaApiService
import retrofit2.Response

class CategoriaRepository(private val api: CategoriaApiService) {
    suspend fun listarCategorias(): Response<List<Categoria>> = api.listarCategorias()
    suspend fun obtenerCategoria(id: Int): Response<Categoria> = api.obtenerCategoria(id)
}
