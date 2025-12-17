package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.receta_2.data.model.Usuario
import com.example.receta_2.navigation.AppScreen
import com.example.receta_2.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

    val emailValid = email.contains("@")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Registrarse", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })

        OutlinedTextField(
            email,
            { email = it },
            label = { Text("Email") },
            isError = email.isNotEmpty() && !emailValid
        )

        OutlinedTextField(
            password,
            { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            enabled = emailValid && password.length >= 6,
            onClick = {
                authViewModel.register(
                    Usuario(nombre = nombre, email = email, password = password)
                ) {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
        ) {
            Text("Registrarse")
        }
        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Iniciar sesión")
        }
    }
}


