package com.umt.memingoapp.lessons


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.R
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.uiComponents.AppTypography


@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    val mockResults = listOf(
        QuestionResult.Correct,
        QuestionResult.Wrong,
        QuestionResult.Unanswered,
        QuestionResult.Correct
    )

    LessonScreen(
        lessonName = "Tiere erkennen",
        currentQuestion = 2,
        totalQuestions = 5,
        percentCompleted = 40,
        questionResults = mockResults,
        onBack = {},
        onPrev = {},
        onNext = {},
        content = {
            Text(
                text = "Was ist das? (What is this?)",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                style = AppTypography.bodyLarge
            )
        }
    )
}



@Composable
fun LessonScreen(
    lessonName: String,
    currentQuestion: Int,
    totalQuestions: Int,
    percentCompleted: Int,
    questionResults: List<QuestionResult>, // e.g. [Correct, Wrong, Unanswered]
    onBack: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LogoColor)
                .padding(vertical = 16.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lessonName,
                    style = AppTypography.titleLarge.copy(color = Color.White)
                )
                Text(
                    text = "${currentQuestion} of $totalQuestions",
                    style = AppTypography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                )
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.13f),
                modifier = Modifier
            ) {
                Text(
                    text = "$percentCompleted%",
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    style = AppTypography.bodyLarge.copy(color = Color.White, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                )
            }
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = percentCompleted / 100f,
            color = LogoColor,
            trackColor = LogoColor.copy(alpha = 0.25f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )

        // Question Status Circles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            questionResults.forEach { result ->
                QuestionStatusCircle(result)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        // Question content (dynamic slot)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            content()
        }

        // Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp, top = 6.dp, start = 18.dp, end = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onPrev,
                enabled = currentQuestion > 1,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Text("Prev", color = Color.White)
            }
            Button(
                onClick = onNext,
                enabled = currentQuestion < totalQuestions,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Text("Next", color = Color.White)
            }
        }
    }
}

// Question result type for status bar
enum class QuestionResult { Unanswered, Correct, Wrong }

@Composable
fun QuestionStatusCircle(result: QuestionResult) {
    val icon = when (result) {
        QuestionResult.Correct -> Icons.Default.Check
        QuestionResult.Wrong -> Icons.Default.Close
        QuestionResult.Unanswered -> null
    }

    val bgColor = when (result) {
        QuestionResult.Correct -> Color(0xFF4CAF50)
        QuestionResult.Wrong -> Color(0xFFD32F2F)
        QuestionResult.Unanswered -> Color.Transparent
    }

    val contentColor = when (result) {
        QuestionResult.Correct, QuestionResult.Wrong -> Color.White
        QuestionResult.Unanswered -> Color.Gray
    }

    val border = when (result) {
        QuestionResult.Unanswered -> BorderStroke(1.dp, Color.Gray)
        else -> null
    }

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(bgColor)
            .let { if (border != null) it.border(border, CircleShape) else it },
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor)
        }
    }
}


//////////////////////////////////////////////////////////
// Example of a question content composable for alphabet:
//////////////////////////////////////////////////////////
@Composable
fun AlphabetQuestionContent(
    alphabet: String,
    onPlaySound: () -> Unit,
    onRecordVoice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = alphabet,
            style = AppTypography.displayLarge,
            color = LogoColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPlaySound) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "Play Sound",
                    tint = LogoColor,
                    modifier = Modifier.size(38.dp)
                )
            }
            IconButton(onClick = onRecordVoice) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sound),
                    contentDescription = "Record Voice",
                    tint = LogoColor,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

    }
}