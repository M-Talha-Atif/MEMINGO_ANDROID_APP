package com.umt.memingoapp.presentation.lesson.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.domain.model.response.ActivityContent
import com.umt.memingoapp.presentation.theme.LogoColor
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun PreviewSentenceBuilderContent() {
    val sampleContent = ActivityContent.SentenceBuilder(
        correct_order = listOf("Ich", "liebe", "dich"),
        instruction = "Arrange the sentence correctly",
        translation = "I love you",
        words = listOf("liebe", "Ich", "dich")
    )

    MaterialTheme {
        SentenceBuilderContent(
            content = sampleContent,
            onCorrect = {},
            onWrong = {}
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderContent(
    content: ActivityContent.SentenceBuilder,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    var selectedWords by remember { mutableStateOf(emptyList<String>()) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    val scrollState = rememberScrollState()

    val remainingWords = content.words - selectedWords.toSet()

    val feedbackColor by animateColorAsState(
        when (isCorrect) {
            true -> Color(0xFF4CAF50) // Green for correct
            false -> Color(0xFFD32F2F) // Red for incorrect
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
        // Instruction and translation
        Text(
            text = content.instruction,
            style = MaterialTheme.typography.titleLarge,
            color = LogoColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content.translation,
            style = MaterialTheme.typography.bodyMedium,
            color = LogoColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Selected words row with horizontal scrolling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedWords.forEachIndexed { idx, word ->
                    Surface(
                        color = LogoColor,
                        shape = RoundedCornerShape(18.dp),
                        tonalElevation = 4.dp,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 46.dp)
                            .clickable {
                                selectedWords = selectedWords.toMutableList().apply { removeAt(idx) }
                            }
                    ) {
                        Text(
                            text = word,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        // Available words in FlowRow
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            remainingWords.forEach { word ->
                Surface(
                    tonalElevation = 2.dp,
                    color = Color.White,
                    border = BorderStroke(1.5.dp, LogoColor),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 46.dp)
                        .clickable {
                            selectedWords = selectedWords + word
                        }
                ) {
                    Text(
                        text = word,
                        color = LogoColor,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (selectedWords.isNotEmpty()) {
                IconButton(
                    onClick = {
                        selectedWords = emptyList()
                        showFeedback = false
                        isCorrect = null
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(LogoColor.copy(alpha = 0.1f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = LogoColor
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
            }

            Button(
                onClick = {
                    showFeedback = true
                    isCorrect = selectedWords == content.correct_order
                },
                enabled = selectedWords.isNotEmpty(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoColor,
                    disabledContainerColor = LogoColor.copy(alpha = 0.3f)
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Check",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Feedback message
        AnimatedVisibility(
            visible = showFeedback && isCorrect != null,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            val correct = isCorrect == true
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (correct) "Correct! 🎉" else "Try again",
                    color = feedbackColor,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                if (!correct) {
                    Text(
                        text = "Correct order: ${content.correct_order.joinToString(" ")}",
                        color = LogoColor.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LaunchedEffect(correct) {
                delay(1200)
                showFeedback = false
                isCorrect = null
                if (correct) {
                    onCorrect()
                    selectedWords = emptyList()
                } else {
                    onWrong()
                }
            }
        }
    }
}

