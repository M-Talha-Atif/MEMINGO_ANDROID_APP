package com.umt.memingoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umt.memingoapp.CourseHomeScreen
import com.umt.memingoapp.CourseSelectionScreen
import com.umt.memingoapp.GetStartedScreen
import com.umt.memingoapp.LoginForm
import com.umt.memingoapp.OtpScreen
import com.umt.memingoapp.SignUpForm
import com.umt.memingoapp.lessons.LessonScreenWithViewModel

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

        composable("${Screen.Otp.route}/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreen(navController, email)
        }


        composable(Screen.HomeScreen.route) {
          CourseSelectionScreen(navController)

        }


        composable("${Screen.Dashboard.route}/{course_id}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("course_id") ?: ""
            CourseHomeScreen(navController,courseId)
        }


        composable("${Screen.LessonScreen.route}/{course_id}/{lesson_id}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("course_id") ?: ""
            val lessonId = backStackEntry.arguments?.getString("lesson_id") ?: ""
            LessonScreenWithViewModel(
                courseId,
                lessonId,
                onBack = {},
                onQuit = {},
                navHostController = navController
            )
        }
    }
}
