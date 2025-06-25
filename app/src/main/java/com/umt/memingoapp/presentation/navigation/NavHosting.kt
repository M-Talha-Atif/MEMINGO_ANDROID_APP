package com.umt.memingoapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umt.memingoapp.presentation.objectDetection.ui.DetectionScreen
import com.umt.memingoapp.presentation.dashboard.ui.CourseHomeScreen
import com.umt.memingoapp.presentation.course_selection.ui.CourseSelectionScreen
import com.umt.memingoapp.presentation.splash.ui.GetStartedScreen
import com.umt.memingoapp.SplashScreen
import com.umt.memingoapp.presentation.auth.ui.LoginForm
import com.umt.memingoapp.presentation.auth.ui.OtpScreen
import com.umt.memingoapp.presentation.auth.ui.SignUpForm
import com.umt.memingoapp.presentation.augmentedReality.ui.ARLearningScreen
import com.umt.memingoapp.presentation.lesson.ui.LessonScreenWithViewModel
import com.umt.memingoapp.presentation.navigation.routes.Screen

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(navController = navController, onSplashEnd = {})
        }

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

        composable("${Screen.VocabularyScreen.route}/{course_id}/{courseName}/{word}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseName") ?: ""
            val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
            val word = backStackEntry.arguments?.getString("word") ?: ""
            ARLearningScreen(
                moduleTitle =  "Learn about $word",
                courseId = courseId,
                language = courseName,
                wordName = word,
                navController = navController,
                onClose = {},
                onScan = {}
            )
        }

        composable("${Screen.ARScreen.route}/{courseName}/{courseId}") {
                backStackEntry ->


            DetectionScreen(
                navController = navController,
                courseId = backStackEntry.arguments?.getString("courseId") ?: "",
                courseName = backStackEntry.arguments?.getString("courseName") ?: ""
            )
        }
    }

    }

