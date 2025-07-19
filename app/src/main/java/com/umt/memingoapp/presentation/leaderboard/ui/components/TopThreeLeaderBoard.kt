package com.umt.memingoapp.presentation.leaderboard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.umt.memingoapp.domain.model.response.LeaderboardUser
import com.umt.memingoapp.presentation.theme.LogoColor

@Composable
fun TopThreeLeaderboard(users: List<LeaderboardUser>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val ordered = listOfNotNull(
            users.getOrNull(1),
            users.getOrNull(0),
            users.getOrNull(2)
        )

        ordered.forEachIndexed { index, user ->
            TopThreeCard(
                user = user,
                rank = if (index == 0) 2 else if (index == 1) 1 else 3 // 2nd, 1st, 3rd visual order
            )
        }
    }
}

@Composable
fun TopThreeCard(user: LeaderboardUser, rank: Int) {
    val size = when (rank) {
        1 -> 100.dp
        else -> 80.dp
    }

    val medalColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> LogoColor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(size)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(medalColor, Color.White)))
                .border(3.dp, medalColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://api.dicebear.com/7.x/thumbs/svg?seed=${user.name}"),
                contentDescription = user.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size - 12.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(user.name, style = MaterialTheme.typography.bodyLarge, color = Color.DarkGray)
        Text("${user.total_points} pts", style = MaterialTheme.typography.bodySmall)
    }
}