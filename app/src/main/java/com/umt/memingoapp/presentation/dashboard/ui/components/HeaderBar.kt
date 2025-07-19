package com.umt.memingoapp.presentation.dashboard.ui.components


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.umt.memingoapp.R



@Composable
fun HeaderBar(
    modifier: Modifier = Modifier,
    dateText: String,
    greetingText: String,
    avatarImage: Painter,
    languageIcon: Painter,
    notificationIcon: Painter,
    notificationCount: Int,
    onLanguageClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Avatar + Text
        Row( modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = avatarImage,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = greetingText,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                    ),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis, // it will show elipsis like ... dots after text if overflow
                )
            }
        }

        // Right: Language + Notification
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Language Flag in Circular Card
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = onLanguageClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = languageIcon,
                    contentDescription = "Language",
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Notification in Circular Card
            Box {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(onClick = onNotificationClick),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = notificationIcon,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = 12.dp, y = (-6).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = notificationCount.toString(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onError,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

// Light Theme Previews
@Preview(showBackground = true, name = "Light - With Notifications")
@Composable
fun HeaderBarPreviewLight() {
    MaterialTheme {
        HeaderBar(
            dateText = "Monday, 15 May",
            greetingText = "Good Morning, Alex!",
            avatarImage = painterResource(R.drawable.p1),
            languageIcon = painterResource(R.drawable.german),
            notificationIcon = painterResource(R.drawable.ic_notification),
            notificationCount = 3,
            onLanguageClick = {},
            onNotificationClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Light - No Notifications")
@Composable
fun HeaderBarNoNotificationsPreviewLight() {
    MaterialTheme {
        HeaderBar(
            dateText = "Tuesday, 16 May",
            greetingText = "Good Afternoon, Sarah!",
            avatarImage = painterResource(R.drawable.p1),
            languageIcon = painterResource(R.drawable.german),
            notificationIcon = painterResource(R.drawable.ic_notification),
            notificationCount = 0,
            onLanguageClick = {},
            onNotificationClick = {}
        )
    }
}

// Dark Theme Previews
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark - With Notifications")
@Composable
fun HeaderBarPreviewDark() {
    MaterialTheme {
        HeaderBar(
            dateText = "Monday, 15 May",
            greetingText = "Good Morning, Alex!",
            avatarImage = painterResource(R.drawable.p1),
            languageIcon = painterResource(R.drawable.german),
            notificationIcon = painterResource(R.drawable.ic_notification),
            notificationCount = 3,
            onLanguageClick = {},
            onNotificationClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark - No Notifications")
@Composable
fun HeaderBarNoNotificationsPreviewDark() {
    MaterialTheme {
        HeaderBar(
            dateText = "Tuesday, 16 May",
            greetingText = "Good Afternoon, Sarah!",
            avatarImage = painterResource(R.drawable.p1),
            languageIcon = painterResource(R.drawable.german),
            notificationIcon = painterResource(R.drawable.ic_notification),
            notificationCount = 0,
            onLanguageClick = {},
            onNotificationClick = {}
        )
    }
}