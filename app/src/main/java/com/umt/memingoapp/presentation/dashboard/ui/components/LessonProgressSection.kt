package com.umt.memingoapp.presentation.dashboard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.shared_components.AppTypography
import com.umt.memingoapp.presentation.theme.LogoColor


@Composable
fun LessonProgressSection(completed: Int, total: Int, modifier: Modifier = Modifier) {
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
                    text = "$completed of $total completed",
                    style = AppTypography.bodyMedium
                )
            }

            CircularProgressIndicator(
                progress = completed.toFloat() / total.toFloat(),
                modifier = Modifier.size(48.dp),
                color = LogoColor,
                strokeWidth = 6.dp,
                trackColor = LogoColor.copy(alpha = 0.2f)
            )
        }
    }
}