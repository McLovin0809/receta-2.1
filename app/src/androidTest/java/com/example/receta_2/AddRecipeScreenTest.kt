// Test: AddRecipeScreenTestable.kt
package com.example.receta_2

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import coil.compose.AsyncImage
import com.example.receta_2.data.model.*
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   MODELOS FAKE PARA TEST - USANDO TUS MODELOS REALES
   ------------------------------------------------------------------------- */

private val fakeCategoria1 = Categoria(
    id = 1,
    nombre = "Postres",
    descripcion = "Recetas dulces"
)

private val fakeCategoria2 = Categoria(
    id = 2,
    nombre = "Platos Principales",
    descripcion = "Comidas principales"
)

private val fakeSubcategoria1 = Subcategoria(
    id = 1,
    nombre = "Tortas"
)

private val fakeSubcategoria2 = Subcategoria(
    id = 2,
    nombre = "Galletas"
)

private val fakeSubcategoria3 = Subcategoria(
    id = 3,
    nombre = "Carnes"
)

/* -------------------------------------------------------------------------
   ADD RECIPE SCREEN TESTABLE
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreenTestable(
    imageUri: Uri? = null,
    categorias: List<Categoria> = listOf(fakeCategoria1, fakeCategoria2),
    subcategorias: List<Subcategoria> = listOf(fakeSubcategoria1, fakeSubcategoria2, fakeSubcategoria3),
    onImageClick: () -> Unit = {},
    onCategoriaSelect: (Categoria) -> Unit = {},
    onSubcategoriaSelect: (Subcategoria) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ingredientes by remember { mutableStateOf("") }
    var instrucciones by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }
    var subcategoriaSeleccionada by remember { mutableStateOf<Subcategoria?>(null) }

    var expandCategoria by remember { mutableStateOf(false) }
    var expandSubcategoria by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = { Text("Agregar Receta") },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("btnVolver")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .testTag("contenedorPrincipal")
        ) {

            // Selector de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .clickable(onClick = onImageClick)
                    .testTag("selectorImagen"),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.testTag("vistaSinImagen")
                    ) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = "Icono de cámara",
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("iconoCamara")
                        )
                        Text("Toca para seleccionar una imagen")
                    }
                } else {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Imagen de la receta",
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("imagenSeleccionada"),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Campos de texto
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inputTitulo")
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inputDescripcion")
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = ingredientes,
                onValueChange = { ingredientes = it },
                label = { Text("Ingredientes (uno por línea)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inputIngredientes")
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = instrucciones,
                onValueChange = { instrucciones = it },
                label = { Text("Instrucciones (una por línea)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inputInstrucciones")
            )

            Spacer(Modifier.height(16.dp))

            // Selector de categoría
            ExposedDropdownMenuBox(
                expanded = expandCategoria,
                onExpandedChange = { expandCategoria = !expandCategoria },
                modifier = Modifier.testTag("dropdownCategoria")
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("inputCategoria")
                )
                ExposedDropdownMenu(
                    expanded = expandCategoria,
                    onDismissRequest = { expandCategoria = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                categoriaSeleccionada = categoria
                                subcategoriaSeleccionada = null
                                onCategoriaSelect(categoria)
                                expandCategoria = false
                            },
                            modifier = Modifier.testTag("itemCategoria_${categoria.id}")
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Selector de subcategoría
            ExposedDropdownMenuBox(
                expanded = expandSubcategoria,
                onExpandedChange = { expandSubcategoria = !expandSubcategoria },
                modifier = Modifier.testTag("dropdownSubcategoria")
            ) {
                OutlinedTextField(
                    value = subcategoriaSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Subcategoría") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("inputSubcategoria")
                )
                ExposedDropdownMenu(
                    expanded = expandSubcategoria,
                    onDismissRequest = { expandSubcategoria = false }
                ) {
                    subcategorias.forEach { subcategoria ->
                        DropdownMenuItem(
                            text = { Text(subcategoria.nombre) },
                            onClick = {
                                subcategoriaSeleccionada = subcategoria
                                onSubcategoriaSelect(subcategoria)
                                expandSubcategoria = false
                            },
                            modifier = Modifier.testTag("itemSubcategoria_${subcategoria.id}")
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Botón guardar
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btnGuardar"),
                onClick = onSaveClick
            ) {
                Text("Guardar Receta")
            }
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — AddRecipeScreenTest
   ------------------------------------------------------------------------- */

class AddRecipeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private var imageClickCount = 0
    private var categoriaSelectCount = 0
    private var subcategoriaSelectCount = 0
    private var saveClickCount = 0
    private var backClickCount = 0

    /* --- TOP BAR Y NAVEGACIÓN --- */

    @Test
    fun addRecipeScreen_muestraTopBar() {
        rule.setContent {
            AddRecipeScreenTestable()
        }

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithText("Agregar Receta").assertIsDisplayed()
    }

    @Test
    fun addRecipeScreen_botonVolverFunciona() {
        rule.setContent {
            AddRecipeScreenTestable(
                onBackClick = { backClickCount++ }
            )
        }

        rule.onNodeWithTag("btnVolver").performClick()
        rule.waitForIdle()

        assert(backClickCount == 1)
    }

    /* --- SELECTOR DE IMAGEN --- */



    @Test
    fun addRecipeScreen_selectorImagenClickable() {
        rule.setContent {
            AddRecipeScreenTestable(
                onImageClick = { imageClickCount++ }
            )
        }

        rule.onNodeWithTag("selectorImagen").performClick()
        rule.waitForIdle()

        assert(imageClickCount == 1)
    }

    /* --- CAMPOS DE TEXTO --- */

    @Test
    fun addRecipeScreen_muestraTodosLosCampos() {
        rule.setContent {
            AddRecipeScreenTestable()
        }

        rule.onNodeWithTag("inputTitulo").assertIsDisplayed()
        rule.onNodeWithTag("inputDescripcion").assertIsDisplayed()
        rule.onNodeWithTag("inputIngredientes").assertIsDisplayed()
        rule.onNodeWithTag("inputInstrucciones").assertIsDisplayed()
    }

    /* --- SELECTOR DE CATEGORÍA --- */

    @Test
    fun addRecipeScreen_muestraDropdownCategoria() {
        rule.setContent {
            AddRecipeScreenTestable()
        }

        rule.onNodeWithTag("dropdownCategoria").assertIsDisplayed()
        rule.onNodeWithTag("inputCategoria").assertIsDisplayed()
    }

    @Test
    fun addRecipeScreen_dropdownCategoriaMuestraOpciones() {
        rule.setContent {
            AddRecipeScreenTestable(
                onCategoriaSelect = { categoriaSelectCount++ }
            )
        }

        // Abrir dropdown
        rule.onNodeWithTag("inputCategoria").performClick()
        rule.waitForIdle()

        // Verificar que se muestran las opciones
        rule.onNodeWithTag("itemCategoria_1").assertIsDisplayed()
        rule.onNodeWithTag("itemCategoria_2").assertIsDisplayed()

        // Seleccionar una categoría
        rule.onNodeWithTag("itemCategoria_1").performClick()
        rule.waitForIdle()

        assert(categoriaSelectCount == 1)
    }

    /* --- SELECTOR DE SUBCATEGORÍA --- */

    @Test
    fun addRecipeScreen_muestraDropdownSubcategoria() {
        rule.setContent {
            AddRecipeScreenTestable()
        }

        rule.onNodeWithTag("dropdownSubcategoria").assertIsDisplayed()
        rule.onNodeWithTag("inputSubcategoria").assertIsDisplayed()
    }

    @Test
    fun addRecipeScreen_dropdownSubcategoriaFunciona() {
        rule.setContent {
            AddRecipeScreenTestable(
                onSubcategoriaSelect = { subcategoriaSelectCount++ }
            )
        }

        // Abrir dropdown de subcategorías
        rule.onNodeWithTag("inputSubcategoria").performClick()
        rule.waitForIdle()

        // Deberían aparecer todas las subcategorías
        rule.onNodeWithTag("itemSubcategoria_1").assertIsDisplayed()
        rule.onNodeWithTag("itemSubcategoria_2").assertIsDisplayed()
        rule.onNodeWithTag("itemSubcategoria_3").assertIsDisplayed()

        // Seleccionar una subcategoría
        rule.onNodeWithTag("itemSubcategoria_1").performClick()
        rule.waitForIdle()

        assert(subcategoriaSelectCount == 1)
    }

    /* --- BOTÓN GUARDAR --- */

    @Test
    fun addRecipeScreen_muestraBotonGuardar() {
        rule.setContent {
            AddRecipeScreenTestable()
        }

        rule.onNodeWithTag("btnGuardar").assertIsDisplayed()
        rule.onNodeWithText("Guardar Receta").assertIsDisplayed()
    }

    @Test
    fun addRecipeScreen_botonGuardarFunciona() {
        rule.setContent {
            AddRecipeScreenTestable(
                onSaveClick = { saveClickCount++ }
            )
        }

        rule.onNodeWithTag("btnGuardar").performClick()
        rule.waitForIdle()

        assert(saveClickCount == 1)
    }
}