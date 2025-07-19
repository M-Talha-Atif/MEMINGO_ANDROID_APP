package com.umt.memingoapp.presentation.lesson.state

import com.umt.memingoapp.domain.model.response.LessonResponse
import com.umt.memingoapp.presentation.lesson.viewmodel.LessonQuestion

sealed class LessonUiState {
    object Loading : LessonUiState()
    data class Success(
        val lesson: LessonResponse,
        val flattenedQuestions: List<LessonQuestion>
    ) : LessonUiState()
    data class Error(val message: String) : LessonUiState()
}
