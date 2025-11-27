package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Receta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecetaApiService {

    @GET("api/recetas")
    suspend fun getRecetas(): List<Receta>

    @GET("api/recetas/{id}")
    suspend fun getRecetaPorId(@Path("id") id: Int): Receta

    @POST("api/recetas")
    suspend fun crearReceta(@Body receta: Receta): Response<Receta>

    @PUT("api/recetas/{id}")
    suspend fun actualizarReceta(@Path("id") id: Int, @Body receta: Receta): Response<Receta>

    @DELETE("api/recetas/{id}")
    suspend fun eliminarReceta(@Path("id") id: Int): Response<Unit>

    // Endpoints personalizados (igual que tu Spring Repository)
    @GET("api/recetas/titulo/{titulo}")
    suspend fun buscarPorTitulo(@Path("titulo") titulo: String): List<Receta>

    @GET("api/recetas/usuario/{usuarioId}")
    suspend fun buscarPorUsuarioId(@Path("usuarioId") usuarioId: Int): List<Receta>

    @GET("api/recetas/categoria/{categoriaId}")
    suspend fun buscarPorCategoriaId(@Path("categoriaId") categoriaId: Int): List<Receta>

    @GET("api/recetas/subcategoria/{subcategoriaId}")
    suspend fun buscarPorSubcategoriaId(@Path("subcategoriaId") subcategoriaId: Int): List<Receta>
}