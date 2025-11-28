package com.example.receta_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.receta_2.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Mi Perfil") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Información del Usuario", style = MaterialTheme.typography.titleLarge)

            if (currentUser != null) {
                Text("Nombre: ${currentUser?.nombre ?: ""}", style = MaterialTheme.typography.bodyLarge)
                Text("Email: ${currentUser?.email ?: ""}", style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("No hay información de usuario disponible", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onSettingsClick, modifier = Modifier.fillMaxWidth()) { Text("Configuración") }
            Button(
                onClick = {
                    authViewModel.logout()
                    onLogoutClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Cerrar Sesión") }
        }
    }
}
