package com.umt.memingoapp.models

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.umt.memingoapp.R

class GetStartedData(@DrawableRes val resId: Int, val title: String, val desc: String)
// @RawRes for raw folder
//

val listData = listOf(
    GetStartedData(
        R.drawable.av2,
        "Learn Step by Step",
        "Start your language-learning journey with structured, easy-to-follow lessons."
    ),
    GetStartedData(
        R.drawable.av5,
        "Practice with Confidence",
        "Engage in interactive exercises to boost your skills and build confidence."
    ),
    GetStartedData(
        R.drawable.av6,
        "Track Your Progress",
        "Monitor your improvement with detailed stats and personalized feedback."
    )
)