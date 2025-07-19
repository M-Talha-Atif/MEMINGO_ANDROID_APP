package com.umt.memingoapp.domain.model.response

data class LeaderboardResponse(
    val leaderboard: List<LeaderboardUser>,
    val current_user_rank: LeaderboardUser?
)

data class LeaderboardUser(
    val user_id: String,
    val name: String,
    val rank: Int,
    val total_points: Int,
    val best_streak: Int,
    val active_courses: Int
)
