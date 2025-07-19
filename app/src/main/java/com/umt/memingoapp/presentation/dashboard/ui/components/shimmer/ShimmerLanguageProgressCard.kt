package com.umt.memingoapp.presentation.dashboard.ui.components.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.rememberShimmer
import androidx.compose.animation.core.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.ShimmerBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.BlendMode


@Composable
fun ShimmerLanguageProgressCard() {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = ShimmerTheme(
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            rotation = 20f,
            shimmerWidth = 200.dp,
            shaderColors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.White.copy(alpha = 0.9f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            blendMode = BlendMode.SrcOver,
            shaderColorStops = listOf(
                0.0f,
                0.5f,
                1.0f
            )
        )
    )

    val baseColor = Color.LightGray.copy(alpha = 0.2f)
    val highlightColor = Color.LightGray.copy(alpha = 0.4f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .shimmer(shimmer),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = baseColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(highlightColor)
                )
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(highlightColor)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(highlightColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(3) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .height(14.dp)
                                .width(50.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(highlightColor)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .height(12.dp)
                                .width(40.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(highlightColor)
                        )
                    }
                }
            }
        }
    }
}

