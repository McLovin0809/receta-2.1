// com/example/receta_2/navigation/AppNavigation.kt
package com.example.receta_2.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.receta_2.ui.screens.*
import com.example.receta_2.viewmodel.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {NavHost(
    navController = navController,
    startDestination = "welcome"
) {

    composable("welcome") {
        WelcomeScreen(navController)
    }

    composable("login") {
        LoginScreen(navController, authViewModel)
    }

    composable("register") {
        RegisterScreen(navController, authViewModel)
    }

    composable("home") {
        HomeScreen(
            navController = navController,
            recipeViewModel = recipeViewModel
        )
    }

    composable("add_recipe") {
        AddRecipeScreen(
            navController = navController,
            recipeViewModel = recipeViewModel,
            authViewModel = authViewModel
        )
    }

    composable("profile") {
        ProfileScreen(
            authViewModel = authViewModel,
            onLogoutClick = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
    }

    composable("favorite") {
        FavoritesScreen(
            navController = navController,
            favoritesViewModel = FavoritesViewModel(),
            isLoggedIn = true
        )

    }
}
}

