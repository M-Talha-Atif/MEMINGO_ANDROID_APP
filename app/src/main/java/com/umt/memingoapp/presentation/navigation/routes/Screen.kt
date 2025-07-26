package com.umt.memingoapp.presentation.navigation.routes

// Sealed class for defining routes
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object GetStarted : Screen("get_started")
    object Profile : Screen("profile")
    object HomeScreen : Screen("home")
    object Dashboard : Screen("dashboard")
    object Otp: Screen("otp_screen")
    object LessonScreen : Screen("lesson_screen")
    object VocabularyScreen : Screen("vocabulary_screen")
    object ARScreen : Screen("ar_screen")
    object Splash : Screen("splash_screen")
    object Leaderboard : Screen("leaderboard")
    object Chat : Screen("chat")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
}
