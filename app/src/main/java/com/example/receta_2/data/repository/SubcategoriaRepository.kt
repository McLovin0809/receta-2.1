package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Subcategoria
import com.example.receta_2.data.remote.SubcategoriaApiService
import retrofit2.Response

class SubcategoriaRepository(private val api: SubcategoriaApiService) {
    suspend fun listar(): Response<List<Subcategoria>> = api.listarSubcategorias()
    suspend fun obtener(id: Int): Response<Subcategoria> = api.obtenerPorId(id)
    suspend fun crear(sub: Subcategoria): Response<Subcategoria> = api.crearSubcategoria(sub)
}
