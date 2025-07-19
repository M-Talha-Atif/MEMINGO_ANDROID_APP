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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer



@Composable
fun ShimmerLessonListItem() {
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .shimmer(shimmer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .padding(end = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth(0.5f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(0.4f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray.copy(alpha = 0.4f))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}
