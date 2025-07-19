package com.umt.memingoapp.presentation.lesson.components.helpers


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.presentation.theme.LogoColor
import kotlinx.coroutines.delay


@Composable
fun AnimatedHintCard(
    explanation: String,
    showHint: Boolean,
    modifier: Modifier = Modifier
) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(showHint, explanation) {
        if (showHint) {
            visibleText = ""
            explanation.forEach { c ->
                delay(20)
                visibleText += c
            }
        }
    }

    AnimatedVisibility(
        visible = showHint,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = LogoColor.copy(alpha = 0.12f))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Hint",
                        tint = LogoColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Hint",
                        style = MaterialTheme.typography.labelMedium.copy(color = LogoColor)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = visibleText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LogoColor,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    overflow = TextOverflow.Visible,
                    softWrap = true,
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}

