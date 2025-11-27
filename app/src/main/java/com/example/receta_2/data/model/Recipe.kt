package com.example.receta_2.data.model

import androidx.annotation.DrawableRes

data class SearchCategory(
    val id: String,
    val name: String,
    @DrawableRes val image: Int,
    val recipeCount: Int,
    val group: CategoryGroup
)

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val categoryIds: List<String>,
    val ingredients: List<String>,
    val steps: List<String>
)
