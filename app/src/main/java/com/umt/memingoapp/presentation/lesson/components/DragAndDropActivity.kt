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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.domain.model.response.ActivityContent
import com.umt.memingoapp.presentation.theme.LogoColor

@Preview(showBackground = true)
@Composable
fun PreviewDragAndDropSentenceContent() {
    val sampleContent = ActivityContent.DragAndDrop(
        correct_order = listOf("Ich", "liebe", "dich"),
        instruction = "Rearrange the sentence correctly",
        translation = "I love you",
        words = listOf("liebe", "Ich", "dich")
    )

    MaterialTheme {
        DragAndDropSentenceContent(
            content = sampleContent,
            onCorrect = {},
            onWrong = {}
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DragAndDropSentenceContent(
    content: ActivityContent.DragAndDrop,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    var arrangedWords by remember { mutableStateOf(content.words.shuffled()) }
    var selectedIdx by remember { mutableStateOf<Int?>(null) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var isChecking by remember { mutableStateOf(false) }

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
            text = content.translation,
            style = MaterialTheme.typography.bodyMedium.copy(color = LogoColor.copy(alpha = 0.7f)),
            modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
        )

        // The word tiles (Duolingo style, swap on tap)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            arrangedWords.forEachIndexed { index, word ->
                val isSelected = selectedIdx == index
                Surface(
                    color = if (isSelected) LogoColor.copy(alpha = 0.22f) else Color.White,
                    border = BorderStroke(2.dp, if (isSelected) LogoColor else LogoColor.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(18.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 44.dp)
                        .clickable(enabled = !showFeedback) {
                            if (selectedIdx == null) {
                                selectedIdx = index
                            } else if (selectedIdx != index) {
                                // Swap
                                arrangedWords = arrangedWords.toMutableList().apply {
                                    val temp = this[selectedIdx!!]
                                    this[selectedIdx!!] = this[index]
                                    this[index] = temp
                                }
                                selectedIdx = null
                            } else {
                                selectedIdx = null
                            }
                        }
                ) {
                    Text(
                        word,
                        color = LogoColor,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        // Controls: Reset and Check
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    arrangedWords = content.words.shuffled()
                    selectedIdx = null
                    showFeedback = false
                    isCorrect = null
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(LogoColor.copy(alpha = 0.10f), shape = RoundedCornerShape(18.dp))
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = LogoColor)
            }
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = {
                    showFeedback = true
                    isCorrect = arrangedWords == content.correct_order
                    isChecking = true
                },
                enabled = !showFeedback,
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
                    selectedIdx = null
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