package com.umt.memingoapp.presentation.lesson.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.domain.model.response.ActivityContent
import com.umt.memingoapp.presentation.theme.LogoColor


@Preview(showBackground = true)
@Composable
fun PreviewTypeTranslationContent() {
    val sampleContent = ActivityContent.TypeTranslation(
        correct_answer = "Ich liebe dich",
        alternative_answers = listOf("Ich hab dich lieb", "Ich mag dich"),
        english_text = "I love you",
        hints = listOf("Ich", "liebe", "dich"),
        instruction = "Type the correct German sentence"
    )

    MaterialTheme {
        TypeTranslationContent(
            content = sampleContent,
            onCorrect = {},
            onWrong = {}
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TypeTranslationContent(
    content: ActivityContent.TypeTranslation,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    val answerNormalized = userInput.text.trim().lowercase()
    val correctAnswers = remember(content) {
        buildSet {
            add(content.correct_answer.trim().lowercase())
            content.alternative_answers?.forEach { add(it.trim().lowercase()) }
        }
    }

    val feedbackColor by animateColorAsState(
        when (isCorrect) {
            true -> Color(0xFF4CAF50)
            false -> Color(0xFFD32F2F)
            else -> LogoColor
        },
        label = "feedback-color"
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
        Text(
            text = content.english_text,
            style = MaterialTheme.typography.bodyLarge.copy(color = LogoColor.copy(alpha = 0.8f)),
            modifier = Modifier.padding(bottom = 18.dp, top = 8.dp)
        )

        // Hints as chips (Duolingo style)
        if (!content.hints.isNullOrEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                content.hints.forEach { hint ->
                    Surface(
                        color = LogoColor.copy(alpha = 0.09f),
                        border = BorderStroke(1.dp, LogoColor),
                        shape = RoundedCornerShape(20.dp),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .clickable {
                                userInput = TextFieldValue(
                                    if (userInput.text.isEmpty()) hint
                                    else userInput.text.trimEnd() + " " + hint
                                )
                            }
                    ) {
                        Text(
                            hint,
                            color = LogoColor,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        // Input field
        OutlinedTextField(
            value = userInput,
            onValueChange = {
                if (!showFeedback) userInput = it
            },
            label = { Text("Type in German") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LogoColor,
                unfocusedBorderColor = LogoColor.copy(alpha = 0.3f),
                cursorColor = LogoColor
            ),
            enabled = !showFeedback
        )

        // Buttons
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (userInput.text.isNotEmpty() && !showFeedback) {
                IconButton(
                    onClick = { userInput = TextFieldValue("") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(LogoColor.copy(alpha = 0.10f), shape = RoundedCornerShape(18.dp))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = LogoColor)
                }
                Spacer(Modifier.width(24.dp))
            }
            Button(
                onClick = {
                    showFeedback = true
                    isCorrect = answerNormalized in correctAnswers
                    isChecking = true
                },
                enabled = userInput.text.trim().isNotEmpty() && !showFeedback,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Check", color = Color.White)
            }
        }

        // Feedback
        AnimatedVisibility(visible = showFeedback && isCorrect != null, modifier = Modifier.padding(top = 20.dp)) {
            val correct = isCorrect == true
            Text(
                text = if (correct) "Correct!" else "Try again",
                color = feedbackColor,
                style = MaterialTheme.typography.titleLarge
            )
            LaunchedEffect(isChecking) {
                if (isChecking) {
                    kotlinx.coroutines.delay(1200)
                    // Reset state before advancing!
                    showFeedback = false
                    isCorrect = null
                    userInput = TextFieldValue("")
                    isChecking = false
                    if (correct) {
                        onCorrect()
                    } else {
                        onWrong()
                    }
                }
            }
        }
    }
}