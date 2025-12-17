// Test: FavoritesScreenTest.kt
package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.receta_2.data.model.*
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAVORITES SCREEN TESTABLE - USANDO TU MODELO REAL Receta
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenTestable(
    recipes: List<Receta>,  // Usando Receta, no Recipe
    isLoggedIn: Boolean,
    onToggleFavorite: (Int) -> Unit = {},  // Int, no String
    onDetailClick: (Int) -> Unit = {},     // Int, no String
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("btnVolver")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                modifier = Modifier.testTag("topBar")
            )
        }
    ) { paddingValues ->

        if (recipes.isEmpty()) {
            Text(
                "Aún no has guardado recetas favoritas.",
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .testTag("mensajeVacio")
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .testTag("listaFavoritos"),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(recipes, key = { it.id ?: 0 }) { recipe ->

                    // Tarjeta simplificada usando tu modelo Receta
                    Card(
                        modifier = Modifier
                            .testTag("recipe_${recipe.id}")
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = recipe.titulo,  // Usando titulo, no name
                            modifier = Modifier
                                .padding(16.dp)
                                .testTag("recipeName_${recipe.id}")
                        )

                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            TextButton(
                                modifier = Modifier.testTag("toggleFav_${recipe.id}"),
                                onClick = { onToggleFavorite(recipe.id ?: 0) }
                            ) {
                                Text("Quitar Favorito")
                            }

                            Spacer(Modifier.width(8.dp))

                            TextButton(
                                modifier = Modifier.testTag("details_${recipe.id}"),
                                onClick = { onDetailClick(recipe.id ?: 0) }
                            ) {
                                Text("Detalles")
                            }
                        }
                    }

                    Spacer(Modifier.padding(8.dp))
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — FavoritesScreenTest
   ------------------------------------------------------------------------- */

class FavoritesScreenTest {

    @get:Rule
    val rule = createComposeRule()

    // Crear recetas usando TU modelo real Receta
    private val receta1 = Receta(
        id = 1,
        titulo = "Pizza Casera",
        descripcion = "Deliciosa pizza casera con ingredientes frescos",
        ingredientes = "Harina\nTomate\nQueso\nJamón",
        instrucciones = "1. Preparar la masa\n2. Agregar ingredientes\n3. Hornear",
        usuario = IdWrapper(1),
        categoria = IdWrapper(1),
        subcategoria = IdWrapper(1)
    )

    private val receta2 = Receta(
        id = 2,
        titulo = "Brownie de Chocolate",
        descripcion = "Brownie húmedo y delicioso",
        ingredientes = "Chocolate\nHarina\nHuevos\nAzúcar",
        instrucciones = "1. Derretir chocolate\n2. Mezclar ingredientes\n3. Hornear",
        usuario = IdWrapper(1),
        categoria = IdWrapper(2),
        subcategoria = IdWrapper(3)
    )

    private var toggleCalledWith: Int? = null
    private var detailCalledWith: Int? = null
    private var backClickCount = 0

    @Test
    fun favoritesScreen_muestraTopBar() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = emptyList(),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithText("Mis Favoritos").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_botonVolverFunciona() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = emptyList(),
                isLoggedIn = true,
                onBackClick = { backClickCount++ }
            )
        }

        rule.onNodeWithTag("btnVolver").performClick()
        assert(backClickCount == 1)
    }

    @Test
    fun favoritesScreen_muestraMensajeCuandoVacio() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = emptyList(),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("mensajeVacio")
            .assertIsDisplayed()
            .assertTextContains("Aún no has guardado recetas favoritas.")
    }

    @Test
    fun favoritesScreen_muestraListaCuandoHayRecetas() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(receta1, receta2),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("listaFavoritos").assertIsDisplayed()
        rule.onNodeWithTag("recipe_1").assertIsDisplayed()
        rule.onNodeWithTag("recipe_2").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_muestraTituloRecetas() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(receta1, receta2),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("recipeName_1").assertTextContains("Pizza Casera")
        rule.onNodeWithTag("recipeName_2").assertTextContains("Brownie de Chocolate")
    }

    @Test
    fun favoritesScreen_toggleFavoriteFunciona() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(receta1),
                isLoggedIn = true,
                onToggleFavorite = { toggleCalledWith = it }
            )
        }

        rule.onNodeWithTag("toggleFav_1").performClick()
        assert(toggleCalledWith == 1)
    }

    @Test
    fun favoritesScreen_detailClickFunciona() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(receta1),
                isLoggedIn = true,
                onDetailClick = { detailCalledWith = it }
            )
        }

        rule.onNodeWithTag("details_1").performClick()
        assert(detailCalledWith == 1)
    }

    @Test
    fun favoritesScreen_muestraBotonesCorrectos() {
        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(receta1),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("toggleFav_1").assertTextContains("Quitar Favorito")
        rule.onNodeWithTag("details_1").assertTextContains("Detalles")
    }
}