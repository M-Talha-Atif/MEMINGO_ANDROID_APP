package com.umt.memingoapp.presentation.lesson.ui


import MatchPairsContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umt.memingoapp.presentation.theme.LogoColor
import com.umt.memingoapp.presentation.shared_components.AppTypography
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.umt.memingoapp.presentation.lesson.components.helpers.BackConfirmationDialog
import com.umt.memingoapp.presentation.lesson.components.DragAndDropSentenceContent
import com.umt.memingoapp.presentation.lesson.components.FillInBlankContent
import com.umt.memingoapp.presentation.lesson.components.ListenAndSelectContent
import com.umt.memingoapp.presentation.lesson.components.MultipleChoiceContent
import com.umt.memingoapp.presentation.lesson.components.PronunciationPracticeContent
import com.umt.memingoapp.presentation.lesson.components.SentenceBuilderContent
import com.umt.memingoapp.presentation.lesson.components.TypeTranslationContent
import com.umt.memingoapp.presentation.lesson.viewmodel.LessonQuestion
import com.umt.memingoapp.presentation.lesson.state.LessonUiState
import com.umt.memingoapp.presentation.lesson.viewmodel.LessonViewModel
import com.umt.memingoapp.presentation.lesson.viewmodel.MAX_HEARTS
import com.umt.memingoapp.presentation.lesson.viewmodel.QuestionResult
import com.umt.memingoapp.presentation.shared_components.LoadingIndicator
import java.util.Locale


@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
}

fun getCourseLocale(courseId: String): Locale {
    val language = courseId.substringBefore("-").lowercase()
    return when (language) {
        "german" -> Locale.GERMAN
        "spanish" -> Locale("es")  // Spanish (es)
        "italian" -> Locale.ITALIAN
        "french" -> Locale.FRENCH
        "japanese" -> Locale.JAPANESE
        "russian" -> Locale("ru")   // Russian (ru)
        "korean" -> Locale.KOREAN
        "chinese" -> Locale.CHINESE
        "portuguese" -> Locale("pt") // Portuguese (pt)
        // Add more languages as needed
        else -> Locale.ENGLISH // Default fallback
    }
}

fun getCourseLanguageTag(courseId: String): String {
    val language = courseId.substringBefore("-").lowercase()
    return when (language) {
        "german" -> "de-DE"
        "spanish" -> "es-ES"
        "italian" -> "it-IT"
        "french" -> "fr-FR"
        "japanese" -> "ja-JP"
        "russian" -> "ru-RU"
        "korean" -> "ko-KR"
        "chinese" -> "zh-CN"
        "portuguese" -> "pt-PT"
        "english" -> "en-US"
        // Add more as needed
        else -> "en-US" // Default fallback
    }
}

@Composable
fun LessonScreenWithViewModel(
    courseId: String,
    lessonId: String,
    onBack: () -> Unit,
    onQuit: () -> Unit,
    navHostController: NavHostController
) {
    val lessonViewModel: LessonViewModel = hiltViewModel()

    val uiState by lessonViewModel.uiState.collectAsState()
    val currentIndex by lessonViewModel.currentQuestion.collectAsState()
    val hearts by lessonViewModel.hearts.collectAsState()
    val questionResults by lessonViewModel.questionResults.collectAsState()
    val questions by lessonViewModel.flattenedQuestions.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    fun extractLanguage(courseId: String): String {
        return courseId.substringBefore("-")
    }

    val courseLocale = getCourseLocale(courseId)

    val courseTag = getCourseLanguageTag(courseId)



    LaunchedEffect(lessonId) {
        lessonViewModel.fetchLesson(lessonId)
    }


    val lessonTitle = (uiState as? LessonUiState.Success)?.lesson?.title ?: "Lesson"

    if (showExitDialog) {
        BackConfirmationDialog(
            onDismissRequest = { showExitDialog = false },
            onConfirmExit = {
                showExitDialog = false
                navHostController.popBackStack()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        if (uiState is LessonUiState.Loading){
            LoadingIndicator()
        }

        LessonScreen(
            lessonName = lessonTitle,
            currentQuestion = (currentIndex + 1).coerceAtMost(questions.size),
            totalQuestions = questions.size,
            percentCompleted = (((currentIndex + 1).toFloat() / (questions.size.coerceAtLeast(1))) * 100).toInt(),
            questionResults = questionResults,
            hearts = hearts,
            onBack = { showExitDialog = true },
            content = {
                when {
                    hearts <= 0 -> {

                        var didSubmit by remember { mutableStateOf(false) }
                        var submitSuccess by remember { mutableStateOf<Boolean?>(null) }

                        LaunchedEffect(Unit) {
                            if (!didSubmit) {
                                didSubmit = true
                                lessonViewModel.finishLesson(
                                    courseId = courseId,
                                    lessonId = lessonId,
                                    onResult = { success -> submitSuccess = success }
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                            Text("You've run out of hearts!", color = Color.Red)
                            if (submitSuccess == false) {
                                Text("Failed to submit results.", color = Color.Red)
                            }
                            Button(
                                onClick = { navHostController.popBackStack() },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Quit Lesson")
                            }
                        }
                    }
                    currentIndex >= questions.size -> {
                        // This block renders when lesson is complete
                        var didSubmit by remember { mutableStateOf(false) }
                        var submitSuccess by remember { mutableStateOf<Boolean?>(null) }

                        LaunchedEffect(Unit) {
                            if (!didSubmit) {
                                didSubmit = true
                                lessonViewModel.finishLesson(
                                    courseId = courseId,
                                    lessonId = lessonId,
                                    onResult = { success -> submitSuccess = success }
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                            Text("Congratulations! Lesson Completed.", color = LogoColor)
                            if (submitSuccess == false) {
                                Text("Failed to submit results.", color = Color.Red)
                            }
                            Button(
                                onClick = { navHostController.popBackStack() },
                                modifier = Modifier.padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LogoColor,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Finish")
                            }
                        }
                    }
                    else -> {
                        when (val q = questions[currentIndex]) {
                            is LessonQuestion.ListenAndSelectQuestion ->
                                ListenAndSelectContent(
                                    content = q.content,
                                    language = courseLocale,
                                    explanation = q.content.explanation,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    courseTag = courseTag,
                                    onWrong = { lessonViewModel.onWrong() }
                                )
                            is LessonQuestion.PronunciationQuestion ->
                                PronunciationPracticeContent(
                                    word = q.word,
                                    language = courseLocale,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    courseTag = courseTag,
                                    onWrong = { lessonViewModel.onWrong() }
                                )
                            is LessonQuestion.SentenceBuilderQuestion ->
                                SentenceBuilderContent(
                                    content = q.content,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    onWrong = { lessonViewModel.onWrong() }
                                )
                            is LessonQuestion.TypeTranslationQuestion ->
                                TypeTranslationContent(
                                    content = q.content,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    onWrong = { lessonViewModel.onWrong() }
                                )
                            is LessonQuestion.DragAndDropQuestion ->
                                DragAndDropSentenceContent(
                                    content = q.content,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    onWrong = { lessonViewModel.onWrong() }
                                )

                            is LessonQuestion.MatchPairsQuestion ->
                                MatchPairsContent(
                                    content = q.content,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    onWrong = { lessonViewModel.onWrong() }
                                )

                            is LessonQuestion.MultipleChoiceQuestion ->
                                MultipleChoiceContent(
                                    content = q.content,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    onWrong = { lessonViewModel.onWrong() }
                                )

                            is LessonQuestion.FillInBlankQuestion ->
                                FillInBlankContent(
                                    content = q.content,
                                    onCorrect = { lessonViewModel.onCorrect() },
                                    onWrong = { lessonViewModel.onWrong() }
                                )
                        }
                    }
                }
            }
        )

    }



}


@Composable
fun LessonScreen(
    lessonName: String,
    currentQuestion: Int,
    totalQuestions: Int,
    percentCompleted: Int,
    questionResults: List<QuestionResult>, // e.g. [Correct, Wrong, Unanswered]
    hearts: Int,
    onBack: () -> Unit,
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
                    text = "$currentQuestion of $totalQuestions",
                    style = AppTypography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                )
            }
            // Hearts
            Row {
                repeat(MAX_HEARTS) { i ->
                    val color = if (i < hearts) Color.Red else Color.Gray
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(28.dp)
                    )
                }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                content()
            }
        }
    }
}


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

