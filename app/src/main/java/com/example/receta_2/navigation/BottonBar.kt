package com.example.receta_2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(navController: NavController, isLoggedIn: Boolean) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute?.startsWith(AppScreen.Home.route) == true,
            onClick = {
                navController.navigate(AppScreen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        val destination = if (isLoggedIn) AppScreen.Profile.route else AppScreen.Login.route
        val label = if (isLoggedIn) "Perfil" else "Login"
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = label) },
            label = { Text(label) },
            selected = currentRoute?.startsWith(destination) == true,
            onClick = {
                navController.navigate(destination) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
