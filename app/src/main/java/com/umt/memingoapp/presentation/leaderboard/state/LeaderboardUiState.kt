package com.umt.memingoapp.presentation.leaderboard.state

import com.umt.memingoapp.domain.model.response.LeaderboardResponse

sealed class LeaderboardUiState {
    object Loading : LeaderboardUiState()
    data class Success(val data: LeaderboardResponse) : LeaderboardUiState()
    data class Error(val message: String) : LeaderboardUiState()
}
