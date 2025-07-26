package com.umt.memingoapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.umt.memingoapp.presentation.navigation.routes.Screen

sealed interface MainNavigation {
    val route: String
    val icon: ImageVector
    val title: String

    object Home : MainNavigation {
        override val route = Screen.Dashboard.route
        override val icon = Icons.Default.Home
        override val title = "Home"
    }

    object Profile : MainNavigation {
        override val route = Screen.Profile.route
        override val icon = Icons.Default.Person
        override val title = "Profile"
    }

    object Chat : MainNavigation {
        override val route = Screen.Chat.route
        override val icon = Icons.Default.Chat
        override val title = "Chat"
    }

    object Leaderboard : MainNavigation {
        override val route = Screen.Leaderboard.route
        override val icon = Icons.Default.Leaderboard
        override val title = "Leaderboard"
    }

}

