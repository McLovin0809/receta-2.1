package com.example.receta_2.data.repository

import com.example.receta_2.data.model.ImagenReceta
import com.example.receta_2.data.remote.ImagenRecetaApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ImagenRecetaRepository(private val api: ImagenRecetaApiService) {
    suspend fun obtenerPorReceta(id: Int): Response<ImagenReceta> = api.obtenerPorReceta(id)
    suspend fun subirImagen(file: MultipartBody.Part, recetaId: RequestBody): Response<ImagenReceta> =
        api.subirImagen(file, recetaId)
}
