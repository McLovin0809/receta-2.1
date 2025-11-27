package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receta_2.ui.components.RecipeItemCard
import com.example.receta_2.viewmodel.FavoritesViewModel
import com.example.receta_2.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    navController: NavController,
    categoryId: String?,
    categoryName: String?,
    favoritesViewModel: FavoritesViewModel,
    recipeViewModel: RecipeViewModel,
    isLoggedIn: Boolean
) {
    val recipes by recipeViewModel.recipes.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteRecipeIds.collectAsState()
    val recipesToShow = recipes.filter { it.categoryIds.contains(categoryId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName ?: "Recetas") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (recipesToShow.isEmpty()) {
            Text("Aún no hay recetas en esta categoría.", modifier = Modifier.padding(paddingValues).padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(recipesToShow, key = { it.id }) { recipe ->
                    RecipeItemCard(
                        recipe = recipe,
                        isFavorite = favoriteIds.contains(recipe.id),
                        onToggleFavorite = { favoritesViewModel.toggleFavorite(recipe.id) },
                        onDetailsClick = { navController.navigate("recipe_detail/${recipe.id}") },
                        isLoggedIn = isLoggedIn
                    )
                    Spacer(Modifier.padding(8.dp))
                }
            }
        }
    }
}
