package com.example.receta_2.data.remote

import com.example.receta_2.data.model.ImagenReceta
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ImagenRecetaApiService {
    @Multipart
    @POST("api/imagenesReceta/upload")
    suspend fun subirImagen(
        @Part file: MultipartBody.Part,
        @Part("recetaId") recetaId: RequestBody
    ): Response<ImagenReceta>

    @GET("api/imagenesReceta/receta/{id}")
    suspend fun obtenerPorReceta(@Path("id") id: Int): Response<ImagenReceta>

    companion object {
        fun create(): ImagenRecetaApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://recetaback-main.onrender.com/") // 🔁 Reemplaza con tu URL real
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ImagenRecetaApiService::class.java)
        }
    }
}
