package com.example.receta_2

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.junit.Rule
import org.junit.Test
import java.util.*

/* -------------------------------------------------------------------------
   PANTALLA TESTABLE — AddRecipeScreenTestable
   ------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreenTestable(
    onSave: (Recipe) -> Unit,
    imageUriExternal: Uri? = null
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf(imageUriExternal) }

    val isFormValid by derivedStateOf {
        name.isNotBlank() &&
                description.isNotBlank() &&
                ingredients.isNotBlank() &&
                steps.isNotBlank() &&
                imageUri != null
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Añadir Nueva Receta") }) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag("fabGuardar"),
                onClick = {
                    if (isFormValid) {
                        onSave(
                            Recipe(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                description = description,
                                image = imageUri.toString(),
                                ingredients = ingredients.lines().filter { it.isNotBlank() },
                                steps = steps.lines().filter { it.isNotBlank() },
                                categoryIds = listOf("cat_user")
                            )
                        )
                    }
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Guardar Receta")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Caja de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .testTag("imagePicker")
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imageUri).build(),
                        contentDescription = "Imagen seleccionada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().testTag("imagePreview")
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().testTag("inputNombre")
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().testTag("inputDescripcion")
            )

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredientes") },
                modifier = Modifier.fillMaxWidth().testTag("inputIngredientes")
            )

            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text("Pasos") },
                modifier = Modifier.fillMaxWidth().testTag("inputPasos")
            )
        }
    }
}

/* -------------------------------------------------------------------------
   TESTS — AddRecipeScreenTest
   ------------------------------------------------------------------------- */

class AddRecipeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private var savedRecipe: Recipe? = null

    @Test
    fun addRecipe_muestraCamposIniciales() {

        rule.setContent {
            AddRecipeScreenTestable(onSave = {})
        }

        rule.onNodeWithTag("inputNombre").assertIsDisplayed()
        rule.onNodeWithTag("inputDescripcion").assertIsDisplayed()
        rule.onNodeWithTag("inputIngredientes").assertIsDisplayed()
        rule.onNodeWithTag("inputPasos").assertIsDisplayed()
        rule.onNodeWithTag("fabGuardar").assertIsDisplayed()
    }

    @Test
    fun addRecipe_noGuardaSiFaltanCampos() {

        rule.setContent {
            AddRecipeScreenTestable(onSave = { savedRecipe = it })
        }

        rule.onNodeWithTag("inputNombre").performTextInput("Mi receta")

        rule.onNodeWithTag("fabGuardar").performClick()

        assert(savedRecipe == null)
    }

    @Test
    fun addRecipe_noGuardaSiNoHayImagen() {

        rule.setContent {
            AddRecipeScreenTestable(onSave = { savedRecipe = it })
        }

        rule.onNodeWithTag("inputNombre").performTextInput("Torta")
        rule.onNodeWithTag("inputDescripcion").performTextInput("Descripción")
        rule.onNodeWithTag("inputIngredientes").performTextInput("Harina")
        rule.onNodeWithTag("inputPasos").performTextInput("Mezclar")

        rule.onNodeWithTag("fabGuardar").performClick()

        assert(savedRecipe == null)
    }

    @Test
    fun addRecipe_muestraImagenCuandoExiste() {

        val fakeUri = Uri.parse("file://imagen_test.jpg")

        rule.setContent {
            AddRecipeScreenTestable(onSave = {}, imageUriExternal = fakeUri)
        }

        rule.onNodeWithTag("imagePreview").assertIsDisplayed()
    }

    @Test
    fun addRecipe_guardaRecetaCorrectamente() {

        val fakeUri = Uri.parse("file://imagen_test.jpg")

        rule.setContent {
            AddRecipeScreenTestable(onSave = { savedRecipe = it }, imageUriExternal = fakeUri)
        }

        rule.onNodeWithTag("inputNombre").performTextInput("Brownie")
        rule.onNodeWithTag("inputDescripcion").performTextInput("Muy rico")
        rule.onNodeWithTag("inputIngredientes").performTextInput("Chocolate")
        rule.onNodeWithTag("inputPasos").performTextInput("Hornear")

        rule.onNodeWithTag("fabGuardar").performClick()

        assert(savedRecipe != null)
        assert(savedRecipe!!.name == "Brownie")
        assert(savedRecipe!!.ingredients.size == 1)
    }
}
