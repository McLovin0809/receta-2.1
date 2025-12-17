package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.example.receta_2.data.model.Receta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    navController: NavController,
    categoryId: Int?,
    categoryName: String?,
    favoritesViewModel: FavoritesViewModel,
    recipeViewModel: RecipeViewModel,
    isLoggedIn: Boolean
) {
    // ✅ Recolectamos el StateFlow para que Compose observe cambios
    val recetas by recipeViewModel.recetas.collectAsState()
    val favoriteRecipes by favoritesViewModel.favoriteRecipes.collectAsState()

    val recipesToShow = if (categoryId != null) {
        recetas.filter { it.categoria?.id == categoryId }
    } else recetas

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
            Text(
                "Aún no hay recetas en esta categoría.",
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(recipesToShow, key = { it.id ?: 0 }) { receta: Receta ->
                    RecipeItemCard(
                        recipe = receta,
                        isFavorite = favoriteRecipes.any { it.id == receta.id },
                        onToggleFavorite = { favoritesViewModel.toggleFavorite(receta) },
                        onDetailsClick = { navController.navigate("recipe_detail/${receta.id}") },
                        isLoggedIn = { if (isLoggedIn) navController.navigate("login") }
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // ✅ Espaciado correcto
                }
            }
        }
    }
}
