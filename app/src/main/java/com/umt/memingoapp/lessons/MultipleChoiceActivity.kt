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
fun MultipleChoiceContent(
    content: ActivityContent.MultipleChoice,
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
        if (content.question.isNotBlank()) {
            Text(
                text = content.question,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = LogoColor,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content.options.forEach { option ->
                val chosen = selected == option
                Surface(
                    color = when {
                        isCorrect == true && chosen -> Color(0xFF4CAF50)
                        isCorrect == false && chosen -> Color(0xFFD32F2F)
                        else -> Color.White
                    },
                    border = BorderStroke(
                        2.dp,
                        when {
                            chosen -> LogoColor
                            else -> LogoColor.copy(alpha = 0.35f)
                        }
                    ),
                    shape = RoundedCornerShape(18.dp),
                    shadowElevation = if (chosen) 4.dp else 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = isCorrect == null
                        ) {
                            selected = option
                        }
                ) {
                    Text(
                        option,
                        color = if (chosen) LogoColor else LogoColor.copy(alpha = 0.85f),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        // Check button
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isCorrect == true) "Correct!" else "Try again",
                    color = feedbackColor,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = content.explanation,
                    color = feedbackColor.copy(alpha = 0.73f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            LaunchedEffect(isChecking) {
                if (isChecking) {
                    kotlinx.coroutines.delay(1200)
                    selected = null
                    isChecking = false
                    if (isCorrect == true) onCorrect() else onWrong()
                    isCorrect = null
                }
            }

        }
    }
}