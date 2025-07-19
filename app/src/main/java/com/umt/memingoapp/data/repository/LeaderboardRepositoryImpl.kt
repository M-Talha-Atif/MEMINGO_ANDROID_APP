package com.umt.memingoapp.data.repository

import com.umt.memingoapp.data.source.remote.service.LeaderboardService
import com.umt.memingoapp.domain.model.response.LeaderboardResponse
import com.umt.memingoapp.domain.repository.LeaderboardRepository
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardService
) : LeaderboardRepository {

    override suspend fun getLeaderboard(period: String): LeaderboardResponse {
        return api.getLeaderboard(period)
    }
}


