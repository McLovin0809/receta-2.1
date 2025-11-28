package com.example.receta_2

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
import org.junit.Rule
import org.junit.Test

/* -------------------------------------------------------------------------
   FAKE RECIPE VIEWMODEL
   ------------------------------------------------------------------------- */

class FakeRecipeDetailVM(initial: Recipe? = null) {
    private val _currentRecipe = MutableStateFlow(initial)
    val currentRecipe: StateFlow<Recipe?> = _currentRecipe

    fun setRecipe(recipe: Recipe?) {
        _currentRecipe.value = recipe
    }
}

/* -------------------------------------------------------------------------
   TESTABLE RECIPE DETAIL SCREEN
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
                        recipe?.name ?: "Detalle",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("title")
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}, modifier = Modifier.testTag("btnBack")) {
                        Icon(Icons.Default.ArrowBack, "")
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

                // Imagen simulada (AsyncImage no se testea bien)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .testTag("recipeImage")
                )

                Column(Modifier.padding(16.dp)) {

                    Text(
                        r.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.testTag("recipeName")
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        r.description,
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
                        r.ingredients.forEachIndexed { index, ing ->
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
                        r.steps.forEachIndexed { index, step ->
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

    private val recipe = Recipe(
        id = "1",
        name = "Torta de Vainilla",
        description = "Una deliciosa receta de torta esponjosa.",
        image = "https://fake.com/img.jpg",
        ingredients = listOf("Harina", "Huevos", "Azúcar"),
        steps = listOf("Mezclar ingredientes", "Hornear por 30 minutos"),
        categoryIds = listOf("postres")
    )

    private lateinit var fakeVM: FakeRecipeDetailVM

    private fun setContent(recipe: Recipe?) {
        fakeVM = FakeRecipeDetailVM(recipe)
        rule.setContent {
            RecipeDetailScreenTestable(fakeVM)
        }
    }

    /* --- LOADING --- */

    @Test
    fun recipeDetail_muestraLoadingSiEsNull() {
        setContent(recipe = null)

        rule.onNodeWithTag("loading").assertIsDisplayed()
    }

    /* --- TITULO --- */

    @Test
    fun recipeDetail_muestraTituloCorrecto() {
        setContent(recipe)

        rule.onNodeWithTag("title").assertTextContains("Torta de Vainilla")
    }

    /* --- IMAGEN --- */

    @Test
    fun recipeDetail_muestraImagen() {
        setContent(recipe)

        rule.onNodeWithTag("recipeImage").assertIsDisplayed()
    }

    /* --- DESCRIPCIÓN --- */

    @Test
    fun recipeDetail_muestraDescripcion() {
        setContent(recipe)

        rule.onNodeWithTag("recipeDescription")
            .assertIsDisplayed()
            .assertTextContains("torta esponjosa", ignoreCase = true)
    }

    /* --- INGREDIENTES --- */

    @Test
    fun recipeDetail_muestraIngredientes() {
        setContent(recipe)

        rule.onNodeWithTag("ing_0").assertTextContains("Harina")
        rule.onNodeWithTag("ing_1").assertTextContains("Huevos")
        rule.onNodeWithTag("ing_2").assertTextContains("Azúcar")
    }

    /* --- PASOS --- */

    @Test
    fun recipeDetail_muestraPasos() {
        setContent(recipe)

        rule.onNodeWithTag("step_0").assertTextContains("Mezclar")
        rule.onNodeWithTag("step_1").assertTextContains("Hornear")
    }

    /* --- SCROLL Y ESTRUCTURA GENERAL --- */

    @Test
    fun recipeDetail_muestraTodoElContenido() {
        setContent(recipe)

        rule.onNodeWithTag("ingredientesTitle").assertIsDisplayed()
        rule.onNodeWithTag("stepsTitle").assertIsDisplayed()
    }
}