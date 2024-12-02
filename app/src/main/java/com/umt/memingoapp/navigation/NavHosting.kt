package com.umt.memingoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umt.memingoapp.GetStartedScreen
import com.umt.memingoapp.LoginForm
import com.umt.memingoapp.SignUpForm
import com.umt.memingoapp.models.Screen

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.GetStarted.route) {
        composable(Screen.GetStarted.route) {
            GetStartedScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginForm(navController)
        }
        composable(Screen.SignUp.route) {
            SignUpForm(navController)
        }
    }
}
