package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.receta_2.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogoutClick: () -> Unit
) {
    val user by authViewModel.user.collectAsState()

    Column(Modifier.padding(24.dp)) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium)
        Text("Nombre: ${user?.nombre}")
        Text("Email: ${user?.email}")

        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión")
        }
    }
}
