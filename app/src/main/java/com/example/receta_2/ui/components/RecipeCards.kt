package com.example.receta_2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.receta_2.data.model.Receta

@Composable
fun RecipeItemCard(
    recipe: Receta,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onDetailsClick: () -> Unit,
    isLoggedIn: () -> Unit
) {
    Card(onClick = onDetailsClick) {
        Column {
            AsyncImage(
                model = recipe.id,
                contentDescription = recipe.titulo,
                modifier = Modifier.fillMaxWidth().height(160.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(recipe.titulo, style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { if (isLoggedIn() != null) onToggleFavorite() }) {
                    if (isFavorite) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Quitar de favoritos",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Agregar a favoritos")
                    }
                }
            }
            Text(
                recipe.descripcion,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
