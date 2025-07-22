package com.umt.memingoapp.data.repository

import com.umt.memingoapp.data.source.remote.service.AuthenticationService
import com.umt.memingoapp.domain.model.response.UserProfileResponse
import com.umt.memingoapp.domain.repository.ProfileRepository
import javax.inject.Inject


class ProfileRepositoryImpl @Inject constructor(
    private val api: AuthenticationService
) : ProfileRepository {

    override suspend fun getUserProfile(): UserProfileResponse {
        return api.getUserProfile()
    }
}

