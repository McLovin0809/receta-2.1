package com.example.receta_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.receta_2.navigation.AppBottomBar
import com.example.receta_2.navigation.AppScreen
import com.example.receta_2.navigation.ExtraRoutes
import com.example.receta_2.ui.screens.*
import com.example.receta_2.ui.theme.Receta2Theme
import com.example.receta_2.viewmodel.AuthViewModel
import com.example.receta_2.viewmodel.FavoritesViewModel
import com.example.receta_2.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Receta2Theme {
                val authViewModel: AuthViewModel = viewModel()
                val favoritesViewModel: FavoritesViewModel = viewModel()
                val recipeViewModel: RecipeViewModel = viewModel()

                val navController = rememberNavController()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                Scaffold(
                    bottomBar = { AppBottomBar(navController = navController, isLoggedIn = isLoggedIn) }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = AppScreen.Home.route
                    ) {
                        composable(AppScreen.Home.route) {
                            HomeScreen(
                                isLoggedIn = isLoggedIn,
                                navController = navController,
                                onProfileClick = { navController.navigate(AppScreen.Profile.route) },
                                onFavoritesClick = {
                                    if (isLoggedIn) {
                                        navController.navigate("favorites")
                                    } else {
                                        navController.navigate(AppScreen.Login.route) {
                                            popUpTo(AppScreen.Home.route) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        composable("favorites") {
                            if (isLoggedIn) {
                                FavoritesScreen(navController, favoritesViewModel, isLoggedIn)
                            } else {
                                navController.navigate(AppScreen.Login.route) {
                                    popUpTo("favorites") { inclusive = true }
                                }
                            }
                        }

                        composable(AppScreen.Profile.route) {
                            if (isLoggedIn) {
                                ProfileScreen(
                                    onSettingsClick = { navController.navigate(ExtraRoutes.SETTINGS) },
                                    onLogoutClick = {
                                        authViewModel.logout()
                                        navController.navigate(AppScreen.Home.route) {
                                            popUpTo(navController.graph.id) { inclusive = true }
                                        }
                                    }
                                )
                            } else {
                                navController.navigate(AppScreen.Login.route) {
                                    popUpTo(AppScreen.Profile.route) { inclusive = true }
                                }
                            }
                        }

                        composable(
                            route = "recipe_list/{categoryId}/{categoryName}",
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.StringType },
                                navArgument("categoryName") { type = NavType.StringType }
                            )
                        ) { entry ->
                            RecipeListScreen(
                                navController = navController,
                                categoryId = entry.arguments?.getString("categoryId"),
                                categoryName = entry.arguments?.getString("categoryName"),
                                favoritesViewModel = favoritesViewModel,
                                recipeViewModel = recipeViewModel,
                                isLoggedIn = isLoggedIn
                            )
                        }

                        composable(
                            route = "recipe_detail/{recipeId}",
                            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
                        ) { entry ->
                            entry.arguments?.getString("recipeId")?.let { id ->
                                recipeViewModel.loadRecipeById(id)
                            }
                            RecipeDetailScreen(navController = navController, recipeViewModel = recipeViewModel)
                        }

                        composable(AppScreen.Login.route) {
                            // Aquí pasamos el authViewModel, navController, y la función onLoginSuccess
                            LoginScreen(
                                authViewModel = authViewModel,
                                navController = navController,
                                onRegisterClick = { navController.navigate(ExtraRoutes.REGISTER) },
                                onLoginSuccess = { email, password ->
                                    // Al hacer login exitoso, llamamos a la función login del ViewModel
                                    authViewModel.login(email, password)

                                    // Navegar a la pantalla principal después de un login exitoso
                                    navController.navigate(AppScreen.Home.route) {
                                        popUpTo(navController.graph.id) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(ExtraRoutes.REGISTER) {
                            RegisterScreen(navController = navController, authViewModel = authViewModel)
                        }

                        composable(ExtraRoutes.SETTINGS) { SettingsScreen() }

                        composable(ExtraRoutes.ADD_RECIPE) {
                            if (isLoggedIn) {
                                AddRecipeScreen(navController = navController, recipeViewModel = recipeViewModel)
                            } else {
                                navController.navigate(AppScreen.Login.route) {
                                    popUpTo(ExtraRoutes.ADD_RECIPE) { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}