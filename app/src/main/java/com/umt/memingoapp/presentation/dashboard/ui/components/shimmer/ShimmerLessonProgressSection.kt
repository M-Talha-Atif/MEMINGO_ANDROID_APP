package com.umt.memingoapp.presentation.dashboard.ui.components.shimmer


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import com.valentinilk.shimmer.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.umt.memingoapp.presentation.shared_components.AppTypography
import com.umt.memingoapp.presentation.theme.LogoColor
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@Composable
fun ShimmerLessonProgressSection(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View, // or ShimmerBounds.Window
        theme = ShimmerTheme(
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            blendMode = DrawScope.DefaultBlendMode,
            rotation = 20f,
            shaderColors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.Gray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            shaderColorStops = listOf(
                0.0f,
                0.5f,
                1.0f
            ),
            shimmerWidth = 250.dp
        )
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LogoColor.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Lessons Progress",
                    style = AppTypography.titleMedium
                )
                Text(
                    text = "Loading...", // fallback
                    style = AppTypography.bodyMedium
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .shimmer(shimmer)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}
