// com/example/receta_2/MainActivity.kt
package com.example.receta_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.receta_2.data.remote.RetrofitInstance
import com.example.receta_2.data.repository.*
import com.example.receta_2.navigation.AppNavigation
import com.example.receta_2.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            val authRepo = AuthRepository(RetrofitInstance.apiUsuario)
            val recetaRepo = RecetaRepository(RetrofitInstance.apiRecetas)
            val categoriaRepo = CategoriaRepository(RetrofitInstance.apiCategorias)
            val subRepo = SubcategoriaRepository(RetrofitInstance.apiSubcategorias)

            val authViewModel: AuthViewModel =
                viewModel(factory = AuthViewModelFactory(authRepo))

            val recipeViewModel: RecipeViewModel =
                viewModel(factory = RecipeViewModelFactory(recetaRepo, categoriaRepo, subRepo))

            AppNavigation(
                navController = navController,
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }
    }
}
