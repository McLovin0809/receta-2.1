package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receta_2.navigation.AppScreen
import com.example.receta_2.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,  // Este parámetro es necesario para interactuar con el ViewModel
    navController: NavController,  // Para navegar a otras pantallas
    onLoginSuccess: (String, String) -> Unit,  // Esta función maneja el éxito del login
    onRegisterClick: () -> Unit  // Navegar a la pantalla de registro
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by authViewModel.errorMessage.collectAsState()

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#\$%^&+=!]{6,}$")

    val isEmailValid = emailRegex.matches(email)
    val isPasswordValid = passwordRegex.matches(password)
    val isFormValid = email.isNotBlank() && password.isNotBlank() && isEmailValid && isPasswordValid

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            isError = email.isNotEmpty() && !isEmailValid,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isEmailValid && email.isNotEmpty()) {
            Text("Correo inválido", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isNotEmpty() && !isPasswordValid,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isPasswordValid && password.isNotEmpty()) {
            Text("Debe tener al menos 6 caracteres y contener letras y números", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                // Llamamos a la función onLoginSuccess que fue pasada como parámetro
                onLoginSuccess(email, password)
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(Modifier.height(16.dp))
        if (!errorMessage.isNullOrEmpty()) {
            Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onRegisterClick) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }

    // Navegar si login es exitoso
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    if (isLoggedIn) {
        LaunchedEffect(Unit) {
            navController.navigate(AppScreen.Home.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }
}