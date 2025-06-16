package com.umt.memingoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mlr_apps.objectdetection.ui.views.DetectionScreen
import com.umt.memingoapp.CourseHomeScreen
import com.umt.memingoapp.CourseSelectionScreen
import com.umt.memingoapp.GetStartedScreen
import com.umt.memingoapp.auth.LoginForm
import com.umt.memingoapp.auth.OtpScreen
import com.umt.memingoapp.auth.SignUpForm
import com.umt.memingoapp.featureObjectDetection.YuvToRgbConverter
import com.umt.memingoapp.lessons.LessonScreenWithViewModel
import loadLabels
import loadModel
import org.tensorflow.lite.Interpreter
import java.util.concurrent.Executors

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

        composable("ar_scan") {
            // Only for demonstration: You should ideally provide these from a shared ViewModel or singleton!
            val context = LocalContext.current
            val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
            val interpreter = remember { Interpreter(loadModel(context)) }
            val labels = remember { loadLabels(context) }
            val yuvToRgbConverter = remember { YuvToRgbConverter(context) }

            DisposableEffect(Unit) {
                onDispose {
                    cameraExecutor.shutdown()
                    interpreter.close()
                }
            }

            DetectionScreen(
                cameraExecutor = cameraExecutor,
                yuvToRgbConverter = yuvToRgbConverter,
                interpreter = interpreter,
                labels = labels
            )
        }
    }
    }

