package com.example.receta_2.data.remote

import com.example.receta_2.data.model.Categoria
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CategoriaApiService {

    @GET("api/categorias")
    suspend fun getCategorias(): List<Categoria>

    @GET("api/categorias/{id}")
    suspend fun getCategoriaPorId(@Path("id") id: Int): Categoria

    @POST("api/categorias")
    suspend fun crearCategoria(@Body categoria: Categoria): Response<Categoria>

    @DELETE("api/categorias/{id}")
    suspend fun eliminarCategoria(@Path("id") id: Int): Response<Unit>

    @GET("api/categorias/nombre/{nombre}")
    suspend fun getCategoriaPorNombre(@Path("nombre") nombre: String): Categoria

    @GET("api/categorias/existe/{nombre}")
    suspend fun existeCategoriaPorNombre(@Path("nombre") nombre: String): Boolean
}