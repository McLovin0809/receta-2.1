package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Categoria
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriaApiService {

    @GET("api/categorias")
    suspend fun listarCategorias(): Response<List<Categoria>>

    @GET("api/categorias/{id}")
    suspend fun obtenerCategoria(@Path("id") id: Int): Response<Categoria>
}
