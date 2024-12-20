package com.umt.memingoapp.models.responses

data class LoginResponse(
    val access_token: String,
    val user: User
)

data class ApiResponse(
    val message: String
)

data class User(
    val name: String,
    val email: String,
    val preferred_language: String,
    val learning_goal: String
)


data class CourseResponse(
    val id: String,
    val language_id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val topics: List<Topic>,
    val flag_image_url: String
)

data class Topic(
    val id: String,
    val name: String,
    val progress: Int
)

data class LessonResponse(
    val lesson_id: String,
    val course_id: String,
    val title: String,
    val description: String,
    val content: String,
    val questions: List<Question>?
)

data class Question(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)



data class TopicProgress(
    val id: String,
    val name: String,
    val progress: Int
)

data class Language(
    val id: String,
    val name: String,
    val flag: String,
    val overall_progress: Int,
    val topics_progress: List<TopicProgress>,
    val streak: Int
)

data class UserLanguagesResponse(
    val languages: List<Language>?
)

