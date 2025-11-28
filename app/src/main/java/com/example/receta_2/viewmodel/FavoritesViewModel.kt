package com.example.receta_2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.receta_2.data.model.Receta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FavoritesViewModel : ViewModel() {

    private val _favoriteRecipes = MutableStateFlow<List<Receta>>(emptyList())
    val favoriteRecipes: StateFlow<List<Receta>> = _favoriteRecipes

    fun toggleFavorite(receta: Receta) {
        _favoriteRecipes.update { list ->
            val mutable = list.toMutableList()
            if (mutable.any { it.id == receta.id }) {
                mutable.removeAll { it.id == receta.id }
            } else {
                mutable.add(receta)
            }
            mutable
        }
    }

    fun isFavorite(id: Int?): Boolean =
        _favoriteRecipes.value.any { it.id == id }
}
