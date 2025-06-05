package com.umt.memingoapp.models.responses

data class UpdateLessonSubmitRequest(
    val course_id: String,
    val activity_scores: List<Int>,
    val overall_score: Int
)