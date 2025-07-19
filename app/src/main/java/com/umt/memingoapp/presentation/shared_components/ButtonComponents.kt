package com.umt.memingoapp.presentation.shared_components


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.theme.LogoColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MemingoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = LogoColor,
    textColor: Color = LogoColor,
    borderColors: List<Color> = listOf(LogoColor, LogoColor.copy(alpha = 0.6f))
) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedBorder = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutLinearInEasing)
        )
    )

    val brush = Brush.linearGradient(
        colors = borderColors,
        start = Offset(0f, 0f),
        end = Offset(
            x = cos(animatedBorder.value * PI / 180f).toFloat() * 400f,
            y = sin(animatedBorder.value * PI / 180f).toFloat() * 400f
        )
    )

    Box(
        modifier = modifier
            .border(2.dp, brush, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
