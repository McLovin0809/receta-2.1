package com.example.receta_2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.example.receta_2.data.model.SearchCategory
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   MODELOS FAKE PARA TEST
   ------------------------------------------------------------------------- */

private val fakeCategories = listOf(
    SearchCategory(
        id = "1",
        name = "Postres",
        image = 0,
        group = CategoryGroup.DIETARY_NEED,
        recipeCount = 5
    ),
    SearchCategory(
        id = "2",
        name = "Pastas",
        image = 0,
        group = CategoryGroup.SPECIAL_OCCASION  ,
        recipeCount = 10
    )
)

/* -------------------------------------------------------------------------
   HOME SCREEN TESTABLE — versión simplificada para pruebas
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTestable(
    isLoggedIn: Boolean,
    onProfileClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onFabClick: () -> Unit = {},
    categories: List<SearchCategory> = fakeCategories
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = {
                    Text(
                        "RecetApp",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    if (isLoggedIn) {
                        IconButton(onClick = onFavoritesClick, modifier = Modifier.testTag("btnFavoritos")) {
                            Icon(Icons.Default.Favorite, contentDescription = "")
                        }
                        IconButton(onClick = onProfileClick, modifier = Modifier.testTag("btnPerfil")) {
                            Icon(Icons.Default.Person, contentDescription = "")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (isLoggedIn) {
                FloatingActionButton(
                    modifier = Modifier.testTag("fabAddRecipe"),
                    onClick = onFabClick
                ) {
                    Icon(Icons.Default.Add, contentDescription = "")
                }
            }
        }
    ) { paddingValues ->

        Column(Modifier.padding(paddingValues)) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("inputBuscar"),
                placeholder = { Text("Buscar...") }
            )

            CategoryTabsTestable(
                categories = categories,
                searchQuery = searchQuery,
                onCategoryClick = {}
            )
        }
    }
}

/* -------------------------------------------------------------------------
   CATEGORY TABS TESTABLE — versión simplificada
   ------------------------------------------------------------------------- */

@Composable
fun CategoryTabsTestable(
    categories: List<SearchCategory>,
    searchQuery: String,
    onCategoryClick: (SearchCategory) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val groups = CategoryGroup.values()

    Column {

        if (searchQuery.isBlank()) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.testTag("tabRow")
            ) {
                groups.forEachIndexed { index, group ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier.testTag("tab_$index"),
                        text = { Text(group.name) }
                    )
                }
            }
        }

        val filtered = if (searchQuery.isBlank()) {
            categories.filter { it.group == groups[selectedTab] }
        } else {
            categories.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        if (filtered.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("mensajeVacio"),
                text = "No se encontraron categorías"
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("gridCategorias")
            ) {
                items(filtered, key = { it.id }) { cat ->
                    CategoryCardTestable(
                        cat,
                        Modifier.testTag("cat_${cat.id}"),
                        onCategoryClick
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------
   CATEGORY CARD TESTABLE
   ------------------------------------------------------------------------- */

@Composable
fun CategoryCardTestable(
    category: SearchCategory,
    modifier: Modifier,
    onClick: (SearchCategory) -> Unit
) {
    Card(
        modifier = modifier,
        onClick = { onClick(category) }
    ) {
        Text(
            category.name,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/* -------------------------------------------------------------------------
   TESTS — HomeScreenTest
   ------------------------------------------------------------------------- */

class HomeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private var fabCalled = false
    private var profileCalled = false
    private var favoritesCalled = false

    /* --- TOP BAR --- */

    @Test
    fun homeScreen_muestraTopBar() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("topBar").assertIsDisplayed()
    }

    /* --- BOTONES PERFIL Y FAVORITOS --- */

    @Test
    fun homeScreen_muestraBotonesSiLoggedIn() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("btnPerfil").assertIsDisplayed()
        rule.onNodeWithTag("btnFavoritos").assertIsDisplayed()
    }

    @Test
    fun homeScreen_noMuestraBotonesSiNoLoggedIn() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("btnPerfil").assertDoesNotExist()
        rule.onNodeWithTag("btnFavoritos").assertDoesNotExist()
    }

    /* --- FAB AÑADIR RECETA --- */

    @Test
    fun homeScreen_muestraFabSiLoggedIn() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = true
            )
        }

        rule.onNodeWithTag("fabAddRecipe").assertIsDisplayed()
    }

    @Test
    fun homeScreen_noMuestraFabSiNoLoggedIn() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("fabAddRecipe").assertDoesNotExist()
    }

    /* --- BÚSQUEDA --- */

    @Test
    fun homeScreen_busquedaFiltraCategorias() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("inputBuscar").performTextInput("Postres")

        rule.onNodeWithTag("cat_1").assertIsDisplayed()
        rule.onNodeWithTag("cat_2").assertDoesNotExist()
    }

    @Test
    fun homeScreen_busquedaSinResultados() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("inputBuscar").performTextInput("ZZZ")

        rule.onNodeWithTag("mensajeVacio").assertIsDisplayed()
    }

    /* --- TABS --- */

    @Test
    fun homeScreen_tabsFuncionan() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("tab_0").assertIsDisplayed()
        rule.onNodeWithTag("tab_1").assertIsDisplayed()

        rule.onNodeWithTag("tab_1").performClick()
    }

    /* --- GRID DE CATEGORÍAS --- */

    @Test
    fun homeScreen_muestraGrid() {

        rule.setContent {
            HomeScreenTestable(
                isLoggedIn = false
            )
        }

        rule.onNodeWithTag("gridCategorias").assertIsDisplayed()
    }
}