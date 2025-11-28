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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.receta_2.data.remote.*
import com.example.receta_2.data.repository.RecetaRepository
import com.example.receta_2.navigation.AppBottomBar
import com.example.receta_2.navigation.AppScreen
import com.example.receta_2.navigation.ExtraRoutes
import com.example.receta_2.ui.screens.*
import com.example.receta_2.ui.theme.Receta2Theme
import com.example.receta_2.viewmodel.AuthViewModel
import com.example.receta_2.viewmodel.FavoritesViewModel
import com.example.receta_2.viewmodel.RecipeViewModel
import com.example.receta_2.viewmodel.RecipeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializamos Retrofit y el repositorio con TODOS los servicios
        val recetaApiService = RecetaApiService.create()
        val imagenApiService = ImagenRecetaApiService.create()
        val categoriaApiService = CategoriaApiService.create()
        val subcategoriaApiService = SubcategoriaApiService.create()

        val recetaRepository = RecetaRepository(
            apiRecetas = recetaApiService,
            apiImagen = imagenApiService,
            apiCategorias = categoriaApiService,
            apiSubcategorias = subcategoriaApiService
        )

        setContent {
            Receta2Theme {
                val authViewModel: AuthViewModel = viewModel()
                val favoritesViewModel: FavoritesViewModel = viewModel()
                val recipeViewModel: RecipeViewModel = viewModel(
                    factory = RecipeViewModelFactory(recetaRepository)
                )

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
                                recipeViewModel = recipeViewModel,
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
                                FavoritesScreen(
                                    navController = navController,
                                    favoritesViewModel = favoritesViewModel,
                                    isLoggedIn = isLoggedIn
                                )
                            } else {
                                navController.navigate(AppScreen.Login.route) {
                                    popUpTo("favorites") { inclusive = true }
                                }
                            }
                        }

                        composable(AppScreen.Profile.route) {
                            if (isLoggedIn) {
                                ProfileScreen(
                                    authViewModel = authViewModel,
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
                                navArgument("categoryId") { type = NavType.IntType },
                                navArgument("categoryName") { type = NavType.StringType }
                            )
                        ) { entry ->
                            RecipeListScreen(
                                navController = navController,
                                categoryId = entry.arguments?.getInt("categoryId"),
                                categoryName = entry.arguments?.getString("categoryName"),
                                favoritesViewModel = favoritesViewModel,
                                recipeViewModel = recipeViewModel,
                                isLoggedIn = isLoggedIn
                            )
                        }

                        composable(
                            route = "recipe_detail/{recipeId}",
                            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                        ) { entry ->
                            entry.arguments?.getInt("recipeId")?.let { id ->
                                recipeViewModel.cargarDetalle(id)
                            }
                            RecipeDetailScreen(navController = navController, recipeViewModel = recipeViewModel)
                        }

                        composable(AppScreen.Login.route) {
                            LoginScreen(
                                authViewModel = authViewModel,
                                navController = navController,
                                onRegisterClick = { navController.navigate(ExtraRoutes.REGISTER) },
                                onLoginSuccess = {
                                    navController.navigate(AppScreen.Home.route) {
                                        popUpTo(navController.graph.id) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(ExtraRoutes.REGISTER) {
                            RegisterScreen(navController = navController, authViewModel = authViewModel)
                        }

                        composable(ExtraRoutes.SETTINGS) {
                            SettingsScreen(navController = navController, authViewModel = authViewModel)
                        }

                        composable(ExtraRoutes.ADD_RECIPE) {
                            if (isLoggedIn) {
                                AddRecipeScreen(
                                    navController = navController,
                                    recipeViewModel = recipeViewModel,
                                    currentUser = authViewModel.currentUser.collectAsState().value
                                )
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
