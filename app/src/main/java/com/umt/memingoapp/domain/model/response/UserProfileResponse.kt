package com.umt.memingoapp.domain.model.response


data class UserProfileResponse(
    val user_id: String,
    val name: String,
    val email: String,
    val learning_goal: String,
    val phone: String,
)