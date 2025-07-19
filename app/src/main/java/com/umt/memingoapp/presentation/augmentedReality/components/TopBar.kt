package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.CenterFocusWeak
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.presentation.theme.LogoColor


@Preview(showBackground = true)
@Composable
fun ARLearningTopBarPreview() {
    ARLearningTopBar(
        moduleTitle = "AR Learning",
        language = "English",
        onClose = { /*TODO*/ },
        onScan = { /*TODO*/ }
    )
}

@Composable
fun ARLearningTopBar(
    moduleTitle: String,
    language: String,
    onClose: () -> Unit,
    onScan: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Cross Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .background(LogoColor.copy(alpha = 0.10f), shape = CircleShape)
                .clickable { onClose() }
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = LogoColor,
                modifier = Modifier.size(28.dp)
            )
        }

        // Title and Subtitle
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = moduleTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 28.sp,
                    color = LogoColor
                )
            )
            Text(
                text = language,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = LogoColor.copy(alpha = 0.68f),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    letterSpacing = 0.2.sp
                )
            )
        }

        // Scan Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .background(LogoColor.copy(alpha = 0.10f), shape = CircleShape)
                .clickable { onScan() }
        ) {
            Icon(
                Icons.Rounded.CenterFocusWeak,
                contentDescription = "Scan",
                tint = LogoColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

