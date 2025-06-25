package com.umt.memingoapp.domain.model.request


// Here are our Api Requesting models like what endpoints will have data when we signup, login etc
data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String = "Talha hu yar Android App se",
    val email: String,
    val password: String,
    val phone: String = "1234567890",
    val preferred_language: String = "English",
    val learning_goal: String = "Business"
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

