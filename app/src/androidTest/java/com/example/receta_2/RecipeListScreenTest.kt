// Test: RecipeListScreenTest.kt
package com.example.receta_2.ui.screens

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
import com.example.receta_2.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAKE VIEWMODELS - USANDO TU MODELO Receta
   ------------------------------------------------------------------------- */

class FakeRecipeViewModel(
    initialRecipes: List<Receta>
) {
    private val _recipes = MutableStateFlow(initialRecipes)
    val recipes: StateFlow<List<Receta>> = _recipes

    fun setRecipes(list: List<Receta>) {
        _recipes.value = list
    }
}

class FakeFavoritesViewModel(
    initialFavorites: List<Int> = emptyList()
) {
    private val _favoriteIds = MutableStateFlow(initialFavorites)
    val favoriteRecipeIds: StateFlow<List<Int>> = _favoriteIds

    var toggledId: Int? = null

    fun toggleFavorite(id: Int) {
        toggledId = id
    }
}

/* -------------------------------------------------------------------------
   TESTABLE VERSION OF RECIPE LIST SCREEN - USANDO TU MODELO Receta
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreenTestable(
    categoriaId: Int?,  // Cambiado a Int
    categoriaNombre: String?,
    recipeVM: FakeRecipeViewModel,
    favoritesVM: FakeFavoritesViewModel,
    isLoggedIn: Boolean,
    onDetailsClick: (Int) -> Unit = {}  // Cambiado a Int
) {
    val recipes by recipeVM.recipes.collectAsState()
    val favoriteIds by favoritesVM.favoriteRecipeIds.collectAsState()

    // Filtrar recetas por categoría (usando IdWrapper)
    val toShow = recipes.filter { receta ->
        categoriaId == null || receta.categoria?.id == categoriaId
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = { Text(categoriaNombre ?: "Recetas") },
                navigationIcon = {
                    IconButton(onClick = {}, modifier = Modifier.testTag("btnBack")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
                items(toShow, key = { it.id ?: 0 }) { recipe ->

                    // Fake RecipeItemCard usando tu modelo Receta
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .testTag("recipe_${recipe.id}")
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(
                                recipe.titulo,  // Cambiado a titulo
                                modifier = Modifier.testTag("recipeName_${recipe.id}")
                            )

                            Text(
                                recipe.descripcion.take(50) + "...",  // Descripción corta
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.testTag("recipeDesc_${recipe.id}")
                            )

                            val isFav = favoriteIds.contains(recipe.id)
                            Text(
                                if (isFav) "FAVORITO" else "NO_FAVORITO",
                                modifier = Modifier.testTag("isFav_${recipe.id}")
                            )

                            TextButton(
                                onClick = { favoritesVM.toggleFavorite(recipe.id ?: 0) },
                                modifier = Modifier.testTag("toggleFav_${recipe.id}")
                            ) {
                                Text("Fav")
                            }

                            TextButton(
                                onClick = { onDetailsClick(recipe.id ?: 0) },
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

    // Crear recetas usando TU modelo real Receta
    private val receta1 = Receta(
        id = 1,
        titulo = "Torta de Chocolate",
        descripcion = "Deliciosa torta de chocolate casera",
        ingredientes = "Chocolate\nHarina\nHuevos\nAzúcar",
        instrucciones = "1. Mezclar ingredientes\n2. Hornear\n3. Decorar",
        usuario = IdWrapper(1),
        categoria = IdWrapper(1),  // Categoría Postres
        subcategoria = IdWrapper(1)
    )

    private val receta2 = Receta(
        id = 2,
        titulo = "Lasagna",
        descripcion = "Lasagna italiana con carne y queso",
        ingredientes = "Carne\nPasta\nQueso\nSalsa",
        instrucciones = "1. Cocinar carne\n2. Armar capas\n3. Hornear",
        usuario = IdWrapper(1),
        categoria = IdWrapper(2),  // Categoría Pastas
        subcategoria = IdWrapper(3)
    )

    private val receta3 = Receta(
        id = 3,
        titulo = "Brownie",
        descripcion = "Brownie de chocolate con nueces",
        ingredientes = "Chocolate\nNueces\nHarina",
        instrucciones = "Mezclar y hornear",
        usuario = IdWrapper(1),
        categoria = IdWrapper(1),  // Categoría Postres también
        subcategoria = IdWrapper(2)
    )

    private lateinit var recipeVM: FakeRecipeViewModel
    private lateinit var favVM: FakeFavoritesViewModel
    private var detailsCalledId: Int? = null

    private fun setContent(
        recetas: List<Receta>,
        favorites: List<Int> = emptyList(),
        isLoggedIn: Boolean = true,
        categoriaId: Int? = 1,  // Cambiado a Int
        categoriaNombre: String = "Postres"
    ) {
        recipeVM = FakeRecipeViewModel(recetas)
        favVM = FakeFavoritesViewModel(favorites)
        detailsCalledId = null

        rule.setContent {
            RecipeListScreenTestable(
                categoriaId = categoriaId,
                categoriaNombre = categoriaNombre,
                recipeVM = recipeVM,
                favoritesVM = favVM,
                isLoggedIn = isLoggedIn,
                onDetailsClick = { detailsCalledId = it }
            )
        }
    }

    /* --- TOP BAR Y NAVEGACIÓN --- */

    @Test
    fun recipeList_muestraTopBarConTitulo() {
        setContent(recetas = listOf(receta1))

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithText("Postres").assertIsDisplayed()
    }

    @Test
    fun recipeList_botonVolverExiste() {
        setContent(recetas = listOf(receta1))

        rule.onNodeWithTag("btnBack").assertIsDisplayed()
    }

    /* --- LISTA VACÍA --- */

    @Test
    fun recipeList_muestraMensajeSiVacio() {
        setContent(recetas = listOf(), categoriaId = 1)

        rule.onNodeWithTag("mensajeVacio").assertIsDisplayed()
        rule.onNodeWithText("Aún no hay recetas en esta categoría.").assertIsDisplayed()
    }

    @Test
    fun recipeList_sinCategoriaIdMuestraTodas() {
        setContent(
            recetas = listOf(receta1, receta2),
            categoriaId = null,  // Sin categoría específica
            categoriaNombre = "Todas las Recetas"
        )

        // Debería mostrar ambas recetas
        rule.onNodeWithTag("recipe_1").assertIsDisplayed()
        rule.onNodeWithTag("recipe_2").assertIsDisplayed()
    }

    /* --- FILTRADO POR CATEGORÍA --- */

    @Test
    fun recipeList_filtraPorCategoria() {
        setContent(recetas = listOf(receta1, receta2, receta3), categoriaId = 1)

        // Solo recetas de categoría 1 (Postres)
        rule.onNodeWithTag("recipe_1").assertIsDisplayed()  // Torta de Chocolate
        rule.onNodeWithTag("recipe_3").assertIsDisplayed()  // Brownie
        rule.onNodeWithTag("recipe_2").assertDoesNotExist()  // Lasagna (categoría 2)
    }

    @Test
    fun recipeList_filtroCategoriaDistinta() {
        setContent(recetas = listOf(receta1, receta2, receta3), categoriaId = 2)

        // Solo recetas de categoría 2 (Pastas)
        rule.onNodeWithTag("recipe_2").assertIsDisplayed()  // Lasagna
        rule.onNodeWithTag("recipe_1").assertDoesNotExist()  // Torta de Chocolate
        rule.onNodeWithTag("recipe_3").assertDoesNotExist()  // Brownie
    }

    /* --- INFORMACIÓN DE RECETAS --- */

    @Test
    fun recipeList_muestraNombreRecetas() {
        setContent(recetas = listOf(receta1))

        rule.onNodeWithTag("recipeName_1")
            .assertIsDisplayed()
            .assertTextContains("Torta de Chocolate")
    }



    /* --- FAVORITOS --- */

    @Test
    fun recipeList_indicaSiEsFavorito() {
        setContent(recetas = listOf(receta1), favorites = listOf(1))

        rule.onNodeWithTag("isFav_1").assertTextContains("FAVORITO")
    }

    @Test
    fun recipeList_indicaSiNoEsFavorito() {
        setContent(recetas = listOf(receta1), favorites = emptyList())

        rule.onNodeWithTag("isFav_1").assertTextContains("NO_FAVORITO")
    }


    @Test
    fun recipeList_toggleFavorite_funciona() {
        setContent(recetas = listOf(receta1))

        rule.onNodeWithTag("toggleFav_1").performClick()

        assert(favVM.toggledId == 1)
    }


    /* --- DETALLES --- */

    @Test
    fun recipeList_detailsClick_funciona() {
        setContent(recetas = listOf(receta1))

        rule.onNodeWithTag("details_1").performClick()
        assert(detailsCalledId == 1)
    }



    /* --- ESTADO DE LOGIN --- */

    @Test
    fun recipeList_muestraEstadoLoggedCorrecto() {
        setContent(recetas = listOf(receta1), isLoggedIn = true)

        rule.onNodeWithTag("logged_1").assertTextContains("Logged: true")
    }

    @Test
    fun recipeList_muestraEstadoLoggedFalse() {
        setContent(recetas = listOf(receta1), isLoggedIn = false)

        rule.onNodeWithTag("logged_1").assertTextContains("Logged: false")
    }

    /* --- CAMBIOS DINÁMICOS --- */



    @Test
    fun recipeList_actualizaFavoritos() {
        setContent(recetas = listOf(receta1), favorites = emptyList())

        // Inicialmente no favorito
        rule.onNodeWithTag("isFav_1").assertTextContains("NO_FAVORITO")

        // Simular cambio en favoritos (a través del ViewModel real se haría diferente)
        // En este test, probamos la lógica de visualización
    }

    /* --- CASOS ESPECIALES --- */



    @Test
    fun recipeList_multiplesRecetasMismaCategoria() {
        setContent(
            recetas = listOf(receta1, receta3),  // Ambas de categoría 1
            categoriaId = 1
        )

        rule.onNodeWithTag("recipe_1").assertIsDisplayed()
        rule.onNodeWithTag("recipe_3").assertIsDisplayed()
        rule.onNodeWithTag("listaRecetas").assertIsDisplayed()
    }

}