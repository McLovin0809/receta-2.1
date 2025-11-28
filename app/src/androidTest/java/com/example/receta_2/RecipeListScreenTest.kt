package com.example.receta_2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAKE VIEWMODELS
   ------------------------------------------------------------------------- */

class FakeRecipeViewModel(
    initialRecipes: List<Recipe>
) {
    private val _recipes = MutableStateFlow(initialRecipes)
    val recipes: StateFlow<List<Recipe>> = _recipes

    fun setRecipes(list: List<Recipe>) {
        _recipes.value = list
    }
}

class FakeFavoritesViewModel(
    initialFavorites: List<String> = emptyList()
) {
    private val _favoriteIds = MutableStateFlow(initialFavorites)
    val favoriteRecipeIds: StateFlow<List<String>> = _favoriteIds

    var toggledId: String? = null

    fun toggleFavorite(id: String) {
        toggledId = id
    }
}

/* -------------------------------------------------------------------------
   TESTABLE VERSION OF RECIPE LIST SCREEN
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreenTestable(
    categoryId: String?,
    categoryName: String?,
    recipeVM: FakeRecipeViewModel,
    favoritesVM: FakeFavoritesViewModel,
    isLoggedIn: Boolean,
    onDetailsClick: (String) -> Unit = {}
) {
    val recipes by recipeVM.recipes.collectAsState()
    val favoriteIds by favoritesVM.favoriteRecipeIds.collectAsState()

    val toShow = recipes.filter { it.categoryIds.contains(categoryId) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = { Text(categoryName ?: "Recetas") },
                navigationIcon = {
                    IconButton(onClick = {}, modifier = Modifier.testTag("btnBack")) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                }
            )
        }
    ) { padding ->

        if (toShow.isEmpty()) {
            Text(
                "Aún no hay recetas en esta categoría.",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .testTag("mensajeVacio")
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .testTag("listaRecetas"),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(toShow, key = { it.id }) { recipe ->

                    // Fake RecipeItemCard
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .testTag("recipe_${recipe.id}")
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(
                                recipe.name,
                                modifier = Modifier.testTag("recipeName_${recipe.id}")
                            )

                            val isFav = favoriteIds.contains(recipe.id)
                            Text(
                                if (isFav) "FAVORITO" else "NO_FAVORITO",
                                modifier = Modifier.testTag("isFav_${recipe.id}")
                            )

                            TextButton(
                                onClick = { favoritesVM.toggleFavorite(recipe.id) },
                                modifier = Modifier.testTag("toggleFav_${recipe.id}")
                            ) {
                                Text("Fav")
                            }

                            TextButton(
                                onClick = { onDetailsClick(recipe.id) },
                                modifier = Modifier.testTag("details_${recipe.id}")
                            ) {
                                Text("Detalles")
                            }

                            Text("Logged: $isLoggedIn", modifier = Modifier.testTag("logged_${recipe.id}"))
                        }
                    }

                    Spacer(Modifier.padding(8.dp))
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — RecipeListScreenTest
   ------------------------------------------------------------------------- */

class RecipeListScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val recipe1 = Recipe(
        id = "1",
        name = "Torta de Chocolate",
        description = "",
        image = "",
        ingredients = listOf(),
        steps = listOf(),
        categoryIds = listOf("postres")
    )

    private val recipe2 = Recipe(
        id = "2",
        name = "Lasagna",
        description = "",
        image = "",
        ingredients = listOf(),
        steps = listOf(),
        categoryIds = listOf("pastas")
    )

    private lateinit var recipeVM: FakeRecipeViewModel
    private lateinit var favVM: FakeFavoritesViewModel
    private var detailsCalledId: String? = null

    private fun setContent(
        recipes: List<Recipe>,
        favorites: List<String> = emptyList(),
        isLoggedIn: Boolean = true,
        categoryId: String = "postres",
        categoryName: String = "Postres"
    ) {
        recipeVM = FakeRecipeViewModel(recipes)
        favVM = FakeFavoritesViewModel(favorites)
        detailsCalledId = null

        rule.setContent {
            RecipeListScreenTestable(
                categoryId = categoryId,
                categoryName = categoryName,
                recipeVM = recipeVM,
                favoritesVM = favVM,
                isLoggedIn = isLoggedIn,
                onDetailsClick = { detailsCalledId = it }
            )
        }
    }

    /* --- TOP BAR --- */

    @Test
    fun recipeList_muestraTopBarConTitulo() {
        setContent(recipes = listOf(recipe1))

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithText("Postres").assertIsDisplayed()
    }

    /* --- LISTA VACÍA --- */

    @Test
    fun recipeList_muestraMensajeSiVacio() {
        setContent(recipes = listOf(), categoryId = "x")

        rule.onNodeWithTag("mensajeVacio").assertIsDisplayed()
    }

    /* --- LISTA DE RECETAS --- */

    @Test
    fun recipeList_muestraListaRecetas() {
        setContent(recipes = listOf(recipe1, recipe2), categoryId = "postres")

        rule.onNodeWithTag("listaRecetas").assertIsDisplayed()
        rule.onNodeWithTag("recipe_1").assertIsDisplayed()
        rule.onNodeWithTag("recipe_2").assertDoesNotExist()
    }

    @Test
    fun recipeList_muestraNombreRecetas() {
        setContent(recipes = listOf(recipe1))

        rule.onNodeWithTag("recipeName_1")
            .assertIsDisplayed()
            .assertTextContains("Torta de Chocolate")
    }

    /* --- FAVORITOS --- */

    @Test
    fun recipeList_indicaSiEsFavorito() {
        setContent(recipes = listOf(recipe1), favorites = listOf("1"))

        rule.onNodeWithTag("isFav_1").assertTextContains("FAVORITO")
    }

    @Test
    fun recipeList_indicaSiNoEsFavorito() {
        setContent(recipes = listOf(recipe1), favorites = emptyList())

        rule.onNodeWithTag("isFav_1").assertTextContains("NO_FAVORITO")
    }

    @Test
    fun recipeList_toggleFavorite_funciona() {
        setContent(recipes = listOf(recipe1))

        rule.onNodeWithTag("toggleFav_1").performClick()

        assert(favVM.toggledId == "1")
    }

    /* --- DETALLES --- */

    @Test
    fun recipeList_detailsClick_funciona() {
        setContent(recipes = listOf(recipe1))

        rule.onNodeWithTag("details_1").performClick()

        assert(detailsCalledId == "1")
    }

    /* --- LOGGED IN PROPAGATION --- */

    @Test
    fun recipeList_muestraEstadoLoggedCorrecto() {
        setContent(recipes = listOf(recipe1), isLoggedIn = true)

        rule.onNodeWithTag("logged_1").assertTextContains("Logged: true")
    }

    @Test
    fun recipeList_muestraEstadoLoggedFalse() {
        setContent(recipes = listOf(recipe1), isLoggedIn = false)

        rule.onNodeWithTag("logged_1").assertTextContains("Logged: false")
    }
}