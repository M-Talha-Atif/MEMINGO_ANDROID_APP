package com.umt.memingoapp.models.ml

import android.graphics.RectF

data class DetectionObject (
    val score: Float,
    val label: String,
    val boundingBox: RectF)