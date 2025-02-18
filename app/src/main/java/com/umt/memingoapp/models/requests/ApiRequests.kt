package com.umt.memingoapp.models.requests


data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String = "Talha hu yar Android App se", // Temporary dummy data
    val email: String,
    val password: String,
    val phone: String = "1234567890", // Temporary dummy data
    val preferred_language: String = "English", // Temporary dummy data
    val learning_goal: String = "Business" // Temporary dummy data
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)



data class UpdateLanguagePreferenceRequest(
    val language: String
)

data class UpdateProgressRequest(
    val courseId: String,
    val lessonId: String,
    val status: String = "in_progress",
    val score: Int? = null
)
