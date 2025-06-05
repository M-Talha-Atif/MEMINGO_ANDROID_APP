package com.umt.memingoapp.lessons.helpers

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.uiComponents.AppTypography
import kotlinx.coroutines.flow.StateFlow


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun BackConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val slideIn = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        slideIn.animateTo(1f, animationSpec = tween(300))
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = modifier
                .scale(slideIn.value.coerceIn(0.8f, 1f))
                .alpha(slideIn.value)
                .border(
                    width = 2.dp,
                    color = LogoColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Exit Lesson?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = LogoColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your progress will be saved, but you'll need to restart from the beginning next time.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, LogoColor.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Continue Learning", color = LogoColor)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onConfirmExit,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LogoColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Exit Lesson")
                    }
                }
            }
        }
    }
}



@Composable
private fun LessonFailedDialog(onDismiss: () -> Unit, onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Lesson Failed", style = AppTypography.titleLarge)
        },
        text = {
            Text(text = "You've made too many mistakes. Would you like to try again?",
                style = AppTypography.bodyLarge)
        },
        confirmButton = {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Text(text = "Try Again")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Exit")
            }
        }
    )
}


@Composable
private fun LessonCompletedDialog(score: StateFlow<Int>, onContinue: () -> Unit) {
    AlertDialog(
        onDismissRequest = onContinue,
        title = {
            Text(text = "Lesson Complete!", style = AppTypography.titleLarge)
        },
        text = {
            Column {
                Text(text = "Congratulations!", style = AppTypography.bodyLarge)
                Text(text = "Your score: $score", style = AppTypography.bodyLarge)
            }
        },
        confirmButton = {
            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Text(text = "Continue")
            }
        }
    )
}