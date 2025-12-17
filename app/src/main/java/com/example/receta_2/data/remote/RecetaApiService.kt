package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Receta
import retrofit2.Response
import retrofit2.http.*

interface RecetaApiService {

    @GET("api/recetas")
    suspend fun listarRecetas(): Response<List<Receta>>

    @GET("api/recetas/{id}")
    suspend fun obtenerReceta(@Path("id") id: String): Response<Receta>

    @POST("api/recetas")
    suspend fun crearReceta(@Body receta: Receta): Response<Receta>

    @PUT("api/recetas/{id}")
    suspend fun actualizarReceta(
        @Path("id") id: Int,
        @Body receta: Receta
    ): Response<Receta>

    @DELETE("api/recetas/{id}")
    suspend fun eliminarReceta(@Path("id") id: Int): Response<Void>
}
