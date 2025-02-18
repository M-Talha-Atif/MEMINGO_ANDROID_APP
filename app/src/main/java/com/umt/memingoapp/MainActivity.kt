package com.umt.memingoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.umt.memingoapp.ui.theme.MemingoAppTheme
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.navigation.MyNavHost
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemingoAppTheme {
                val navController = rememberNavController()
                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    SplashScreen(onSplashEnd = { showSplash = false })
                } else {
//                    MainScreen()
//                    GetStartedScreen()
                    MyNavHost(navController = navController)
                }

            }
        }
    }
}


@Composable
fun MainScreen() {
    // Sample data for a list of items
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Welcome to Memingo!",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Displaying a list of items
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(8.dp)
                )
            }
        }


        Button(
            onClick = { /* Add action here */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Get Started")
        }
    }
}



@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000) // Delay to show splash screen for 2 seconds
        onSplashEnd()
    }

    // Define scale animation for zoom effect
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f, // Start slightly smaller and grow to full size
        animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing)
//        animationSpec = spring( dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessMedium
//        )

    )

    // Center icon with fade-in and scale effect
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(initialAlpha = 0.5f) + scaleIn(initialScale = 0.6f),
            exit =  fadeOut( animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing))
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
