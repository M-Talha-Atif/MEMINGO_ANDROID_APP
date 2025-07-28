package com.umt.memingoapp.presentation.leaderboard.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.response.LeaderboardUser
import com.umt.memingoapp.presentation.theme.LogoColor

import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale

@Composable
fun OptimizedAvatar(
    @DrawableRes avatarRes: Int,
    highlight: Boolean,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatarRes)
            .crossfade(true)
            .size(96) // 2x display size for sharpness
            .scale(Scale.FILL)
            .build(),
        contentDescription = "User avatar",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(
                2.dp,
                if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                CircleShape
            )
    )
}


private val CardCornerRadius = 12.dp

@Composable
fun LeaderboardList(users: List<LeaderboardUser>, highlightId: String?) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        users.forEach { user ->
            LeaderboardCard(user, highlight = user.user_id == highlightId)
        }
    }
}
@Composable
fun LeaderboardCard(user: LeaderboardUser, highlight: Boolean) {
    val animatedElevation by animateDpAsState(
        targetValue = if (highlight) 12.dp else 8.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedColor by animateColorAsState(
        targetValue = if (highlight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300)
    )

    //  avatar from u4–u8
    val avatarRes = listOf(
        R.drawable.u4,
        R.drawable.u5,
        R.drawable.u6,
        R.drawable.u7,
        R.drawable.u8
    ).random()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = animatedElevation,
                shape = RoundedCornerShape(CardCornerRadius),
                spotColor = if (highlight) LogoColor.copy(alpha = 0.3f) else Color.LightGray
            )
            .animateContentSize(tween(300)),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = animatedColor,
            contentColor = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rank badge (improved)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFD700), // top gold
                                Color(0xFFE5B800)  // bottom gold
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Text(
                    text = "#${user.rank}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            // User avatar (random local)
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {

                OptimizedAvatar(avatarRes = avatarRes, highlight = highlight)

            }

            // User info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${user.total_points} pts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Stats (streak + courses)
            Row(
                modifier = Modifier.padding(start = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (user.best_streak > 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.fire),
                                contentDescription = "Streak",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "${user.best_streak}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                // Courses
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_course),
                            contentDescription = "Courses",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${user.active_courses}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
