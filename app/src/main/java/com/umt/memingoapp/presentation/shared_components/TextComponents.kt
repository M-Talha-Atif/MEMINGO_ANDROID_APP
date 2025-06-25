package com.umt.memingoapp.presentation.shared_components

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.R

// ✅ 1. Define Google Font Provider
val googleFontProvider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// ✅ 2. Load Noto Sans with Provider
val notoSansFont = FontFamily(
    Font(
        googleFont = GoogleFont("Noto Sans"),
        weight = FontWeight.Light,
        fontProvider = googleFontProvider
    ),
    Font(
        googleFont = GoogleFont("Noto Sans"),
        weight = FontWeight.Normal,
        fontProvider = googleFontProvider
    ),
    Font(
        googleFont = GoogleFont("Noto Sans"),
        weight = FontWeight.Medium,
        fontProvider = googleFontProvider
    ),
    Font(
        googleFont = GoogleFont("Noto Sans"),
        weight = FontWeight.Bold,
        fontProvider = googleFontProvider
    ),
    Font(
        googleFont = GoogleFont("Noto Sans"),
        weight = FontWeight.ExtraBold,
        fontProvider = googleFontProvider
    )
)

// ✅ 3. Define typography using Noto Sans
val AppTypography = Typography(
    displayLarge = TextStyle( // H1
        fontFamily = notoSansFont,
        fontSize = 34.sp,
        fontWeight = FontWeight.ExtraBold
    ),
    displayMedium = TextStyle( // H2
        fontFamily = notoSansFont,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = TextStyle( // H3
        fontFamily = notoSansFont,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium
    ),

    titleLarge = TextStyle( // Title 1
        fontFamily = notoSansFont,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle( // Title 2
        fontFamily = notoSansFont,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = TextStyle( // Title 3
        fontFamily = notoSansFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    ),

    bodyLarge = TextStyle( // Large Body
        fontFamily = notoSansFont,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle( // Medium Body
        fontFamily = notoSansFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle( // Small Body
        fontFamily = notoSansFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light
    ),

    labelLarge = TextStyle( // Button Text
        fontFamily = notoSansFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    labelSmall = TextStyle( // Caption
        fontFamily = notoSansFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light
    )
)
