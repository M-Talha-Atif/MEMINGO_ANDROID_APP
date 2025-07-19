package com.umt.memingoapp.presentation.leaderboard.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.leaderboard.state.TimeFilterType
import com.umt.memingoapp.presentation.theme.LogoColor

@Composable
fun TimeFilterSelector(
    selected: TimeFilterType,
    onSelect: (TimeFilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = TimeFilterType.values()
    val selectedIndex = filters.indexOf(selected)

    BoxWithConstraints(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.10f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = LogoColor,
                shape = RoundedCornerShape(24.dp)
            )
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.White.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
    ) {
        val tabWidth = maxWidth / filters.size
        val indicatorOffset by animateDpAsState(
            targetValue = tabWidth * selectedIndex,
            label = "IndicatorOffset"
        )

        // Animated selection indicator
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .padding(4.dp)
                .width(tabWidth - 8.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(LogoColor.copy(alpha = 0.8f), LogoColor)
                    )
                )
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = LogoColor.copy(alpha = 0.3f)
                )
        )

        // Tabs row
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            filters.forEach { filter ->
                val isSelected = selected == filter
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSelect(filter) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter.label,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}
