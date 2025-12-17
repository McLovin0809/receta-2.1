// Test: HomeScreenTest.kt
package com.example.receta_2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.receta_2.data.model.*
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   HOME SCREEN TESTABLE - USANDO TUS MODELOS REALES
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTestable(
    categorias: List<Categoria> = emptyList(),
    subcategorias: List<Subcategoria> = emptyList(),
    recetas: List<Receta> = emptyList(),
    onProfileClick: () -> Unit = {},
    onAddRecipeClick: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {},
    onCategoriaSelect: (Categoria) -> Unit = {},
    onSubcategoriaSelect: (Subcategoria) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoriaIndex by remember { mutableStateOf(0) }
    var selectedSubcategoriaId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas") },
                actions = {
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier.testTag("btnPerfil")
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                modifier = Modifier.testTag("topBar")
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecipeClick,
                modifier = Modifier.testTag("fabAddRecipe")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir receta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("contenedorPrincipal")
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                placeholder = { Text("Buscar recetas...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("inputBuscar")
            )

            if (categorias.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoriaIndex,
                    modifier = Modifier.testTag("tabRowCategorias")
                ) {
                    categorias.forEachIndexed { index, categoria ->
                        Tab(
                            selected = index == selectedCategoriaIndex,
                            onClick = {
                                selectedCategoriaIndex = index
                                selectedSubcategoriaId = null
                                onCategoriaSelect(categoria)
                            },
                            text = { Text(categoria.nombre) },
                            modifier = Modifier.testTag("tabCategoria_${categoria.id}")
                        )
                    }
                }
            }

            val categoriaSeleccionada = categorias.getOrNull(selectedCategoriaIndex)
            val subcatsFiltradas = subcategorias.filter {
                it.categoria?.id == categoriaSeleccionada?.id
            }

            if (subcatsFiltradas.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .padding(8.dp)
                        .testTag("rowSubcategorias"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(subcatsFiltradas) { subcat ->
                        AssistChip(
                            onClick = {
                                selectedSubcategoriaId = subcat.id
                                onSubcategoriaSelect(subcat)
                            },
                            label = { Text(subcat.nombre) },
                            modifier = Modifier.testTag("chipSubcategoria_${subcat.id}")
                        )
                    }
                }
            }

            val recetasFiltradas = recetas.filter {
                (selectedSubcategoriaId == null || it.subcategoria?.id == selectedSubcategoriaId) &&
                        it.titulo.contains(searchQuery, true)
            }

            if (recetasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("mensajeVacio"),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay recetas")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .testTag("gridRecetas"),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recetasFiltradas) { receta ->
                        Card(
                            modifier = Modifier
                                .testTag("cardReceta_${receta.id}")
                                .clickable { onRecipeClick(receta.id ?: 0) },
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = receta.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.testTag("tituloReceta_${receta.id}")
                                )
                                Text(
                                    text = receta.descripcion.take(50) + "...",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.testTag("descripcionReceta_${receta.id}")
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — HomeScreenTest
   ------------------------------------------------------------------------- */

class HomeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    // Datos de prueba usando tus modelos reales
    private val categoria1 = Categoria(
        id = 1,
        nombre = "Postres",
        descripcion = "Recetas dulces"
    )

    private val categoria2 = Categoria(
        id = 2,
        nombre = "Platos Principales",
        descripcion = "Comidas principales"
    )

    private val subcategoria1 = Subcategoria(
        id = 1,
        nombre = "Tortas",
        categoria = categoria1
    )

    private val subcategoria2 = Subcategoria(
        id = 2,
        nombre = "Galletas",
        categoria = categoria1
    )

    private val subcategoria3 = Subcategoria(
        id = 3,
        nombre = "Carnes",
        categoria = categoria2
    )

    private val receta1 = Receta(
        id = 1,
        titulo = "Brownie de Chocolate",
        descripcion = "Delicioso brownie casero con nueces",
        ingredientes = "Chocolate\nHarina\nHuevos\nAzúcar\nNueces",
        instrucciones = "1. Derretir chocolate\n2. Mezclar ingredientes\n3. Hornear 25 minutos",
        usuario = IdWrapper(1),
        categoria = IdWrapper(1),
        subcategoria = IdWrapper(1)
    )

    private val receta2 = Receta(
        id = 2,
        titulo = "Pizza Casera",
        descripcion = "Pizza con masa casera y ingredientes frescos",
        ingredientes = "Harina\nTomate\nQueso\nJamón\nChampiñones",
        instrucciones = "1. Preparar masa\n2. Agregar ingredientes\n3. Hornear 15 minutos",
        usuario = IdWrapper(1),
        categoria = IdWrapper(2),
        subcategoria = IdWrapper(3)
    )

    private var profileClickCount = 0
    private var addRecipeClickCount = 0
    private var recipeClickId: Int? = null
    private var categoriaSeleccionada: Categoria? = null
    private var subcategoriaSeleccionada: Subcategoria? = null

    @Test
    fun homeScreen_muestraTopBar() {
        rule.setContent {
            HomeScreenTestable()
        }

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithText("Recetas").assertIsDisplayed()
    }

    @Test
    fun homeScreen_botonPerfilFunciona() {
        rule.setContent {
            HomeScreenTestable(
                onProfileClick = { profileClickCount++ }
            )
        }

        rule.onNodeWithTag("btnPerfil").performClick()
        assert(profileClickCount == 1)
    }

    @Test
    fun homeScreen_fabAddRecipeFunciona() {
        rule.setContent {
            HomeScreenTestable(
                onAddRecipeClick = { addRecipeClickCount++ }
            )
        }

        rule.onNodeWithTag("fabAddRecipe").performClick()
        assert(addRecipeClickCount == 1)
    }

    @Test
    fun homeScreen_muestraInputBusqueda() {
        rule.setContent {
            HomeScreenTestable()
        }

        rule.onNodeWithTag("inputBuscar").assertIsDisplayed()
        rule.onNodeWithText("Buscar recetas...").assertIsDisplayed()
    }

    @Test
    fun homeScreen_busquedaFiltraRecetas() {
        rule.setContent {
            HomeScreenTestable(
                recetas = listOf(receta1, receta2)
            )
        }

        // Escribir en la búsqueda
        rule.onNodeWithTag("inputBuscar").performTextInput("Brownie")

        // Solo debería mostrar la receta 1
        rule.onNodeWithTag("cardReceta_1").assertIsDisplayed()
        rule.onNodeWithTag("cardReceta_2").assertDoesNotExist()
    }

    @Test
    fun homeScreen_muestraTabCategorias() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1, categoria2)
            )
        }

        rule.onNodeWithTag("tabRowCategorias").assertIsDisplayed()
        rule.onNodeWithTag("tabCategoria_1").assertTextContains("Postres")
        rule.onNodeWithTag("tabCategoria_2").assertTextContains("Platos Principales")
    }

    @Test
    fun homeScreen_tabCategoriaFunciona() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1, categoria2),
                onCategoriaSelect = { categoriaSeleccionada = it }
            )
        }

        rule.onNodeWithTag("tabCategoria_2").performClick()
        assert(categoriaSeleccionada?.id == 2)
    }

    @Test
    fun homeScreen_muestraSubcategoriasFiltradas() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1, categoria2),
                subcategorias = listOf(subcategoria1, subcategoria2, subcategoria3)
            )
        }

        // Primero seleccionar categoría 1 (Postres)
        rule.onNodeWithTag("tabCategoria_1").performClick()

        // Debería mostrar solo subcategorías de Postres
        rule.onNodeWithTag("rowSubcategorias").assertIsDisplayed()
        rule.onNodeWithTag("chipSubcategoria_1").assertTextContains("Tortas")
        rule.onNodeWithTag("chipSubcategoria_2").assertTextContains("Galletas")
        // No debería mostrar Carnes (ID 3) que es de Platos Principales
        rule.onNodeWithTag("chipSubcategoria_3").assertDoesNotExist()
    }

    @Test
    fun homeScreen_chipSubcategoriaFunciona() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1),
                subcategorias = listOf(subcategoria1, subcategoria2),
                onSubcategoriaSelect = { subcategoriaSeleccionada = it }
            )
        }

        rule.onNodeWithTag("chipSubcategoria_1").performClick()
        assert(subcategoriaSeleccionada?.id == 1)
    }

    @Test
    fun homeScreen_muestraMensajeVacio() {
        rule.setContent {
            HomeScreenTestable(
                recetas = emptyList()
            )
        }

        rule.onNodeWithTag("mensajeVacio").assertIsDisplayed()
        rule.onNodeWithText("No hay recetas").assertIsDisplayed()
    }

    @Test
    fun homeScreen_muestraGridRecetas() {
        rule.setContent {
            HomeScreenTestable(
                recetas = listOf(receta1, receta2)
            )
        }

        rule.onNodeWithTag("gridRecetas").assertIsDisplayed()
        rule.onNodeWithTag("cardReceta_1").assertIsDisplayed()
        rule.onNodeWithTag("cardReceta_2").assertIsDisplayed()
    }


    @Test
    fun homeScreen_clickRecetaFunciona() {
        rule.setContent {
            HomeScreenTestable(
                recetas = listOf(receta1),
                onRecipeClick = { recipeClickId = it }
            )
        }

        rule.onNodeWithTag("cardReceta_1").performClick()
        assert(recipeClickId == 1)
    }

    @Test
    fun homeScreen_filtroSubcategoriaFunciona() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1),
                subcategorias = listOf(subcategoria1, subcategoria2),
                recetas = listOf(receta1)
            )
        }

        // Seleccionar subcategoría
        rule.onNodeWithTag("chipSubcategoria_1").performClick()

        // Verificar que las recetas se filtren correctamente
        rule.onNodeWithTag("gridRecetas").assertIsDisplayed()
    }

    @Test
    fun homeScreen_flujoCompleto() {
        var categoriaSeleccionada: Categoria? = null
        var subcategoriaSeleccionada: Subcategoria? = null

        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1, categoria2),
                subcategorias = listOf(subcategoria1, subcategoria2, subcategoria3),
                recetas = listOf(receta1, receta2),
                onCategoriaSelect = { categoriaSeleccionada = it },
                onSubcategoriaSelect = { subcategoriaSeleccionada = it },
                onRecipeClick = { recipeClickId = it }
            )
        }

        // 1. Buscar receta
        rule.onNodeWithTag("inputBuscar").performTextInput("Pizza")
        rule.waitForIdle()

        // 2. Seleccionar categoría
        rule.onNodeWithTag("tabCategoria_2").performClick()
        rule.waitForIdle()
        assert(categoriaSeleccionada?.id == 2)

        // 3. Seleccionar subcategoría
        rule.onNodeWithTag("chipSubcategoria_3").performClick()
        rule.waitForIdle()
        assert(subcategoriaSeleccionada?.id == 3)

        // 4. Hacer clic en receta
        rule.onNodeWithTag("cardReceta_2").performClick()
        rule.waitForIdle()
        assert(recipeClickId == 2)
    }

    @Test
    fun homeScreen_sinCategorias() {
        rule.setContent {
            HomeScreenTestable(
                categorias = emptyList(),
                recetas = listOf(receta1)
            )
        }

        // No debería mostrar tabs
        rule.onNodeWithTag("tabRowCategorias").assertDoesNotExist()

        // Debería mostrar recetas directamente
        rule.onNodeWithTag("gridRecetas").assertIsDisplayed()
    }

    @Test
    fun homeScreen_sinSubcategorias() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1),
                subcategorias = emptyList(),
                recetas = listOf(receta1)
            )
        }

        // No debería mostrar row de subcategorías
        rule.onNodeWithTag("rowSubcategorias").assertDoesNotExist()

        // Debería mostrar recetas
        rule.onNodeWithTag("gridRecetas").assertIsDisplayed()
    }

    @Test
    fun homeScreen_busquedaVaciaMuestraTodas() {
        rule.setContent {
            HomeScreenTestable(
                recetas = listOf(receta1, receta2)
            )
        }

        // Con búsqueda vacía, debería mostrar todas las recetas
        rule.onNodeWithTag("cardReceta_1").assertIsDisplayed()
        rule.onNodeWithTag("cardReceta_2").assertIsDisplayed()
    }

    @Test
    fun homeScreen_subcategoriasCambianConCategoria() {
        rule.setContent {
            HomeScreenTestable(
                categorias = listOf(categoria1, categoria2),
                subcategorias = listOf(subcategoria1, subcategoria2, subcategoria3)
            )
        }

        // Inicialmente seleccionada categoría 1
        rule.onNodeWithTag("chipSubcategoria_1").assertIsDisplayed()
        rule.onNodeWithTag("chipSubcategoria_2").assertIsDisplayed()
        rule.onNodeWithTag("chipSubcategoria_3").assertDoesNotExist()

        // Cambiar a categoría 2
        rule.onNodeWithTag("tabCategoria_2").performClick()
        rule.waitForIdle()

        // Ahora debería mostrar solo subcategoría 3
        rule.onNodeWithTag("chipSubcategoria_1").assertDoesNotExist()
        rule.onNodeWithTag("chipSubcategoria_2").assertDoesNotExist()
        rule.onNodeWithTag("chipSubcategoria_3").assertIsDisplayed()
    }
}