package com.example.receta_2.data.repository

import com.example.receta_2.data.model.Subcategoria
import com.example.receta_2.data.remote.RetrofitInstance
import retrofit2.Response

class SubcategoriaRepository {

    suspend fun getSubcategorias(): List<Subcategoria> {
        return RetrofitInstance.apiSubcategorias.getSubcategorias()
    }

    suspend fun getSubcategoriaPorId(id: Int): Subcategoria {
        return RetrofitInstance.apiSubcategorias.getSubcategoriaPorId(id)
    }

    suspend fun crearSubcategoria(subcategoria: Subcategoria): Response<Subcategoria> {
        return RetrofitInstance.apiSubcategorias.crearSubcategoria(subcategoria)
    }

    suspend fun eliminarSubcategoria(id: Int): Response<Unit> {
        return RetrofitInstance.apiSubcategorias.eliminarSubcategoria(id)
    }

    // Métodos equivalentes a los personalizados de tu backend
    suspend fun buscarPorCategoriaId(categoriaId: Int): List<Subcategoria> {
        return RetrofitInstance.apiSubcategorias.buscarPorCategoriaId(categoriaId)
    }

    suspend fun buscarPorNombre(nombre: String): Subcategoria {
        return RetrofitInstance.apiSubcategorias.buscarPorNombre(nombre)
    }
}