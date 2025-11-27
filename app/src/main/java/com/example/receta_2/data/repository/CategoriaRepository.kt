package com.example.receta_2.data.repository


import com.example.receta_2.data.model.Categoria
import com.example.receta_2.data.remote.RetrofitInstance
import retrofit2.Response


class CategoriaRepository {

    suspend fun getCategorias(): List<Categoria> {
        return RetrofitInstance.apiCategorias.getCategorias()
    }

    suspend fun getCategoriaPorId(id: Int): Categoria {
        return RetrofitInstance.apiCategorias.getCategoriaPorId(id)
    }

    suspend fun crearCategoria(categoria: Categoria): Response<Categoria> {
        return RetrofitInstance.apiCategorias.crearCategoria(categoria)
    }

    suspend fun eliminarCategoria(id: Int): Response<Unit> {
        return RetrofitInstance.apiCategorias.eliminarCategoria(id)
    }

    suspend fun getCategoriaPorNombre(nombre: String): Categoria {
        return RetrofitInstance.apiCategorias.getCategoriaPorNombre(nombre)
    }

    suspend fun existeCategoriaPorNombre(nombre: String): Boolean {
        return RetrofitInstance.apiCategorias.existeCategoriaPorNombre(nombre)
    }
}