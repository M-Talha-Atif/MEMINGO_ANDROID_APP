package com.umt.memingoapp.presentation.leaderboard.state

enum class TimeFilterType(val label: String) {
    DAILY("daily"),
    WEEK("weekly"),
    MONTH("monthly"),
    ALL_TIME("all-time")
}


fun TimeFilterType.toApiParam(): String = when (this) {
    TimeFilterType.WEEK -> "weekly"
    TimeFilterType.MONTH -> "monthly"
    TimeFilterType.ALL_TIME -> "all_time"
    TimeFilterType.DAILY -> "daily"
}
