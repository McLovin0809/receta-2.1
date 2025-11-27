package com.example.receta_2.repository

import com.example.receta_2.data.model.Recipe
import com.example.receta_2.data.model.SearchCategory
import com.example.receta_2.data.model.sampleRecipes
import com.example.receta_2.data.model.allCategories

class RecipeRepository {
    private val recipes = sampleRecipes.toMutableList()
    private val categories = allCategories.toMutableList()

    suspend fun getAllRecipes(): List<Recipe> = recipes
    suspend fun getRecipeById(id: String): Recipe? = recipes.find { it.id == id }
    suspend fun getAllCategories(): List<SearchCategory> = categories
    suspend fun insertRecipe(recipe: Recipe) { recipes.add(recipe) }
}
