package com.umt.memingoapp.featureAr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CompleteLearningPackageCard(
    modifier: Modifier = Modifier,
    sentencesCount: Int = 5,
    tipsCount: Int = 5,
    factsCount: Int = 5,
    relatedCount: Int = 5
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(7.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF256470),  // Logo color
                            Color(0xFF5A9BA6),  // Intermediate lighter teal
                            Color(0xFFF9F9FB),  // Light gray/white
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                // Title Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 18.dp)
                ) {
                    Icon(
                        Icons.Outlined.MenuBook,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        "Complete Learning Package",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 19.sp,
                            color = Color(0xFFDCDCDE)
                        )
                    )
                }
                // Chips Grid
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        InfoChip(
                            icon = Icons.Outlined.ChatBubble,
                            text = "$sentencesCount Sentences",
                            bgColor = Color(0xFFE5F3FF),
                            iconColor = Color(0xFF2684FF),
                            textColor = Color(0xFF1A2B49)
                        )
                        InfoChip(
                            icon = Icons.Outlined.Lightbulb,
                            text = "$tipsCount Tips",
                            bgColor = Color(0xFFFFF7E5),
                            iconColor = Color(0xFFFFB300),
                            textColor = Color(0xFF1A2B49)
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        InfoChip(
                            icon = Icons.Filled.Star,
                            text = "$factsCount Facts",
                            bgColor = Color(0xFFE8FBF2),
                            iconColor = Color(0xFF22A97A),
                            textColor = Color(0xFF1A2B49)
                        )
                        InfoChip(
                            icon = Icons.Filled.Link,
                            text = "$relatedCount Related",
                            bgColor = Color(0xFFF1EEFE),
                            iconColor = Color(0xFF8852F9),
                            textColor = Color(0xFF1A2B49)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(
    icon: ImageVector,
    text: String,
    bgColor: Color,
    iconColor: Color,
    textColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(7.dp))
        Text(
            text,
            color = textColor,
            fontSize = 15.5.sp
        )
    }
}