package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Subcategoria
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SubcategoriaApiService {

    @GET("api/subcategorias")
    suspend fun listarSubcategorias(): Response<List<Subcategoria>>

    @GET("api/subcategorias/{id}")
    suspend fun obtenerSubcategoria(@Path("id") id: Int): Response<Subcategoria>
}
