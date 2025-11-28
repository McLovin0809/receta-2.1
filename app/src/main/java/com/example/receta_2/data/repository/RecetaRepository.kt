package com.example.receta_2.data.repository

import com.example.receta_2.data.model.*
import com.example.receta_2.data.remote.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class RecetaRepository(
    private val apiRecetas: RecetaApiService,
    private val apiImagen: ImagenRecetaApiService,
    private val apiCategorias: CategoriaApiService,
    private val apiSubcategorias: SubcategoriaApiService
) {
    suspend fun recetasPorCategoria(id: Int): Response<List<Receta>> =
        apiRecetas.getByCategoria(id)

    suspend fun detalle(id: Int): Response<Receta> =
        apiRecetas.getDetalle(id)

    suspend fun crearReceta(receta: Receta): Response<Receta> =
        apiRecetas.crearReceta(receta)

    suspend fun subirImagen(file: MultipartBody.Part, recetaId: Int): Response<ImagenReceta> {
        val idBody = RequestBody.create("text/plain".toMediaType(), recetaId.toString())
        return apiImagen.subirImagen(file, idBody)
    }

    // ✅ Nuevos métodos
    suspend fun listarCategorias(): Response<List<Categoria>> =
        apiCategorias.listarCategorias()

    suspend fun listarSubcategorias(): Response<List<Subcategoria>> =
        apiSubcategorias.listarSubcategorias()
}
