package com.example.receta_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receta_2.data.model.*
import com.example.receta_2.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val recetaRepository: RecetaRepository,
    private val categoriaRepository: CategoriaRepository,
    private val subcategoriaRepository: SubcategoriaRepository
) : ViewModel() {

    private val _recetas = MutableStateFlow<List<Receta>>(emptyList())
    val recetas: StateFlow<List<Receta>> = _recetas

    private val _recetaDetalle = MutableStateFlow<Receta?>(null)
    val recetaDetalle: StateFlow<Receta?> = _recetaDetalle

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias

    private val _subcategorias = MutableStateFlow<List<Subcategoria>>(emptyList())
    val subcategorias: StateFlow<List<Subcategoria>> = _subcategorias

    fun cargarCategorias() = viewModelScope.launch {
        val res = categoriaRepository.listarCategorias()
        if (res.isSuccessful) _categorias.value = res.body().orEmpty()
    }

    fun cargarSubcategorias() = viewModelScope.launch {
        val res = subcategoriaRepository.listarSubcategorias()
        if (res.isSuccessful) _subcategorias.value = res.body().orEmpty()
    }

    fun cargarRecetas() = viewModelScope.launch {
        val res = recetaRepository.listarRecetas()
        if (res.isSuccessful) _recetas.value = res.body().orEmpty()
    }

    fun cargarRecetasPorSubcategoria(id: Int) = viewModelScope.launch {
        val res = recetaRepository.listarRecetas()
        if (res.isSuccessful) {
            _recetas.value = res.body()?.filter { it.subcategoria?.id == id } ?: emptyList()
        }
    }

    fun obtenerRecetaPorId(id: String) = viewModelScope.launch {
        val res = recetaRepository.obtenerReceta(id)
        if (res.isSuccessful) _recetaDetalle.value = res.body()
    }

    fun limpiarRecetas() {
        _recetas.value = emptyList()
    }

    fun crearReceta(
        receta: Receta,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val res = recetaRepository.crearReceta(receta)
                if (res.isSuccessful) {
                    cargarRecetas()
                    onSuccess()
                } else {
                    onError("Error al guardar receta")
                }
            } catch (e: Exception) {
                onError("Error de conexión")
            }
        }
    }
}
