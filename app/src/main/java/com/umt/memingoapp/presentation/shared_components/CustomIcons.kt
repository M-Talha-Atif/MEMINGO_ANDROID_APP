package com.umt.memingoapp.presentation.shared_components


import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ArrowBack: ImageVector
    get() {
        return ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fillAlpha = 1.0f,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(313.0f, -440.0f)
                lineToRelative(224.0f, 224.0f)
                lineToRelative(-57.0f, 56.0f)
                lineToRelative(-320.0f, -320.0f)
                lineToRelative(320.0f, -320.0f)
                lineToRelative(57.0f, 56.0f)
                lineToRelative(-224.0f, 224.0f)
                horizontalLineToRelative(487.0f)
                verticalLineToRelative(80.0f)
                close()
            }
        }.build()
    }