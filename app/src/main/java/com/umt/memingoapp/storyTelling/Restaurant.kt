package com.umt.memingoapp.storyTelling

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.SignUpForm
import kotlin.random.Random




@Preview
@Composable
fun RestaurantBackgroundPreview(){
    RestaurantBackground()
}


@Composable
fun RestaurantBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Wall color/texture
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5D4037), // Dark brown (wood paneling)
                            Color(0xFF8D6E63)  // Lighter brown
                        )
                    )
                )
        )

        // Decorative elements
        Column(modifier = Modifier.fillMaxWidth()) {
            // Window with outside view
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFF263238))
                    .border(2.dp, Color(0xFFBCAAA4), RectangleShape)
            ) {
                // Night city view (simplified)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF0D47A1), Color.Black),
                                center = Offset(0.5f, 0.2f),
                                radius = 1.5f
                            )
                        )
                ) {
                    repeat(20) {
                        Box(
                            modifier = Modifier
                                .offset(x = Random.nextFloat().dp * 360, y = Random.nextFloat().dp * 100)
                                .size(2.dp, 4.dp)
                                .background(Color.Yellow)
                        )
                    }
                }
            }

            // Wall decorations
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = Color(0xFF3E2723),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(1.dp, Color(0xFFD7CCC8), RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        // Floor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF795548),
                            Color(0xFFA1887F),
                            Color(0xFF795548)
                        )
                    )
                )
                .clip(RectangleShape)
        ) {
            // Wooden floor pattern
            Canvas(modifier = Modifier.fillMaxSize()) {
                val tileWidth = size.width / 10
                for (i in 0..10) {
                    drawLine(
                        start = Offset(x = i * tileWidth, y = 0f),
                        end = Offset(x = i * tileWidth, y = size.height),
                        color = Color(0x4D000000),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
        }
    }
}