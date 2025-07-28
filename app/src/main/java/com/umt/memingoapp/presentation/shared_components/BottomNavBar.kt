package com.umt.memingoapp.presentation.shared_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.umt.memingoapp.presentation.navigation.routes.Screen
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.example.bottombar.model.VisibleItem
import com.umt.memingoapp.presentation.navigation.MainNavigation


@Composable
fun BottomNavigationBar(navController: NavController, courseId: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItems = listOf(
        MainNavigation.Home,
        MainNavigation.Profile,
        MainNavigation.Leaderboard
    )

    var selectedItem by remember {
        mutableIntStateOf(
            navigationItems.indexOfFirst { currentRoute?.startsWith(it.route) == true }
                .takeIf { it >= 0 } ?: 0
        )
    }

    val colorScheme = MaterialTheme.colorScheme

    AnimatedBottomBar(
        selectedItem = selectedItem,
        itemSize = navigationItems.size,
        containerColor = colorScheme.surface,
        indicatorStyle = IndicatorStyle.NONE,
        indicatorColor = colorScheme.primary.copy(alpha = 0.1f)
    ) {
        navigationItems.forEachIndexed { index, item ->

            BottomBarItem(
                modifier = Modifier,
                selected = currentRoute?.startsWith(item.route) == true,
                onClick = {
                    if (currentRoute?.startsWith(item.route) != true) {
                        selectedItem = index
                        val routeWithId = if (item == MainNavigation.Home || item == MainNavigation.Profile ||
                            item == MainNavigation.Leaderboard) {
                            "${item.route}/$courseId"
                        } else item.route

                        navController.navigate(routeWithId) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                imageVector = item.icon,
                label = item.title,

                // Base Colors
                containerColor = Color.Transparent,
                iconColor = colorScheme.onSurface,
                textColor = colorScheme.onSurface,

                // Theme-based colors
                contentColor = colorScheme.primary,

                // Indicator Background
                activeIndicatorColor = colorScheme.primary.copy(alpha = 0.1f),
                inactiveIndicatorColor = Color.Transparent,

                // Soft glow effect
                glowingBackground = Brush.radialGradient(
                    colors = listOf(
                        colorScheme.primary.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    radius = 150f
                ),

                // Style: Show icon and text
                visibleItem = VisibleItem.BOTH,

                // Visual style – rounded, animated
                itemStyle = ItemStyle.STYLE2 // STYLE2 has elevation and highlight effect
            )

        }
    }
}



