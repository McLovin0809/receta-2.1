package com.example.receta_2

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAVORITES SCREEN TESTABLE
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenTestable(
    recipes: List<Recipe>,
    isLoggedIn: Boolean,
    onToggleFavorite: (String) -> Unit = {},
    onDetailClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Favoritos") })
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
                items(recipes, key = { it.id }) { recipe ->

                    // Pequeña tarjeta simplificada para testear
                    Card(
                        modifier = Modifier
                            .testTag("recipe_${recipe.id}")
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = recipe.name,
                            modifier = Modifier.testTag("recipeName_${recipe.id}")
                        )

                        TextButton(
                            modifier = Modifier.testTag("toggleFav_${recipe.id}"),
                            onClick = { onToggleFavorite(recipe.id) }
                        ) {
                            Text("Fav")
                        }

                        TextButton(
                            modifier = Modifier.testTag("details_${recipe.id}"),
                            onClick = { onDetailClick(recipe.id) }
                        ) {
                            Text("Detalles")
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

    private val recipe1 = Recipe(
        id = "1",
        name = "Pizza",
        description = "",
        image = "",
        ingredients = emptyList(),
        steps = emptyList(),
        categoryIds = emptyList()
    )

    private val recipe2 = Recipe(
        id = "2",
        name = "Brownie",
        description = "",
        image = "",
        ingredients = emptyList(),
        steps = emptyList(),
        categoryIds = emptyList()
    )

    private var toggleCalledWith: String? = null
    private var detailCalledWith: String? = null

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
    fun favoritesScreen_muestraLista() {

        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(recipe1, recipe2),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("listaFavoritos").assertIsDisplayed()
        rule.onNodeWithTag("recipe_1").assertIsDisplayed()
        rule.onNodeWithTag("recipe_2").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_muestraNombreRecetas() {

        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(recipe1, recipe2),
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("recipeName_1").assertTextContains("Pizza")
        rule.onNodeWithTag("recipeName_2").assertTextContains("Brownie")
    }

    @Test
    fun favoritesScreen_toggleFavorite_funciona() {

        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(recipe1),
                isLoggedIn = true,
                onToggleFavorite = { toggleCalledWith = it }
            )
        }

        rule.onNodeWithTag("toggleFav_1").performClick()

        assert(toggleCalledWith == "1")
    }

    @Test
    fun favoritesScreen_onDetailClick_funciona() {

        rule.setContent {
            FavoritesScreenTestable(
                recipes = listOf(recipe1),
                isLoggedIn = true,
                onDetailClick = { detailCalledWith = it }
            )
        }

        rule.onNodeWithTag("details_1").performClick()

        assert(detailCalledWith == "1")
    }
}