package com.umt.memingoapp.presentation.leaderboard.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.umt.memingoapp.presentation.leaderboard.state.LeaderboardUiState
import com.umt.memingoapp.presentation.leaderboard.state.TimeFilterType
import com.umt.memingoapp.presentation.leaderboard.state.toApiParam
import com.umt.memingoapp.presentation.leaderboard.ui.components.LeaderboardList
import com.umt.memingoapp.presentation.leaderboard.ui.components.TimeFilterSelector
import com.umt.memingoapp.presentation.leaderboard.ui.components.TopThreeLeaderboard
import com.umt.memingoapp.presentation.leaderboard.viewmodel.LeaderboardViewModel
import com.umt.memingoapp.presentation.theme.LogoColor


@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf(TimeFilterType.ALL_TIME) }

    LaunchedEffect(selectedFilter) {
        viewModel.loadLeaderboard(selectedFilter.toApiParam())
    }

    when (state) {
        is LeaderboardUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LogoColor)
            }
        }

        is LeaderboardUiState.Success -> {
            val data = (state as LeaderboardUiState.Success).data
            val topThree = data.leaderboard.take(3)
            val rest = data.leaderboard.drop(3)

            Column(
                Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Color.White, Color(0xFFF6F9FC))))
                    .padding(16.dp)
            ) {
                Text(
                    "🏆 Leaderboard",
                    style = MaterialTheme.typography.headlineMedium,
                    color = LogoColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // ⏱️ Time filter selector
                TimeFilterSelector(
                    selected = selectedFilter,
                    onSelect = { selectedFilter = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                TopThreeLeaderboard(users = topThree)

                Spacer(modifier = Modifier.height(24.dp))

                LeaderboardList(users = rest, highlightId = data.current_user_rank?.user_id)
            }
        }

        is LeaderboardUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Oops: ${(state as LeaderboardUiState.Error).message}", color = Color.Red)
            }
        }
    }
}
