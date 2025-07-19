package com.umt.memingoapp.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.umt.memingoapp.presentation.shared_components.AppTypography

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = LogoColor,
    secondary = LogoColor.copy(alpha = 0.8f),
    tertiary = LogoColor.copy(alpha = 0.6f),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF333333),
    onSurface = Color(0xFF333333),
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color(0xFF666666),
    outline = Color(0xFFDDDDDD),
    outlineVariant = LogoColor.copy(alpha = 0.5f)
)

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = LogoColor,
    secondary = LogoColor.copy(alpha = 0.8f),
    tertiary = LogoColor.copy(alpha = 0.6f),
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFA0A0A0),
    outline = Color(0xFF444444),
    outlineVariant = LogoColor.copy(alpha = 0.5f)
)

@Composable
fun MemingoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}