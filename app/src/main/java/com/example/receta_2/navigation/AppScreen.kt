package com.example.receta_2.navigation

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object Login : AppScreen("login")
    object Profile : AppScreen("profile")
}

object ExtraRoutes {
    const val REGISTER = "register"
    const val SETTINGS = "settings"
    const val ADD_RECIPE = "add_recipe"
}
