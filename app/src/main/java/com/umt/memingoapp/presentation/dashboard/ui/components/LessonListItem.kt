package com.umt.memingoapp.presentation.dashboard.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.dashboard.ui.screen.Lesson
import com.umt.memingoapp.presentation.shared_components.AppTypography

@Composable
fun LessonListItem(lesson: Lesson, onItemClick: () -> Unit) {
    val isUnlocked = !lesson.locked

    val iconColor = when {
        lesson.locked -> Color.Gray
        lesson.score >= 80 -> Color(0xFF4CAF50)
        lesson.score >= 50 -> Color(0xFFFFA000)
        else -> Color(0xFFD32F2F)
    }

    val iconBgColor = iconColor.copy(alpha = 0.2f)

    val cardBorder = BorderStroke(1.dp, iconColor.copy(alpha = 0.4f))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .alpha(if (lesson.locked) 0.6f else 1f),
        shape = RoundedCornerShape(12.dp),
        border = cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { if (isUnlocked) onItemClick() },
        enabled = isUnlocked
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (lesson.locked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 6.dp)
                        )
                    }
                    Text(
                        text = lesson.title,
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = lesson.type,
                    style = AppTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (isUnlocked) {
                    Text(
                        text = "Quiz: ${lesson.score}%",
                        style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = iconColor,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(iconBgColor, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        lesson.locked -> Icons.Default.Lock
                        lesson.score >= 100 -> Icons.Default.Check
                        else -> Icons.Default.PlayArrow
                    },
                    contentDescription = "Status Icon",
                    tint = iconColor
                )
            }
        }
    }
}