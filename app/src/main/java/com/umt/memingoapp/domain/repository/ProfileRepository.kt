package com.umt.memingoapp.domain.repository



import com.umt.memingoapp.domain.model.response.UserProfileResponse

interface ProfileRepository {
    suspend fun getUserProfile(): UserProfileResponse
}
