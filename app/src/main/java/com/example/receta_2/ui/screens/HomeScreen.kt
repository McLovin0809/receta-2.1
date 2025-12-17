// com/example/receta_2/ui/screens/HomeScreen.kt
package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receta_2.data.model.Categoria
import com.example.receta_2.data.model.Receta
import com.example.receta_2.data.model.Subcategoria
import com.example.receta_2.ui.components.RecipeItemCard
import com.example.receta_2.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel,

) {
    val categorias by recipeViewModel.categorias.collectAsState()
    val subcategorias by recipeViewModel.subcategorias.collectAsState()
    val recetas by recipeViewModel.recetas.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategoriaIndex by rememberSaveable { mutableStateOf(0) }
    var selectedSubcategoriaId by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        recipeViewModel.cargarCategorias()
        recipeViewModel.cargarSubcategorias()
        recipeViewModel.cargarRecetas()
    }

    var search by remember { mutableStateOf("") }

    val resultados = remember(search, recetas, categorias, subcategorias) {
        val q = search.lowercase()

        recetas.filter {
            it.titulo.lowercase().contains(q)
        } +
                categorias.filter {
                    it.nombre.lowercase().contains(q)
                } +
                subcategorias.filter {
                    it.nombre.lowercase().contains(q)
                }
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Buscar recetas, categorías...") }
        )

        LazyColumn {
            items(resultados) { item ->
                when (item) {
                    is Receta -> RecipeItemCard(item, false, {}, {}) {
                        navController.navigate("detail/${item.id}")
                    }
                    is Categoria -> Text("📂 ${item.nombre}")
                    is Subcategoria -> Text("📁 ${item.nombre}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas") },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.Person, null)
                    }
                    IconButton(onClick = { navController.navigate("favorite") }) {
                        Icon(Icons.Default.Favorite, null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_recipe") }
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(recetas) {
                Text(it.titulo, modifier = Modifier.padding(16.dp))
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                placeholder = { Text("Buscar recetas...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (categorias.isNotEmpty()) {
                ScrollableTabRow(selectedTabIndex = selectedCategoriaIndex) {
                    categorias.forEachIndexed { index, categoria ->
                        Tab(
                            selected = index == selectedCategoriaIndex,
                            onClick = {
                                selectedCategoriaIndex = index
                                selectedSubcategoriaId = null
                            },
                            text = { Text(categoria.nombre) }
                        )
                    }
                }
            }

            val categoriaSeleccionada = categorias.getOrNull(selectedCategoriaIndex)
            val subcatsFiltradas = subcategorias.filter {
                it.categoria?.id == categoriaSeleccionada?.id
            }

            LazyRow(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subcatsFiltradas) { subcat ->
                    AssistChip(
                        onClick = { selectedSubcategoriaId = subcat.id },
                        label = { Text(subcat.nombre) }
                    )
                }
            }

            val recetasFiltradas = recetas.filter {
                (selectedSubcategoriaId == null || it.subcategoria?.id == selectedSubcategoriaId) &&
                        it.titulo.contains(searchQuery, true)
            }

            if (recetasFiltradas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay recetas")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recetasFiltradas) { receta ->
                        RecetaCard(receta = receta) {
                            navController.navigate("recipe_detail/${receta.id}")
                        }
                    }
                }
            }
        }
    }
}
