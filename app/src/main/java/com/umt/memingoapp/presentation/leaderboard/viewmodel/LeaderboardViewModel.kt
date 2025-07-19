package com.umt.memingoapp.presentation.leaderboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.domain.repository.LeaderboardRepository
import com.umt.memingoapp.presentation.leaderboard.state.LeaderboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    fun loadLeaderboard(period: String = "all_time") {
        viewModelScope.launch {
            try {
                _uiState.value = LeaderboardUiState.Loading
                val response = repository.getLeaderboard(period)
                _uiState.value = LeaderboardUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = LeaderboardUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}