// Test: RecipeDetailScreenTest.kt
package com.example.receta_2.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.receta_2.data.model.*
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAKE RECIPE VIEWMODEL - USANDO TU MODELO Receta
   ------------------------------------------------------------------------- */

class FakeRecipeDetailVM(initial: Receta? = null) {
    private val _currentRecipe = MutableStateFlow(initial)
    val currentRecipe: StateFlow<Receta?> = _currentRecipe

    fun setRecipe(recipe: Receta?) {
        _currentRecipe.value = recipe
    }
}

/* -------------------------------------------------------------------------
   TESTABLE RECIPE DETAIL SCREEN - USANDO TU MODELO Receta
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreenTestable(
    viewModel: FakeRecipeDetailVM
) {
    val recipe by viewModel.currentRecipe.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.testTag("topBar"),
                title = {
                    Text(
                        recipe?.titulo ?: "Detalle",  // Cambiado a titulo
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("title")
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}, modifier = Modifier.testTag("btnBack")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        recipe?.let { r ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {

                // Imagen simulada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .testTag("recipeImage")
                )

                Column(Modifier.padding(16.dp)) {

                    Text(
                        r.titulo,  // Cambiado a titulo
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.testTag("recipeName")
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        r.descripcion,  // Cambiado a descripcion
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.testTag("recipeDescription")
                    )

                    Divider(Modifier.padding(vertical = 24.dp))

                    Text(
                        "Ingredientes",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.testTag("ingredientesTitle")
                    )

                    Spacer(Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Parsear ingredientes (String con saltos de línea)
                        r.ingredientes.lines()
                            .filter { it.isNotBlank() }
                            .forEachIndexed { index, ing ->
                                IngredientItemTestable(
                                    ing,
                                    tag = "ing_${index}"
                                )
                            }
                    }

                    Divider(Modifier.padding(vertical = 24.dp))

                    Text(
                        "Preparación",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.testTag("stepsTitle")
                    )

                    Spacer(Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Parsear instrucciones (String con saltos de línea)
                        r.instrucciones.lines()
                            .filter { it.isNotBlank() }
                            .forEachIndexed { index, step ->
                                StepItemTestable(
                                    number = index + 1,
                                    text = step,
                                    tag = "step_${index}"
                                )
                            }
                    }
                }
            }

        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.testTag("loading"))
        }
    }
}

/* -------------------------------------------------------------------------
   INGREDIENT ITEM TESTABLE
   ------------------------------------------------------------------------- */

@Composable
fun IngredientItemTestable(text: String, tag: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = text)
    }
}

/* -------------------------------------------------------------------------
   STEP ITEM TESTABLE
   ------------------------------------------------------------------------- */

@Composable
fun StepItemTestable(number: Int, text: String, tag: String) {
    Row(modifier = Modifier.testTag(tag)) {
        Text("$number.")
        Spacer(Modifier.width(12.dp))
        Text(text)
    }
}

/* -------------------------------------------------------------------------
   TESTS — RecipeDetailScreenTest
   ------------------------------------------------------------------------- */

class RecipeDetailScreenTest {

    @get:Rule
    val rule = createComposeRule()

    // Crear receta usando TU modelo real Receta
    private val receta = Receta(
        id = 1,
        titulo = "Torta de Vainilla",  // titulo, no name
        descripcion = "Una deliciosa receta de torta esponjosa.",
        ingredientes = "Harina\nHuevos\nAzúcar\nVainilla",  // String con saltos de línea
        instrucciones = "1. Mezclar ingredientes\n2. Hornear por 30 minutos\n3. Decorar",  // String con saltos de línea
        usuario = IdWrapper(1),
        categoria = IdWrapper(1),
        subcategoria = IdWrapper(1)
    )

    private lateinit var fakeVM: FakeRecipeDetailVM

    private fun setContent(receta: Receta?) {
        fakeVM = FakeRecipeDetailVM(receta)
        rule.setContent {
            RecipeDetailScreenTestable(fakeVM)
        }
    }

    /* --- LOADING --- */

    @Test
    fun recipeDetail_muestraLoadingSiEsNull() {
        setContent(receta = null)

        rule.onNodeWithTag("loading").assertIsDisplayed()
    }

    /* --- TOP BAR Y NAVEGACIÓN --- */

    @Test
    fun recipeDetail_muestraTopBar() {
        setContent(receta)

        rule.onNodeWithTag("topBar").assertIsDisplayed()
        rule.onNodeWithTag("btnBack").assertIsDisplayed()
    }

    @Test
    fun recipeDetail_muestraTituloEnTopBar() {
        setContent(receta)

        rule.onNodeWithTag("title").assertTextContains("Torta de Vainilla")
    }

    /* --- IMAGEN --- */

    @Test
    fun recipeDetail_muestraImagen() {
        setContent(receta)

        rule.onNodeWithTag("recipeImage").assertIsDisplayed()
    }

    /* --- DETALLES DE LA RECETA --- */

    @Test
    fun recipeDetail_muestraNombreReceta() {
        setContent(receta)

        rule.onNodeWithTag("recipeName")
            .assertIsDisplayed()
            .assertTextContains("Torta de Vainilla")
    }


    /* --- INGREDIENTES --- */

    @Test
    fun recipeDetail_muestraTituloIngredientes() {
        setContent(receta)

        rule.onNodeWithTag("ingredientesTitle")
            .assertIsDisplayed()
            .assertTextContains("Ingredientes")
    }



    /* --- PASOS DE PREPARACIÓN --- */

    @Test
    fun recipeDetail_muestraTituloPasos() {
        setContent(receta)

        rule.onNodeWithTag("stepsTitle")
            .assertIsDisplayed()
            .assertTextContains("Preparación")
    }



    /* --- ESTRUCTURA COMPLETA --- */

    @Test
    fun recipeDetail_muestraTodoElContenido() {
        setContent(receta)

        // Verificar estructura completa
        rule.onNodeWithTag("recipeImage").assertIsDisplayed()
        rule.onNodeWithTag("recipeName").assertIsDisplayed()
        rule.onNodeWithTag("recipeDescription").assertIsDisplayed()
        rule.onNodeWithTag("ingredientesTitle").assertIsDisplayed()
        rule.onNodeWithTag("stepsTitle").assertIsDisplayed()

        // Verificar que hay ingredientes
        rule.onNodeWithTag("ing_0").assertIsDisplayed()

        // Verificar que hay pasos
        rule.onNodeWithTag("step_0").assertIsDisplayed()
    }

    @Test
    fun recipeDetail_cambioDeReceta() {
        setContent(null)

        // Inicialmente loading
        rule.onNodeWithTag("loading").assertIsDisplayed()

        // Cambiar a receta
        fakeVM.setRecipe(receta)
        rule.waitForIdle()

        // Ahora debería mostrar la receta
        rule.onNodeWithTag("recipeName").assertIsDisplayed()
        rule.onNodeWithTag("recipeName").assertTextContains("Torta de Vainilla")
    }




}