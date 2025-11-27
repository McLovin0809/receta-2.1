package com.example.receta_2.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.receta_2.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var doPasswordsMatch by remember { mutableStateOf(true) }

    val isFormValid by remember(username, email, password, confirmPassword) {
        derivedStateOf {
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            doPasswordsMatch = password == confirmPassword
            username.isNotBlank() && email.isNotBlank() && password.length >= 6 && doPasswordsMatch
        }
    }

    val errorMessage by authViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                isError = !isEmailValid && email.isNotEmpty(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isEmailValid && email.isNotEmpty()) Text("Introduce un correo válido", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (mín. 6 caracteres)") },
                visualTransformation = PasswordVisualTransformation(),
                isError = password.isNotEmpty() && password.length < 6,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = !doPasswordsMatch && confirmPassword.isNotEmpty(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (!doPasswordsMatch && confirmPassword.isNotEmpty()) Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
            if (!errorMessage.isNullOrEmpty()) Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.register(
                        nombre = username,
                        email = email,
                        password = password
                    ) {
                        // Registro exitoso, navegar a Login
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }
        }
    }
}
