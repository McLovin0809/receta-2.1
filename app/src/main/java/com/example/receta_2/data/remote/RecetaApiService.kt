package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Receta
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RecetaApiService {
    @GET("api/recetas/categoria/{categoriaId}")
    suspend fun getByCategoria(@Path("categoriaId") id: Int): Response<List<Receta>>

    @GET("api/recetas/{id}")
    suspend fun getDetalle(@Path("id") id: Int): Response<Receta>

    @POST("api/recetas")
    suspend fun crearReceta(@Body receta: Receta): Response<Receta>

    companion object {
        fun create(): RecetaApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://recetaback-main.onrender.com/") // 🔁 Reemplaza con tu URL real
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RecetaApiService::class.java)
        }
    }
}
