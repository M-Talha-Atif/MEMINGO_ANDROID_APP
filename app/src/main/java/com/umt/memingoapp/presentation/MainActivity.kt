package com.umt.memingoapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.umt.memingoapp.presentation.theme.MemingoAppTheme
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.navigation.MyNavHost
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.R
import com.umt.memingoapp.presentation.navigation.routes.Screen
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MemingoAppTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false // This will ensure your LogoColor is used
            ) {
                MyNavHost(navController = navController)
            }
        }
    }
}


fun isTokenExpired(token: String): Boolean {
    return try {
        val parts = token.split(".")
        if (parts.size != 3) return true

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), Charsets.UTF_8)
        val jsonObject = JSONObject(payload)
        val exp = jsonObject.getLong("exp")
        val currentTime = System.currentTimeMillis() / 1000 // in seconds

        exp < currentTime
    } catch (e: Exception) {
        true // If parsing fails, assume token is invalid
    }
}



@Composable
fun SplashScreen(onSplashEnd: () -> Unit, navController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    val token = sharedPrefs.getString("token", null)
    val isFirstTime = sharedPrefs.getBoolean("is_first_time", true)

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000)

        val isTokenValid = !token.isNullOrEmpty() && !isTokenExpired(token)

        val nextRoute = when {
            isFirstTime -> {
                sharedPrefs.edit().putBoolean("is_first_time", false).apply()
                Screen.GetStarted.route
            }
            isTokenValid -> Screen.HomeScreen.route
            else -> Screen.Login.route
        }

        navController.navigate(nextRoute) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }

        onSplashEnd()
    }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000)
    )

     Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // use theme background
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(initialAlpha = 0.5f) + scaleIn(initialScale = 0.6f),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000))
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .scale(scale)
            )
        }
    }
}

