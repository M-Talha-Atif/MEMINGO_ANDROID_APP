// AlphabetLessonScreen.kt
package com.umt.memingoapp.lessons

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.uiComponents.AppTypography
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.viewmodels.AlphabetLessonViewModel
import kotlinx.coroutines.flow.StateFlow


@Preview(showBackground = true)
@Composable
fun PreviewLessonScreen() {
    val mockNavController = rememberNavController()
    AlphabetLessonScreen(mockNavController )
}


@Composable
fun AlphabetLessonScreen(navController: NavController) {
    val viewModel = remember { AlphabetLessonViewModel() }
    val showFailureDialog by viewModel.showFailureDialog.collectAsState()
    val isLessonComplete by viewModel.isLessonComplete.collectAsState()

    when {
        showFailureDialog -> {
            LessonFailedDialog(
                onDismiss = { navController.popBackStack() },
                onRetry = { viewModel.resetLesson() }
            )
        }
        isLessonComplete -> {
            LessonCompletedDialog(
                score = viewModel.score,
                onContinue = { navController.popBackStack() }
            )
        }
        else -> {
            LessonContent(viewModel)
        }
    }
}


@Composable
private fun LessonContent(viewModel: AlphabetLessonViewModel) {
    val currentAlphabetIndex by viewModel.currentAlphabetIndex.collectAsState()
    val remainingLives by viewModel.remainingLives.collectAsState()
    val score by viewModel.score.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val userSpeechResult by viewModel.userSpeechResult.collectAsState()
    val isUserCorrect by viewModel.isUserCorrect.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header with progress and lives
            LessonHeader(
                currentIndex = currentAlphabetIndex,
                totalItems = viewModel.totalAlphabets,
                remainingLives = remainingLives,
                score = score
            )

            // Current alphabet display
            CurrentAlphabetDisplay(
                alphabet = viewModel.currentAlphabet,
                onSpeakClick = { viewModel.playAlphabetSound() }
            )

            // Mic button and feedback
            SpeechRecognitionSection(
                isListening = isListening,
                userSpeechResult = userSpeechResult,
                isCorrect = isUserCorrect,
                onMicClick = { viewModel.startSpeechRecognition() }
            )
        }
    }
}


@Composable
private fun LessonHeader(
    currentIndex: Int,
    totalItems: Int,
    remainingLives: Int,
    score: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = currentIndex.toFloat() / totalItems.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = LogoColor,
            trackColor = LogoColor.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lives display
            Row {
                repeat(remainingLives) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Life",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Score display
            Text(
                text = "Score: $score",
                style = AppTypography.titleMedium
            )
        }
    }
}

@Composable
private fun CurrentAlphabetDisplay(alphabet: GermanAlphabet, onSpeakClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Alphabet character
        Text(
            text = alphabet.uppercase,
            style = AppTypography.displayLarge.copy(fontSize = 120.sp)
        )

        // Alphabet name
        Text(
            text = alphabet.name,
            style = AppTypography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Speak button
        IconButton(
            onClick = onSpeakClick,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Speak",
                tint = LogoColor,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun SpeechRecognitionSection(
    isListening: Boolean,
    userSpeechResult: String,
    isCorrect: Boolean?,
    onMicClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Feedback text
        if (isCorrect != null) {
            Text(
                text = if (isCorrect) "Correct! 🎉" else "Try again!",
                style = AppTypography.titleLarge,
                color = if (isCorrect) Color.Green else Color.Red
            )
        }

        // User speech result
        if (userSpeechResult.isNotEmpty()) {
            Text(
                text = "You said: $userSpeechResult",
                style = AppTypography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mic button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp)
        ) {
            CircularProgressIndicator(
                progress = 1f,
                color = LogoColor.copy(alpha = 0.2f),
                modifier = Modifier.size(80.dp)
            )

            IconButton(
                onClick = onMicClick,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicNone,
                    contentDescription = "Microphone",
                    tint = if (isListening) Color.Red else LogoColor,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Text(
            text = if (isListening) "Listening..." else "Tap to speak",
            style = AppTypography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
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

// Data model for German alphabet
data class GermanAlphabet(
    val uppercase: String,
    val lowercase: String,
    val name: String,
    val soundResource: Int // For the pronunciation sound
)

val germanAlphabets = emptyList<GermanAlphabet>()

//// Sample German alphabets
//val germanAlphabets = listOf(
//    GermanAlphabet("A", "a", "Ah", R.raw.a_sound),
//    GermanAlphabet("B", "b", "Beh", R.raw.b_sound),
//    GermanAlphabet("C", "c", "Tseh", R.raw.c_sound),
//    GermanAlphabet("D", "d", "Deh", R.raw.d_sound),
//    GermanAlphabet("E", "e", "Eh", R.raw.e_sound),
//    GermanAlphabet("F", "f", "Eff", R.raw.f_sound),
//    GermanAlphabet("G", "g", "Geh", R.raw.g_sound)
//
//)