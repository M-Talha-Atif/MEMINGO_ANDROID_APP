package com.umt.memingoapp.lessons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.models.responses.ActivityContent
import com.umt.memingoapp.ui.theme.LogoColor

@Composable
fun FillInBlankContent(
    content: ActivityContent.FillInBlank,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    var selected by remember { mutableStateOf<String?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val feedbackColor by animateColorAsState(
        when (isCorrect) {
            true -> Color(0xFF4CAF50)
            false -> Color(0xFFD32F2F)
            else -> LogoColor
        }, label = "feedback-color"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = content.instruction,
            style = MaterialTheme.typography.titleLarge.copy(color = LogoColor)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = content.translation,
            style = MaterialTheme.typography.bodyMedium.copy(color = LogoColor.copy(alpha = 0.7f)),
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // Sentence with blank
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            val parts = content.sentence.split("___")
            Text(parts[0], style = MaterialTheme.typography.titleLarge)
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .border(
                        width = 2.dp,
                        color = if (selected != null) LogoColor else LogoColor.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = if (selected != null) LogoColor.copy(alpha = 0.08f) else Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = selected ?: "___",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = LogoColor)
                )
            }
            if (parts.size > 1) {
                Text(parts[1], style = MaterialTheme.typography.titleLarge)
            }
        }

        // Option chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        ) {
            content.options.forEach { option ->
                Surface(
                    color = if (option == selected) LogoColor else Color.White,
                    border = BorderStroke(2.dp, LogoColor),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 44.dp)
                        .clickable(enabled = isCorrect == null) { selected = option }
                ) {
                    Text(
                        text = option,
                        color = if (option == selected) Color.White else LogoColor,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                    )
                }
            }
        }

        // Check Button
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    isCorrect = selected == content.correct_answer
                    isChecking = true
                },
                enabled = selected != null && isCorrect == null,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Check", color = Color.White)
            }
        }

        // Feedback
        AnimatedVisibility(visible = isCorrect != null, modifier = Modifier.padding(top = 20.dp)) {
            val correct = isCorrect == true
            Text(
                text = if (correct) "Correct!" else "Try again",
                color = feedbackColor,
                style = MaterialTheme.typography.titleLarge
            )
            LaunchedEffect(isChecking) {
                if (isChecking) {
                    kotlinx.coroutines.delay(1200)
                    selected = null
                    isCorrect = null
                    isChecking = false
                    if (correct) onCorrect() else onWrong()
                }
            }
        }
    }
}