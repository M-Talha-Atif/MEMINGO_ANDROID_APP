package com.umt.memingoapp.models

// Sealed class for defining routes
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object GetStarted : Screen("get_started")
    object HomeScreen : Screen("home_screen")
}
