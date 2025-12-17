package com.example.receta_2.navigation

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object Login : AppScreen("login")
    object Profile : AppScreen("profile")

    object Register : AppScreen(ExtraRoutes.REGISTER)

    object Settings : AppScreen(ExtraRoutes.SETTINGS)
    object AddRecipe : AppScreen(ExtraRoutes.ADD_RECIPE)

    object Favorite : AppScreen(ExtraRoutes.FAVORITE)


}

object ExtraRoutes {
    const val REGISTER = "register"
    const val SETTINGS = "settings"
    const val ADD_RECIPE = "add_recipe"

    const val FAVORITE = "favorite"
}
