package com.example.receta_2.data.remote

import com.example.receta_2.data.model.ImagenReceta
import retrofit2.Response
import retrofit2.http.*

interface ImagenRecetaApiService {

    @GET("api/imagenes")
    suspend fun listarImagenes(): Response<List<ImagenReceta>>

    @POST("api/imagenes")
    suspend fun subirImagen(@Body imagen: ImagenReceta): Response<ImagenReceta>

    @DELETE("api/imagenes/{id}")
    suspend fun eliminarImagen(@Path("id") id: Int): Response<Void>
}
