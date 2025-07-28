package com.umt.memingoapp.presentation.dashboard.ui.components


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.presentation.shared_components.AppTypography
import androidx.compose.ui.tooling.preview.Preview
import com.umt.memingoapp.R
import com.umt.memingoapp.presentation.theme.MemingoAppTheme

@Preview(showBackground = true)
@Composable
fun LearningModeCardPreview() {
    MemingoAppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LearningModeCard(
                title = "Chat with AI",
                description = "Ask questions, get instant answers.",
                iconRes = R.drawable.p3,
                onClick = {}
            )
            LearningModeCard(
                title = "AR Learning",
                description = "Select objects and learn vocabulary.",
                iconRes = R.drawable.av5,
                onClick = {}
            )
        }
    }
}


@Composable
fun LearningModeCard(
    title: String,
    description: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "CardScaleAnimation"
    )
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 8.dp,
        label = "CardElevationAnimation"
    )

    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(12.dp)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick,
        interactionSource = interactionSource,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp)
                    .shadow(4.dp, CircleShape, clip = false)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize()
                )
            }


            Text(
                text = title,
                style = AppTypography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = AppTypography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

