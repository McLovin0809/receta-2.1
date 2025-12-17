// com/example/receta_2/data/repository/SubcategoriaRepository.kt
package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Subcategoria
import com.example.receta_2.data.remote.SubcategoriaApiService
import retrofit2.Response

class SubcategoriaRepository(private val api: SubcategoriaApiService) {
    suspend fun listarSubcategorias(): Response<List<Subcategoria>> = api.listarSubcategorias()
    suspend fun obtenerSubcategoria(id: Int): Response<Subcategoria> = api.obtenerSubcategoria(id)
}
