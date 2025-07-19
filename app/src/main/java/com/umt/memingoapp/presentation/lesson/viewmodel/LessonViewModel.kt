package com.umt.memingoapp.presentation.lesson.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.domain.model.response.Activity
import com.umt.memingoapp.domain.model.response.ActivityContent
import com.umt.memingoapp.domain.model.response.PronunciationWord
import com.umt.memingoapp.domain.model.request.UpdateLessonSubmitRequest
import com.umt.memingoapp.domain.repository.CourseRepository
import com.umt.memingoapp.presentation.lesson.state.LessonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// For completeness

const val MAX_HEARTS = 3

sealed class LessonQuestion {
    data class ListenAndSelectQuestion(val content: ActivityContent.ListenAndSelect) : LessonQuestion()
    data class PronunciationQuestion(val word: PronunciationWord) : LessonQuestion()
    data class SentenceBuilderQuestion(val content: ActivityContent.SentenceBuilder) : LessonQuestion()
    data class TypeTranslationQuestion(val content: ActivityContent.TypeTranslation) : LessonQuestion()
    data class DragAndDropQuestion(val content: ActivityContent.DragAndDrop) : LessonQuestion()
    data class MatchPairsQuestion(val content: ActivityContent.MatchPairs) : LessonQuestion()
    data class MultipleChoiceQuestion(val content: ActivityContent.MultipleChoice) : LessonQuestion()
    data class FillInBlankQuestion(val content: ActivityContent.FillInBlank) : LessonQuestion()
}


enum class QuestionResult { Unanswered, Correct, Wrong }


@HiltViewModel
class LessonViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel()
{

    private val _uiState = MutableStateFlow<LessonUiState>(LessonUiState.Loading)
    val uiState: StateFlow<LessonUiState> = _uiState


    private val _flattenedQuestions = MutableStateFlow<List<LessonQuestion>>(emptyList())
    private val _currentQuestion = MutableStateFlow(0)
    private val _questionResults = MutableStateFlow<List<QuestionResult>>(emptyList())
    private val _hearts = MutableStateFlow(MAX_HEARTS)

    val flattenedQuestions: StateFlow<List<LessonQuestion>> = _flattenedQuestions
    val currentQuestion: StateFlow<Int> = _currentQuestion
    val questionResults: StateFlow<List<QuestionResult>> = _questionResults
    val hearts: StateFlow<Int> = _hearts


    fun fetchLesson(lessonId: String) {
        viewModelScope.launch {
            try {
                val lesson = repository.getLesson(lessonId)
                val questions = flattenQuestions(lesson.activities)
                _flattenedQuestions.value = questions
                _questionResults.value = List(questions.size) { QuestionResult.Unanswered }
                _currentQuestion.value = 0
                _hearts.value = MAX_HEARTS
                _uiState.value = LessonUiState.Success(lesson, questions)
            } catch (e: Exception) {
                _uiState.value = LessonUiState.Error(e.message ?: "Error")
            }
        }
    }


    fun flattenQuestions(activities: List<Activity>): List<LessonQuestion> {
        return activities.flatMap { activity ->
            when (val content = activity.content) {
                is ActivityContent.ListenAndSelect -> listOf(
                    LessonQuestion.ListenAndSelectQuestion(
                        content
                    )
                )

                is ActivityContent.PronunciationPractice -> content.words.map { word ->
                    LessonQuestion.PronunciationQuestion(word)
                }
                is ActivityContent.SentenceBuilder -> listOf(
                    LessonQuestion.SentenceBuilderQuestion(
                        content
                    )
                )
                is ActivityContent.TypeTranslation -> listOf(
                    LessonQuestion.TypeTranslationQuestion(
                        content
                    )
                )
                is ActivityContent.DragAndDrop -> listOf(LessonQuestion.DragAndDropQuestion(content))

                is ActivityContent.MatchPairs -> listOf(LessonQuestion.MatchPairsQuestion(content))
                is ActivityContent.MultipleChoice -> listOf(
                    LessonQuestion.MultipleChoiceQuestion(
                        content
                    )
                )
                is ActivityContent.FillInBlank -> listOf(LessonQuestion.FillInBlankQuestion(content))

            }
        }
    }

    fun onCorrect() {
        val idx = _currentQuestion.value
        _questionResults.value = _questionResults.value.toMutableList().also { it[idx] =
            QuestionResult.Correct
        }
        _currentQuestion.value = idx + 1
    }

    fun onWrong() {
        val idx = _currentQuestion.value
        _questionResults.value = _questionResults.value.toMutableList().also { it[idx] =
            QuestionResult.Wrong
        }
        _hearts.value = (_hearts.value - 1).coerceAtLeast(0)
    }

    fun finishLesson(courseId: String, lessonId: String, onResult: (Boolean) -> Unit) {
        val questions = _flattenedQuestions.value
        val results = _questionResults.value

        // Each activity gets 1 for correct, 0 for wrong/unanswered
        val activityScores = results.map { if (it == QuestionResult.Correct) 1 else 0 }

        // Calculate overall score as percent
        val correctCount = activityScores.count { it == 1 }
        val overallScore = if (activityScores.isNotEmpty())
            (correctCount * 100) / activityScores.size
        else 0

        val request = UpdateLessonSubmitRequest(
            course_id = courseId,
            activity_scores = activityScores,
            overall_score = overallScore
        )

        viewModelScope.launch {
            try {
                // Assuming you injected or have repository/service available
                val response = repository.submitLessonResults(lessonId, request)
                onResult(true) // Or pass response if you want to show score etc.
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}