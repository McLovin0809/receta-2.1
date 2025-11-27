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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    favoritesViewModel: FavoritesViewModel,
    isLoggedIn: Boolean
) {
    val favoriteRecipes by favoritesViewModel.favoriteRecipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (favoriteRecipes.isEmpty()) {
            Text("Aún no has guardado recetas favoritas.", modifier = Modifier.padding(paddingValues).padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(favoriteRecipes, key = { it.id }) { recipe ->
                    RecipeItemCard(
                        recipe = recipe,
                        isFavorite = true,
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
