package com.umt.memingoapp.navigation

// Sealed class for defining routes
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object GetStarted : Screen("get_started")
    object HomeScreen : Screen("home")
    object Dashboard : Screen("dashboard")
    object Otp: Screen("otp_screen")
}
