// com/example/receta_2/viewmodel/RecipeViewModelFactory.kt
package com.example.receta_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receta_2.data.repository.CategoriaRepository
import com.example.receta_2.data.repository.RecetaRepository
import com.example.receta_2.data.repository.SubcategoriaRepository

class RecipeViewModelFactory(
    private val recetaRepository: RecetaRepository,
    private val categoriaRepository: CategoriaRepository,
    private val subcategoriaRepository: SubcategoriaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(recetaRepository, categoriaRepository, subcategoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
