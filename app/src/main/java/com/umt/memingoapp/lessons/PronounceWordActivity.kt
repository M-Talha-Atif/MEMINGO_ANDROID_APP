package com.umt.memingoapp.lessons

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.models.responses.PronunciationWord
import com.umt.memingoapp.ui.theme.LogoColor
import java.util.*

@Composable
fun PronunciationPracticeContent(
    word: PronunciationWord,
    language: Locale,
    courseTag : String,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context, null) }
    DisposableEffect(Unit) {
        tts.language = language
        onDispose { tts.shutdown() }
    }

    var micResult by remember { mutableStateOf<String?>(null) }
    var showMicFeedback by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        micResult = spokenText
        showMicFeedback = true
        isChecking = true
    }

    // Listen button pulse animation
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

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Say: ${word.word} (${word.phonetic})",
            style = MaterialTheme.typography.titleLarge.copy(color = LogoColor)
        )
        Text(
            text = "Meaning: ${word.meaning}",
            style = MaterialTheme.typography.bodyMedium.copy(color = LogoColor.copy(alpha = 0.8f))
        )
        Spacer(Modifier.height(20.dp))

        // Listen (TTS) button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = {
                    tts.language = language
                    tts.speak(word.word, TextToSpeech.QUEUE_FLUSH, null, null)
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

        // Speak Button
        Button(
            onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, courseTag)
                }
                launcher.launch(intent)
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Speak", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Speak", color = Color.White)
        }

        // Feedback
        AnimatedVisibility(visible = showMicFeedback && micResult != null) {
            val isCorrect = micResult?.let { spoken ->
                // In real app, convert spoken to phonetic and compare; for now, compare spoken text to phonetic or word
                spoken.equals(word.phonetic, ignoreCase = true) || spoken.equals(word.word, ignoreCase = true)
            } ?: false
            val animColor by animateColorAsState(
                if (isCorrect) Color(0xFF4CAF50) else Color(0xFFD32F2F), label = "result-color"
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "You said: ${micResult.orEmpty()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (isCorrect) "Correct!" else "Try again",
                    color = animColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 6.dp)
                )
                LaunchedEffect(isChecking) {

                    if (isChecking) {
                        kotlinx.coroutines.delay(1200)
                        // 1. Reset state BEFORE calling onCorrect/onWrong!
                        micResult = null
                        showMicFeedback = false
                        isChecking = false
                        // 2. Now call the callback
                        if (isCorrect) {
                            onCorrect()
                        }

                        else {
                            onWrong()
                        }
                    }
                }
            }
        }
    }
}