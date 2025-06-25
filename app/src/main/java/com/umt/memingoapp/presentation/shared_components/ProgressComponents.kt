package com.umt.memingoapp.presentation.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun showProgress(score: Int = 50) {
    val logoColor = Color(0xFF256470)
    val gradient = Brush.linearGradient(
        listOf(
            logoColor,
            logoColor.copy(alpha = 0.7f) // Lighter variation of logo color
        )
    )

    val progressFactor by remember(score) {
        mutableFloatStateOf(score * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(40.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(logoColor, logoColor)
                ),
                shape = RoundedCornerShape(50.dp)
            )
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White), // Screen background white
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            contentPadding = PaddingValues(3.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            // Percentage Box with Rounded Corners
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)) // Rounded corners around text
                    .background(Color.Black.copy(alpha = 0.6f)) // Dark background for contrast
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = "${score}%", // Showing score in percentage format
                    color = Color.White, // Text color for visibility
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start=4.dp)
                )
            }
        }
    }
}
