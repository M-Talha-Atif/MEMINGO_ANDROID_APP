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
        MainNavigation.Chat,
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
                        val routeWithId = if (item == MainNavigation.Home || item == MainNavigation.Profile) {
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

//
//
//@Composable
//fun BottomNavigationBar(navController: NavController, courseId: String) {
//    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
//
//    val containerColor = MaterialTheme.colorScheme.surface
//    val contentColor = MaterialTheme.colorScheme.onSurface
//    val indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
//    AnimatedBottomBar(
//        selectedItem = selectedItem,
//        itemSize = navigationItems.take(3).size,
//        containerColor = Color.LightGray,
//        indicatorStyle = IndicatorStyle.LINE
//    ) {
//        navigationItems.forEachIndexed { index, navigationItem ->
//            BottomBarItem(
//                selected = currentRoute == navigationItem.route,
//                onClick = {
//                    if (currentRoute != navigationItem.route) {
//                        selectedItem = index
//                        // ... Navigation Stuff
//                    }
//                },
//                imageVector = navigationItem.icon,
//                label = navigationItem.title,
//                containerColor = Color.Transparent
//            )
//        }
//    }
//
//    NavigationBar(
//        containerColor = containerColor,
//        tonalElevation = 10.dp,
//    ) {
//        BottomNavItem(
//            route = "${Screen.Dashboard.route}/$courseId",
//            icon = Icons.Default.Home,
//            label = "Home",
//            navController = navController,
//            currentRoute = currentDestination,
//            indicatorColor = indicatorColor,
//            contentColor = contentColor
//        )
//        BottomNavItem(
//            route = "${Screen.Profile.route}/$courseId",
//            icon = Icons.Default.Person,
//            label = "Profile",
//            navController = navController,
//            currentRoute = currentDestination,
//            indicatorColor = indicatorColor,
//            contentColor = contentColor
//        )
//        BottomNavItem(
//            route = Screen.Chat.route,
//            icon = Icons.Default.Chat,
//            label = "Chat",
//            navController = navController,
//            currentRoute = currentDestination,
//            indicatorColor = indicatorColor,
//            contentColor = contentColor
//        )
//        BottomNavItem(
//            route = Screen.Leaderboard.route,
//            icon = Icons.Default.Leaderboard,
//            label = "Leaderboard",
//            navController = navController,
//            currentRoute = currentDestination,
//            indicatorColor = indicatorColor,
//            contentColor = contentColor
//        )
//    }
//}
//
//
//@Composable
//private fun RowScope.BottomNavItem(
//    route: String,
//    icon: ImageVector,
//    label: String,
//    navController: NavController,
//    currentRoute: String?,
//    indicatorColor: Color,
//    contentColor: Color
//) {
//    val isSelected = currentRoute?.startsWith(route) == true
//
//    val backgroundColor by animateColorAsState(
//        targetValue = if (isSelected) indicatorColor else Color.Transparent,
//        animationSpec = tween(durationMillis = 300), label = ""
//    )
//
//    val iconScale by animateFloatAsState(
//        targetValue = if (isSelected) 1.15f else 1f,
//        animationSpec = tween(durationMillis = 300), label = ""
//    )
//
//    NavigationBarItem(
//        selected = isSelected,
//        onClick = {
//            if (!isSelected) {
//                navController.navigate(route) {
//                    launchSingleTop = true
//                    restoreState = true
//                }
//            }
//        },
//        icon = {
//            Box(
//                modifier = Modifier
//                    .size(36.dp)
//                    .scale(iconScale)
//                    .background(
//                        color = backgroundColor,
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                    .then(
//                        if (isSelected) Modifier
//                            .border(
//                                width = 1.dp,
//                                color = MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .shadow(
//                                elevation = 4.dp,
//                                shape = RoundedCornerShape(8.dp),
//                                clip = false
//                            )
//                        else Modifier
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = label,
//                    tint = if (isSelected) MaterialTheme.colorScheme.primary else contentColor
//                )
//            }
//        },
//        label = {
//            Text(
//                text = label,
//                color = if (isSelected) MaterialTheme.colorScheme.primary else contentColor,
//                style = MaterialTheme.typography.labelSmall
//            )
//        },
//        alwaysShowLabel = true
//    )
//}


