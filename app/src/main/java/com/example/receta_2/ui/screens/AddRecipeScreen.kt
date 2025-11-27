package com.example.receta_2.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.receta_2.data.model.Recipe
import com.example.receta_2.viewmodel.RecipeViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var ingredients by rememberSaveable { mutableStateOf("") }
    var steps by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) imagePickerLauncher.launch("image/*")
    }

    val permission = if (android.os.Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val isFormValid by remember {
        derivedStateOf {
            name.isNotBlank() && description.isNotBlank() && ingredients.isNotBlank() && steps.isNotBlank() && imageUri != null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Nueva Receta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isFormValid) {
                        val newRecipe = Recipe(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            description = description,
                            image = imageUri.toString(),
                            ingredients = ingredients.lines().filter { it.isNotBlank() },
                            steps = steps.lines().filter { it.isNotBlank() },
                            categoryIds = listOf("cat_user")
                        )
                        recipeViewModel.addRecipe(newRecipe)
                        navController.navigateUp()
                    }
                },
                containerColor = if (isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ) { Icon(Icons.Default.Check, contentDescription = "Guardar Receta") }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Completa los datos de tu receta", style = MaterialTheme.typography.titleMedium)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .clickable {
                        val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
                        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                            imagePickerLauncher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permission)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Seleccionar imagen", modifier = Modifier.size(48.dp))
                        Text("Toca para seleccionar una imagen", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imageUri).crossfade(true).build(),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre de la receta") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción corta") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
            OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Ingredientes (uno por línea)") }, modifier = Modifier.fillMaxWidth().height(150.dp))
            OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Pasos de preparación (uno por línea)") }, modifier = Modifier.fillMaxWidth().height(200.dp))
        }
    }
}
