// com/example/receta_2/data/repository/RecetaRepository.kt
package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Receta
import com.example.receta_2.data.remote.RecetaApiService
import retrofit2.Response

class RecetaRepository(private val apiRecetas: RecetaApiService) {
    suspend fun listarRecetas(): Response<List<Receta>> = apiRecetas.listarRecetas()
    suspend fun obtenerReceta(id: String): Response<Receta> = apiRecetas.obtenerReceta(id)
    suspend fun crearReceta(receta: Receta): Response<Receta> = apiRecetas.crearReceta(receta)
    suspend fun actualizarReceta(id: Int, receta: Receta): Response<Receta> = apiRecetas.actualizarReceta(id, receta)
    suspend fun eliminarReceta(id: Int): Response<Void> = apiRecetas.eliminarReceta(id)
}
