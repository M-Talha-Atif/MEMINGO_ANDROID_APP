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
