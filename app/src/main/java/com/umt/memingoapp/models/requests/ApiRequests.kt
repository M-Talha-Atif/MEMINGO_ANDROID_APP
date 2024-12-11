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
    val learning_goal: String = "Learn Cricket" // Temporary dummy data
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)
