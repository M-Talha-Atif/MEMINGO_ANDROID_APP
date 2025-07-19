package com.umt.memingoapp.presentation.dashboard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.domain.model.response.Topic
import com.umt.memingoapp.presentation.shared_components.AppTypography
import com.umt.memingoapp.presentation.theme.LogoColor

@Composable
fun LanguageProgressCard(language: String = "German",
                                 level: String = "Intermediate",
                                 progress: Float = 0.65f,
                                 topics: List<Topic> = listOf(
                                     Topic("1", "Alphabet", 100.0),
                                     Topic("2", "Words", 83.0),
                                     Topic("3", "Sentences", 72.0)
                                 )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LogoColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = language,
                    style = AppTypography.titleMedium
                )
                Text(
                    text = level,
                    style = AppTypography.bodyMedium,
                    color = LogoColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = LogoColor,
                trackColor = LogoColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                topics.take(3).forEach {
                    ProgressItem(it.name, "${it.progress.toInt()}%")
                }
            }
        }
    }
}



@Composable
fun ProgressItem(title: String, progress: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = AppTypography.labelSmall
        )
        Text(
            text = progress,
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}