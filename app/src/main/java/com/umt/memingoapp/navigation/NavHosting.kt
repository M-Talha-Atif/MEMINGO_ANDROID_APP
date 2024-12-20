package com.umt.memingoapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umt.memingoapp.CourseSelectionScreen
import com.umt.memingoapp.GetStartedScreen
import com.umt.memingoapp.LoginForm
import com.umt.memingoapp.OtpScreen
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
        composable(Screen.HomeScreen.route) {
          CourseSelectionScreen(navController)
//            CourseSelectionScreen(
//                onNavigateToLesson = { courseId ->
//                   // navController.navigate("lesson/$courseId")
//                },
//                navController
//            )

        }
//        // Otp screen now accepts an email argument
//        composable(
//            Screen.Otp.route + "?email={email}",
//            arguments = listOf(navArgument("email") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            OtpScreen(navController = navController, authViewModel =  viewModel())
//        }

        composable(Screen.Otp.route) {
            OtpScreen(navController)
        }
    }
}
