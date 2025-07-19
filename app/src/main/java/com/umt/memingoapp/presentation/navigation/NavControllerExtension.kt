package com.umt.memingoapp.presentation.navigation


import androidx.navigation.NavController

fun NavController.navigateAndClearStack(
    destination: String,
    clearUpTo: String
) {
    this.navigate(destination) {
        popUpTo(clearUpTo) { inclusive = true }
        launchSingleTop = true
    }
}