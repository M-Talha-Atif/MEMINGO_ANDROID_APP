package com.umt.memingoapp.presentation.leaderboard.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.umt.memingoapp.domain.model.response.LeaderboardResponse
import com.umt.memingoapp.domain.model.response.LeaderboardUser
import com.umt.memingoapp.presentation.leaderboard.state.LeaderboardUiState
import com.umt.memingoapp.presentation.theme.LogoColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.leaderboard.state.TimeFilterType
import com.umt.memingoapp.presentation.leaderboard.ui.components.LeaderboardList
import com.umt.memingoapp.presentation.leaderboard.ui.components.TimeFilterSelector
import com.umt.memingoapp.presentation.leaderboard.ui.components.TopThreeLeaderboard
import com.umt.memingoapp.presentation.theme.MemingoAppTheme


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeaderboardScreenPreview() {
    MemingoAppTheme { // Use your app's theme
        // Create dummy data
        val dummyUsers = listOf(
            LeaderboardUser(
                user_id = "1",
                name = "Alex Johnson",
                rank = 1,
                total_points = 2450,
                best_streak = 12,
                active_courses = 3
            ),
            LeaderboardUser(
                user_id = "2",
                name = "Maria Garcia",
                rank = 2,
                total_points = 2300,
                best_streak = 8,
                active_courses = 4
            ),
            LeaderboardUser(
                user_id = "3",
                name = "James Wilson",
                rank = 3,
                total_points = 2150,
                best_streak = 5,
                active_courses = 2
            ),
            LeaderboardUser(
                user_id = "4",
                name = "Sarah Miller",
                rank = 4,
                total_points = 1950,
                best_streak = 7,
                active_courses = 3
            ),
            LeaderboardUser(
                user_id = "5",
                name = "Robert Davis",
                rank = 5,
                total_points = 1800,
                best_streak = 3,
                active_courses = 1
            ),
            LeaderboardUser(
                user_id = "6", // This will be the highlighted current user
                name = "You",
                rank = 6,
                total_points = 1750,
                best_streak = 4,
                active_courses = 2
            )
        )

        val dummyResponse = LeaderboardResponse(
            leaderboard = dummyUsers,
            current_user_rank = dummyUsers[5] // Highlight the "You" user
        )

        // Create a view model state
        val dummyState = LeaderboardUiState.Success(dummyResponse)

        // Create a preview version of the screen
        LeaderboardScreenPreviewWrapper(uiState = dummyState)
    }
}

@Composable
private fun LeaderboardScreenPreviewWrapper(uiState: LeaderboardUiState) {
    var selectedFilter by remember { mutableStateOf(TimeFilterType.ALL_TIME) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color.White, Color(0xFFF6F9FC))))
    ) {
        when (uiState) {
            is LeaderboardUiState.Success -> {
                val data = uiState.data
                val topThree = data.leaderboard.take(3)
                val rest = data.leaderboard.drop(3)

                Column(
                    Modifier.padding(16.dp)
                ) {
                    Text(
                        "🏆 Leaderboard",
                        style = MaterialTheme.typography.headlineMedium,
                        color = LogoColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Time filter selector
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
            else -> {
                // Handle other states if needed for preview
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Preview not available for this state")
                }
            }
        }
    }
}

