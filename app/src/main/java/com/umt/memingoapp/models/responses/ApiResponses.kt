package com.umt.memingoapp.models.responses


// here are responses when we hit an api returned by server via api
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



data class CourseResponse(
    val id: String,
    val language_id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val topics: List<Topic>,
    val flag_image_url: String
)


data class SingleCourseResponse(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val overallProgress: Double,
    val topics: List<Topic>,
    val totalLessons: Int,
    val flagImageUrl: String,
    val lessons: List<Lesson>,
    val quizzes: List<String> = emptyList()
)


data class Lesson(
    val id: String,
    val title: String,
    val type: String,
    val status: String,
    val progress: Double = 0.0,
    val quizScore: Double? = null,
    val prerequisites: List<String> = emptyList()
)







data class Topic(
    val id: String,
    val name: String,
    val progress: Double
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





