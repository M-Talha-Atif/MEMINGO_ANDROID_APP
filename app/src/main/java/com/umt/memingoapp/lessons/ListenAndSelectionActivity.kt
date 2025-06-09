package com.umt.memingoapp.lessons

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.lessons.helpers.AnimatedHintCard
import com.umt.memingoapp.models.responses.ActivityContent
import com.umt.memingoapp.ui.theme.LogoColor
import java.util.Locale

@Composable
fun ListenAndSelectContent(
    content: ActivityContent.ListenAndSelect,
    language: Locale,
    courseTag: String,
    explanation: String,
    onCorrect: () -> Unit,
    onWrong: () -> Unit = {}
) {
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context, null) }
    DisposableEffect(Unit) {
        tts.language = language
        onDispose { tts.shutdown() }
    }

    var selected by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showHint by remember { mutableStateOf(false) }

    // Sound button pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "sound-pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sound-pulse-anim"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Text(
            content.instruction,
            style = MaterialTheme.typography.titleLarge.copy(color = LogoColor)
        )
        Spacer(Modifier.height(12.dp))

        // Sound button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            IconButton(
                onClick = {
                    tts.language = language
                    tts.speak(content.correct_answer, TextToSpeech.QUEUE_FLUSH, null, null)
                },
                modifier = Modifier
                    .size(54.dp)
                    .scale(pulse)
                    .background(LogoColor.copy(alpha = 0.18f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play Sound",
                    tint = LogoColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Result and Hint button
        if (selected != null) {
            val correct = isCorrect == true
            val animColor by animateColorAsState(
                if (correct) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                label = "result-color"
            )
            Text(
                text = if (correct) "Correct!" else "Wrong.",
                color = animColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
            )
            // Show hint button if incorrect and user hasn't requested hint yet
            AnimatedVisibility(
                visible = isCorrect == false && !showHint,
                enter = scaleIn() + fadeIn(),
                exit = fadeOut()
            ) {
                OutlinedButton(
                    onClick = { showHint = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = LogoColor
                    ),
                    border = BorderStroke(2.dp, LogoColor),
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 4.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Hint",
                        tint = LogoColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Show Hint", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        AnimatedHintCard(
            explanation = explanation,
            showHint = showHint,
            modifier = Modifier
        )


        // OPTIONS: 2 in a row if 2, 2x2 grid if 4, column otherwise
        when (content.options.size) {
            2 -> {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    content.options.forEach { opt ->
                        OptionButton(
                            text = opt,
                            isSelected = selected == opt,
                            isCorrect = isCorrect,
                            correctAnswer = content.correct_answer,
                            onClick = {
                                selected = opt
                                isCorrect = (opt == content.correct_answer)
                                if (isCorrect == true) onCorrect()
                                else onWrong()
                            },
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 80.dp)
                        )
                    }
                }
            }
            4 -> {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OptionButton(
                            text = content.options[0],
                            isSelected = selected == content.options[0],
                            isCorrect = isCorrect,
                            correctAnswer = content.correct_answer,
                            onClick = {
                                selected = content.options[0]
                                isCorrect = (content.options[0] == content.correct_answer)
                                if (isCorrect == true) onCorrect()
                                else onWrong()
                            },
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 80.dp)
                        )
                        OptionButton(
                            text = content.options[1],
                            isSelected = selected == content.options[1],
                            isCorrect = isCorrect,
                            correctAnswer = content.correct_answer,
                            onClick = {
                                selected = content.options[1]
                                isCorrect = (content.options[1] == content.correct_answer)
                                if (isCorrect == true) onCorrect()
                                else onWrong()
                            },
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 80.dp)
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OptionButton(
                            text = content.options[2],
                            isSelected = selected == content.options[2],
                            isCorrect = isCorrect,
                            correctAnswer = content.correct_answer,
                            onClick = {
                                selected = content.options[2]
                                isCorrect = (content.options[2] == content.correct_answer)
                                if (isCorrect == true) onCorrect()
                                else onWrong()
                            },
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 80.dp)
                        )
                        OptionButton(
                            text = content.options[3],
                            isSelected = selected == content.options[3],
                            isCorrect = isCorrect,
                            correctAnswer = content.correct_answer,
                            onClick = {
                                selected = content.options[3]
                                isCorrect = (content.options[3] == content.correct_answer)
                                if (isCorrect == true) onCorrect()
                                else onWrong()
                            },
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 80.dp)
                        )
                    }
                }
            }
            else -> {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    content.options.forEach { opt ->
                        OptionButton(
                            text = opt,
                            isSelected = selected == opt,
                            isCorrect = isCorrect,
                            correctAnswer = content.correct_answer,
                            onClick = {
                                selected = opt
                                isCorrect = (opt == content.correct_answer)
                                if (isCorrect == true) onCorrect()
                                else onWrong()
                            },
                            modifier = Modifier.weight(1f).defaultMinSize(minWidth = 80.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    correctAnswer: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when {
        !isSelected -> LogoColor
        isSelected && isCorrect == true -> Color(0xFF4CAF50)
        isSelected && isCorrect == false -> Color(0xFFD32F2F)
        else -> LogoColor
    }
    val elevation by animateDpAsState(
        if (isSelected) 10.dp else 2.dp,
        label = "button-elevation"
    )
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        modifier = modifier

    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
        )
    }
}
