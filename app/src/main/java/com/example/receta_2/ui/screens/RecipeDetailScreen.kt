package com.example.receta_2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.receta_2.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel
) {
    val recipe by recipeViewModel.recetaDetalle.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.titulo ?: "Detalle", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (recipe == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .crossfade(true)
                            .build(),
                        contentDescription = recipe!!.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(recipe!!.titulo, style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(recipe!!.descripcion, style = MaterialTheme.typography.bodyLarge)

                    Divider(Modifier.padding(vertical = 24.dp))
                    Text("Ingredientes", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        recipe!!.ingredientes.lines()
                            .filter { it.isNotBlank() }
                            .forEach { IngredientItem(it) }
                    }

                    Divider(Modifier.padding(vertical = 24.dp))
                    Text("Preparación", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        recipe!!.instrucciones.lines()
                            .filter { it.isNotBlank() }
                            .forEachIndexed { index, step -> StepItem(index + 1, step) }
                    }
                }
            }
        }
    }


@Composable
fun IngredientItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun StepItem(stepNumber: Int, stepText: String) {
    Row {
        Text("$stepNumber.", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.width(12.dp))
        Text(stepText, style = MaterialTheme.typography.bodyLarge)
    }
}
