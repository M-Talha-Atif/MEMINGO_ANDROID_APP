package com.umt.memingoapp.presentation.objectDetection.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.umt.memingoapp.domain.model.response.DetectionObject

@Composable
fun DetectionBoxesOverlay(
    detectionList: List<DetectionObject>,
    onBoxClick: (DetectionObject) -> Unit,
    modifier: Modifier = Modifier,
    // Pass constraints so you can scale boxes to UI size
    previewWidth: Float,
    previewHeight: Float,
    boxColors: List<Color> = listOf(
        Color(0xFF2979FF), Color(0xFF00C853), Color(0xFFFFD600), Color(0xFFD50000)
    )
) {
    // To animate highlight on tap
    var tappedBoxIdx by remember { mutableStateOf<Int?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        detectionList.forEachIndexed { idx, detection ->
            val box = detection.boundingBox
            // Scale bounding box to preview size (if needed)
            val left = box.left
            val top = box.top
            val width = box.width()
            val height = box.height()

            val highlight = tappedBoxIdx == idx
            val animatedAlpha by animateFloatAsState(targetValue = if (highlight) 0.22f else 0.12f, label = "box-alpha")
            val animatedBorder by animateFloatAsState(targetValue = if (highlight) 3f else 2f, label = "border")

            Box(
                modifier = Modifier
                    .absoluteOffset(x = left.dp, y = top.dp)
                    .size(width.dp, height.dp)
                    .zIndex(if (highlight) 1f else 0f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(boxColors[idx % boxColors.size].copy(alpha = animatedAlpha))
                    .border(
                        width = animatedBorder.dp,
                        color = boxColors[idx % boxColors.size],
                        shape = RoundedCornerShape(16.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                tappedBoxIdx = idx
                                onBoxClick(detection)
                            }
                        )
                    }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        detection.label,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${(detection.score * 100).toInt()}%",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}