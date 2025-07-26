package com.umt.memingoapp.domain.repository

import com.umt.memingoapp.domain.model.request.UpdateProfileRequest
import com.umt.memingoapp.domain.model.response.UserProfileResponse

interface ProfileRepository {
    suspend fun getUserProfile(): UserProfileResponse
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): UserProfileResponse
}