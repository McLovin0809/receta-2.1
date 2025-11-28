package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Categoria
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CategoriaApiService {
    @GET("api/categorias")
    suspend fun listarCategorias(): Response<List<Categoria>>

    @GET("api/categorias/{id}")
    suspend fun obtenerCategoria(@Path("id") id: Int): Response<Categoria>

    @GET("api/categorias/nombre/{nombre}")
    suspend fun obtenerPorNombre(@Path("nombre") nombre: String): Response<Categoria>

    @POST("api/categorias")
    suspend fun crearCategoria(@Body categoria: Categoria): Response<Categoria>

    @PUT("api/categorias/{id}")
    suspend fun actualizarCategoria(@Path("id") id: Int, @Body categoria: Categoria): Response<Categoria>

    @PATCH("api/categorias/{id}")
    suspend fun actualizarParcial(@Path("id") id: Int, @Body cambios: Categoria): Response<Categoria>

    @DELETE("api/categorias/{id}")
    suspend fun eliminarCategoria(@Path("id") id: Int): Response<Void>

    companion object {
        fun create(): CategoriaApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://recetaback-main.onrender.com/") // 🔁 Reemplaza con tu URL real
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CategoriaApiService::class.java)
        }
    }
}
