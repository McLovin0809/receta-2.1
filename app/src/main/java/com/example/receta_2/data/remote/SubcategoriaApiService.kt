package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Subcategoria
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SubcategoriaApiService {
    @GET("api/subcategorias")
    suspend fun listarSubcategorias(): Response<List<Subcategoria>>

    @GET("api/subcategorias/{id}")
    suspend fun obtenerPorId(@Path("id") id: Int): Response<Subcategoria>

    @POST("api/subcategorias")
    suspend fun crearSubcategoria(@Body sub: Subcategoria): Response<Subcategoria>

    @PUT("api/subcategorias/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body subcategoria: Subcategoria): Response<Subcategoria>

    @PATCH("api/subcategorias/{id}")
    suspend fun actualizarParcial(@Path("id") id: Int, @Body cambios: Subcategoria): Response<Subcategoria>

    @DELETE("api/subcategorias/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Void>

    companion object {
        fun create(): SubcategoriaApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://recetaback-main.onrender.com/") // 🔁 Reemplaza con tu URL real
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(SubcategoriaApiService::class.java)
        }
    }
}
