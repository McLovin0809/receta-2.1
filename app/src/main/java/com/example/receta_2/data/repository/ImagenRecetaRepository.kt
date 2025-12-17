// com/example/receta_2/data/repository/ImagenRepository.kt
package com.example.receta_2.data.repository

import com.example.receta_2.data.model.ImagenReceta
import com.example.receta_2.data.remote.ImagenRecetaApiService
import retrofit2.Response

class ImagenRepository(private val api: ImagenRecetaApiService) {
    suspend fun listarImagenes(): Response<List<ImagenReceta>> = api.listarImagenes()
    suspend fun subirImagen(imagen: ImagenReceta): Response<ImagenReceta> = api.subirImagen(imagen)
    suspend fun eliminarImagen(id: Int): Response<Void> = api.eliminarImagen(id)
}
