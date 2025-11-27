package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Subcategoria
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubcategoriaApiService {

    @GET("api/subcategorias")
    suspend fun getSubcategorias(): List<Subcategoria>

    @GET("api/subcategorias/{id}")
    suspend fun getSubcategoriaPorId(@Path("id") id: Int): Subcategoria

    @POST("api/subcategorias")
    suspend fun crearSubcategoria(@Body subcategoria: Subcategoria): Response<Subcategoria>

    @DELETE("api/subcategorias/{id}")
    suspend fun eliminarSubcategoria(@Path("id") id: Int): Response<Unit>

    @GET("api/subcategorias/categoria/{categoriaId}")
    suspend fun buscarPorCategoriaId(@Path("categoriaId") categoriaId: Int): List<Subcategoria>

    @GET("api/subcategorias/nombre/{nombre}")
    suspend fun buscarPorNombre(@Path("nombre") nombre: String): Subcategoria
}