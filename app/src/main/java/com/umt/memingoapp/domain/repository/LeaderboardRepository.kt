package com.umt.memingoapp.domain.repository

import com.umt.memingoapp.domain.model.response.LeaderboardResponse

interface LeaderboardRepository {
    suspend fun getLeaderboard(period: String): LeaderboardResponse
}
