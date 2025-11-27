package com.example.receta_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.receta_2.data.model.Recipe
import com.example.receta_2.data.model.SearchCategory
import com.example.receta_2.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RecipeRepository()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    private val _categories = MutableStateFlow<List<SearchCategory>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _currentRecipe = MutableStateFlow<Recipe?>(null)
    val currentRecipe = _currentRecipe.asStateFlow()

    init {
        viewModelScope.launch {
            _recipes.value = repo.getAllRecipes()
            _categories.value = repo.getAllCategories()
        }
    }

    fun loadRecipeById(id: String) {
        _currentRecipe.value = null
        viewModelScope.launch {
            _currentRecipe.value = repo.getRecipeById(id)
        }
    }

    fun addRecipe(newRecipe: Recipe) {
        viewModelScope.launch {
            repo.insertRecipe(newRecipe)
            _recipes.value = repo.getAllRecipes()
        }
    }
}
