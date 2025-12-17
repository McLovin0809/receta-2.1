package com.example.receta_2.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.receta_2.data.model.*
import com.example.receta_2.viewmodel.AuthViewModel
import com.example.receta_2.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel
) {

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionToRequest = if (android.os.Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        }
    }

    val categorias by recipeViewModel.categorias.collectAsState()
    val subcategorias by recipeViewModel.subcategorias.collectAsState()
    val userId by authViewModel.userId.collectAsState()

    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var ingredientes by rememberSaveable { mutableStateOf("") }
    var instrucciones by rememberSaveable { mutableStateOf("") }

    var categoriaSeleccionada by rememberSaveable { mutableStateOf<Categoria?>(null) }
    var subcategoriaSeleccionada by rememberSaveable { mutableStateOf<Subcategoria?>(null) }

    var expandCategoria by remember { mutableStateOf(false) }
    var expandSubcategoria by remember { mutableStateOf(false) }

    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        recipeViewModel.cargarCategorias()
        recipeViewModel.cargarSubcategorias()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Receta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // Icono corregido para usar la versión AutoMirrored que se adapta a la dirección de lectura
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado consistente
        ) {

            // --- INICIO DEL COMPONENTE VISUAL PARA LA IMAGEN ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .clickable {
                        // Lógica para pedir permiso y abrir la galería
                        val permissionStatus =
                            ContextCompat.checkSelfPermission(context, permissionToRequest)
                        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                            imagePickerLauncher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    // Vista que se muestra si no hay imagen seleccionada
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Icono de cámara", modifier = Modifier.size(48.dp))
                        Text("Toca para seleccionar una imagen", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // Vista que muestra la imagen seleccionada usando Coil
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Imagen de la receta",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Para que la imagen cubra todo el espacio
                    )
                }
            }
            // --- FIN DEL COMPONENTE VISUAL PARA LA IMAGEN ---


            OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(ingredientes, { ingredientes = it }, label = { Text("Ingredientes (uno por línea)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(instrucciones, { instrucciones = it }, label = { Text("Instrucciones (una por línea)") }, modifier = Modifier.fillMaxWidth())

            // ===== CATEGORIA =====
            ExposedDropdownMenuBox(
                expanded = expandCategoria,
                onExpandedChange = { expandCategoria = !expandCategoria }
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada?.nombre ?: "",
                    onValueChange = {}, readOnly = true,
                    label = { Text("Categoría") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandCategoria,
                    onDismissRequest = { expandCategoria = false }
                ) {
                    categorias.forEach {
                        DropdownMenuItem(
                            text = { Text(it.nombre) },
                            onClick = {
                                categoriaSeleccionada = it
                                subcategoriaSeleccionada = null
                                expandCategoria = false
                            }
                        )
                    }
                }
            }

            // ===== SUBCATEGORIA =====
            ExposedDropdownMenuBox(
                expanded = expandSubcategoria,
                onExpandedChange = { expandSubcategoria = !expandSubcategoria }
            ) {
                OutlinedTextField(
                    value = subcategoriaSeleccionada?.nombre ?: "",
                    onValueChange = {}, readOnly = true,
                    label = { Text("Subcategoría") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandSubcategoria,
                    onDismissRequest = { expandSubcategoria = false }
                ) {
                    subcategorias
                        .filter { it.categoria?.id == categoriaSeleccionada?.id }
                        .forEach {
                            DropdownMenuItem(
                                text = { Text(it.nombre) },
                                onClick = {
                                    subcategoriaSeleccionada = it
                                    expandSubcategoria = false
                                }
                            )
                        }
                }
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val currentUserId = userId // Obtenemos el valor actual del ID del usuario
                    if (
                        titulo.isBlank() ||
                        descripcion.isBlank() ||
                        ingredientes.isBlank() ||
                        instrucciones.isBlank() ||
                        categoriaSeleccionada == null ||
                        subcategoriaSeleccionada == null ||
                        currentUserId == null ||
                        imageUri == null // Se añade la validación para la imagen
                    ) {
                        error = "Completa todos los campos, incluyendo la imagen"
                        return@Button
                    }

                    // Se crea el objeto Receta, asumiendo que el modelo espera la URI como String
                    val receta = Receta(
                        titulo = titulo,
                        descripcion = descripcion,
                        ingredientes = ingredientes,
                        instrucciones = instrucciones,
                        usuario = IdWrapper(id = currentUserId),
                        categoria = IdWrapper(categoriaSeleccionada!!.id),
                        subcategoria = IdWrapper(subcategoriaSeleccionada!!.id),
                        // imagen = imageUri.toString() // Descomenta esta línea cuando tu modelo `Receta` tenga el campo `imagen`
                    )

                    recipeViewModel.crearReceta(
                        receta,
                        onSuccess = { navController.popBackStack() },
                        onError = { error = it }
                    )
                }
            ) {
                Text("Guardar Receta")
            }
        }
    }
}
