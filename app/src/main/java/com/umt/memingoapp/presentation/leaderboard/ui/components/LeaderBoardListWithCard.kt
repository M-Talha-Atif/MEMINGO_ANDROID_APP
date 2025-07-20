package com.umt.memingoapp.presentation.leaderboard.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.response.LeaderboardUser
import com.umt.memingoapp.presentation.theme.LogoColor

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



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = animatedElevation,
                shape = RoundedCornerShape(CardCornerRadius),
                spotColor = if (highlight) LogoColor.copy(alpha = 0.3f) else Color.LightGray
            )
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            ),
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = animatedColor,
            contentColor = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rank number with decorative background
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = if (highlight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "#${user.rank}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (highlight) Color.White else LogoColor
                )
            }

            // User avatar with animation
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://api.dicebear.com/7.x/thumbs/svg?seed=${user.name}"),
                    contentDescription = "User avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }

            // User info column
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

            // Stats container (streak + courses)
            Row(
                modifier = Modifier.padding(start = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak info (only shown if > 0)
                if (user.best_streak > 0) {
                    AnimatedVisibility(
                        visible = user.best_streak > 0,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(
                                    color = if (highlight) MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
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
                                    tint = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "${user.best_streak}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                // Courses info
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            color = if (highlight) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
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
                            tint = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${user.active_courses}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (highlight) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}