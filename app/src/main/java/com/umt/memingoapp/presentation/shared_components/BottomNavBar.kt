package com.umt.memingoapp.presentation.shared_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umt.memingoapp.presentation.navigation.routes.Screen

@Composable
fun BottomNavigationBar(navController: NavController, courseId: String) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 10.dp,
    ) {
        BottomNavItem(
            route = "${Screen.Dashboard.route}/$courseId",
            icon = Icons.Default.Home,
            label = "Home",
            navController = navController,
            currentRoute = currentDestination,
            indicatorColor = indicatorColor,
            contentColor = contentColor
        )
        BottomNavItem(
            route = "${Screen.Profile.route}/$courseId",
            icon = Icons.Default.Person,
            label = "Profile",
            navController = navController,
            currentRoute = currentDestination,
            indicatorColor = indicatorColor,
            contentColor = contentColor
        )
        BottomNavItem(
            route = Screen.Chat.route,
            icon = Icons.Default.Chat,
            label = "Chat",
            navController = navController,
            currentRoute = currentDestination,
            indicatorColor = indicatorColor,
            contentColor = contentColor
        )
        BottomNavItem(
            route = Screen.Leaderboard.route,
            icon = Icons.Default.Leaderboard,
            label = "Leaderboard",
            navController = navController,
            currentRoute = currentDestination,
            indicatorColor = indicatorColor,
            contentColor = contentColor
        )
    }
}


@Composable
private fun RowScope.BottomNavItem(
    route: String,
    icon: ImageVector,
    label: String,
    navController: NavController,
    currentRoute: String?,
    indicatorColor: Color,
    contentColor: Color
) {
    val isSelected = currentRoute == route

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) indicatorColor else Color.Transparent,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    NavigationBarItem(
        selected = isSelected,
        onClick = {
            if (!isSelected) {
                navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        icon = {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else contentColor,
                    modifier = Modifier.scale(iconScale)
                )
            }
        },
        label = {
            Text(
                text = label,
                color = if (isSelected) MaterialTheme.colorScheme.primary else contentColor,
                style = MaterialTheme.typography.labelSmall
            )
        },
        alwaysShowLabel = true
    )
}

