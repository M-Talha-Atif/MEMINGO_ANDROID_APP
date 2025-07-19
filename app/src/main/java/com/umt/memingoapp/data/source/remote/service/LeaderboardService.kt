package com.umt.memingoapp.data.source.remote.service

import com.umt.memingoapp.domain.model.response.LeaderboardResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LeaderboardService {

    @GET("/leaderboard")
    suspend fun getLeaderboard(
        @Query("period") period: String
    ): LeaderboardResponse
}
