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
import com.example.receta_2.data.model.*
import com.example.receta_2.viewmodel.RecipeViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel,
    currentUser: Usuario?
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var ingredients by rememberSaveable { mutableStateOf("") }
    var steps by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    var selectedCategory by rememberSaveable { mutableStateOf<Categoria?>(null) }
    var selectedSubcategory by rememberSaveable { mutableStateOf<Subcategoria?>(null) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedSubcategory by remember { mutableStateOf(false) }

    val categorias by recipeViewModel.categorias.collectAsState()
    val subcategorias by recipeViewModel.subcategorias.collectAsState()

    var showError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        recipeViewModel.cargarCategorias()
        recipeViewModel.cargarSubcategorias()
    }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) imagePickerLauncher.launch("image/*")
    }

    val permission = if (android.os.Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val isFormValid by remember {
        derivedStateOf {
            name.isNotBlank() &&
                    description.isNotBlank() &&
                    ingredients.isNotBlank() &&
                    steps.isNotBlank() &&
                    imageUri != null &&
                    selectedCategory != null &&
                    selectedSubcategory != null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Añadir Receta") },
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
                        val nuevaReceta = Receta(
                            titulo = name,
                            descripcion = description,
                            ingredientes = ingredients,
                            instrucciones = steps,
                            usuario = currentUser?.let { Usuario(id = it.id, nombre = it.nombre, email = it.email, password = it.password) },
                            categoria = selectedCategory?.let { Categoria(id = it.id, nombre = "") },
                            subcategoria = selectedSubcategory?.let { Subcategoria(id = it.id, nombre = "") }
                        )

                        val imagenPart = imageUri?.let { uri ->
                            try {
                                val inputStream = context.contentResolver.openInputStream(uri) ?: return@let null
                                val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                                tempFile.outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                                val requestFile = RequestBody.create("image/*".toMediaType(), tempFile)
                                MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
                            } catch (e: Exception) {
                                null
                            }
                        }

                        recipeViewModel.crear(nuevaReceta, imagenPart) { success ->
                            if (success) {
                                navController.navigateUp()
                            } else {
                                showError = true
                            }
                        }
                    } else {
                        showError = true
                    }
                },
                containerColor = if (isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(Icons.Default.Check, contentDescription = "Guardar")
            }
        }
    ) { paddingValues ->
        if (showError) {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar("Revisa los campos o intenta nuevamente.")
                showError = false
            }
        }

        val isLoading = categorias.isEmpty() || subcategorias.isEmpty()

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Completa los datos de tu receta", style = MaterialTheme.typography.titleMedium)

                // Selector de imagen
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Seleccionar imagen", modifier = Modifier.size(48.dp))
                            Text("Toca para seleccionar una imagen")
                        }
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(imageUri).crossfade(true).build(),
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Campos de texto
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Ingredientes") }, modifier = Modifier.fillMaxWidth().height(150.dp))
                OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Pasos") }, modifier = Modifier.fillMaxWidth().height(150.dp))

                // Dropdown Categoría
                ExposedDropdownMenuBox(expanded = expandedCategory, onExpandedChange = { expandedCategory = !expandedCategory }) {
                    OutlinedTextField(
                        value = selectedCategory?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria.nombre) },
                                onClick = {
                                    selectedCategory = categoria
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
                // Dropdown Subcategoría
                ExposedDropdownMenuBox(
                    expanded = expandedSubcategory,
                    onExpandedChange = { expandedSubcategory = !expandedSubcategory }
                ) {
                    OutlinedTextField(
                        value = selectedSubcategory?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Subcategoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubcategory) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSubcategory,
                        onDismissRequest = { expandedSubcategory = false }
                    ) {
                        subcategorias.forEach { subcat ->
                            DropdownMenuItem(
                                text = { Text(subcat.nombre) },
                                onClick = {
                                    selectedSubcategory = subcat
                                    expandedSubcategory = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

