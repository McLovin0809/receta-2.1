package com.example.receta_2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receta_2.data.model.*
import com.example.receta_2.data.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RecipeViewModel(
    private val repository: RecetaRepository
) : ViewModel() {

    // ✅ Recetas
    private val _recetas = MutableStateFlow<List<Receta>>(emptyList())
    val recetas: StateFlow<List<Receta>> = _recetas

    private val _recetaDetalle = MutableStateFlow<Receta?>(null)
    val recetaDetalle: StateFlow<Receta?> = _recetaDetalle

    // ✅ Categorías
    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias

    // ✅ Subcategorías
    private val _subcategorias = MutableStateFlow<List<Subcategoria>>(emptyList())
    val subcategorias: StateFlow<List<Subcategoria>> = _subcategorias

    // -------------------------------
    // Métodos de carga
    // -------------------------------
    fun cargarRecetasPorCategoria(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.recetasPorCategoria(id)
                if (response.isSuccessful) {
                    _recetas.value = response.body().orEmpty()
                }
            } catch (e: Exception) {
                _recetas.value = emptyList()
            }
        }
    }

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.detalle(id)
                if (response.isSuccessful) {
                    _recetaDetalle.value = response.body()
                } else {
                    _recetaDetalle.value = null
                }
            } catch (e: Exception) {
                _recetaDetalle.value = null
            }
        }
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            try {
                val response = repository.listarCategorias()
                if (response.isSuccessful) {
                    _categorias.value = response.body().orEmpty()
                } else {
                    Log.e("API", "Error categorías: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Excepción categorías", e)
            }
        }
    }

    fun cargarSubcategorias() {
        viewModelScope.launch {
            try {
                val response = repository.listarSubcategorias()
                if (response.isSuccessful) {
                    _subcategorias.value = response.body().orEmpty()
                } else {
                    Log.e("API", "Error subcategorías: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Excepción subcategorías", e)
            }
        }
    }

    // -------------------------------
    // Crear receta con imagen
    // -------------------------------
    fun crear(
        receta: Receta,
        imagenPart: MultipartBody.Part?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val crearResp = repository.crearReceta(receta)
                if (!crearResp.isSuccessful) {
                    onResult(false)
                    return@launch
                }

                val recetaCreada = crearResp.body()
                val recetaId = recetaCreada?.id
                if (recetaId == null) {
                    onResult(false)
                    return@launch
                }

                // Subir imagen si existe
                if (imagenPart != null) {
                    val subirResp = repository.subirImagen(imagenPart, recetaId)
                    if (subirResp.isSuccessful) {
                        recetaCreada?.imagenUrl = subirResp.body()?.url
                    }
                }

                // ✅ Actualizamos lista local
                recetaCreada?.let {
                    _recetas.value = _recetas.value + it
                }

                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
