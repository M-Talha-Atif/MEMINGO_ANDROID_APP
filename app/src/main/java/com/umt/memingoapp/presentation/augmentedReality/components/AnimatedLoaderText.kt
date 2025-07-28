package com.umt.memingoapp.presentation.augmentedReality.components


import androidx.compose.animation.core.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.presentation.theme.LogoColor

@Composable
fun AnimatedLoadingText(wordName: String) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animate alpha for fade-in/out
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Animate dots at the end like typing (...)
    val dotCount by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = 3,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Text(
        text = "Loading content for $wordName" + ".".repeat(dotCount),
        color = LogoColor,
        fontSize = 18.sp,
        modifier = Modifier.alpha(alpha)
    )
}
