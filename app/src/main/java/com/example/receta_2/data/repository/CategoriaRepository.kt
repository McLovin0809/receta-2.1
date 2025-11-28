package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Categoria
import com.example.receta_2.data.remote.CategoriaApiService
import retrofit2.Response

class CategoriaRepository(private val api: CategoriaApiService) {
    suspend fun listar(): Response<List<Categoria>> = api.listarCategorias()
    suspend fun obtener(id: Int): Response<Categoria> = api.obtenerCategoria(id)
    suspend fun obtenerPorNombre(nombre: String): Response<Categoria> = api.obtenerPorNombre(nombre)
}
