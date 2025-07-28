package com.umt.memingoapp.presentation.leaderboard.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.umt.memingoapp.domain.model.response.LeaderboardUser
import com.umt.memingoapp.presentation.theme.LogoColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex


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
        else -> MaterialTheme.colorScheme.primary
    }

    val glowColor = medalColor.copy(alpha = 0.4f)
    val firstName = user.name.split(" ").firstOrNull() ?: user.name

    //  Pick image resource based on rank
    @DrawableRes val imageRes = when (rank) {
        1 -> com.umt.memingoapp.R.drawable.u1
        2 -> com.umt.memingoapp.R.drawable.u2
        3 -> com.umt.memingoapp.R.drawable.u3
        else -> com.umt.memingoapp.R.drawable.u1
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size + 12.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor, Color.Transparent),
                        center = Offset.Zero,
                        radius = (size.value + 12f) * 0.6f,
                        tileMode = TileMode.Clamp
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Wrap in a BoxScope to allow align()
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, medalColor, CircleShape)
                    .shadow(8.dp, CircleShape, clip = false)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = user.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )

                // Proper overlay for rank badge
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset((-2).dp, 8.dp)   // -2, horizontal increase to move left, 8 is vertical, decrease to move up
                        .zIndex(1f)
                        .background(color = medalColor, shape = CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = rank.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = firstName,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            maxLines = 1
        )

        Text(
            text = "${user.total_points} pts",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
