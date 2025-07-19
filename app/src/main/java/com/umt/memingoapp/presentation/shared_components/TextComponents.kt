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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

// Define Google Font Provider
val googleFontProvider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Load Noto Sans with Provider
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

// Define typography using Noto Sans
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



@Composable
fun FontTextFieldPreview(
    label: String,
    textStyle: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier
) {
    var value by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(label, style = textStyle) },
        textStyle = textStyle,
        modifier = modifier
    )
}

@Composable
fun AllTextFieldPreviews(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("TextFields with Different Font Styles", style = AppTypography.displayMedium)
        FontTextFieldPreview(
            label = "Display Large (H1)",
            textStyle = AppTypography.displayLarge
        )
        FontTextFieldPreview(
            label = "Display Medium (H2)",
            textStyle = AppTypography.displayMedium
        )
        FontTextFieldPreview(
            label = "Display Small (H3)",
            textStyle = AppTypography.displaySmall
        )
        FontTextFieldPreview(
            label = "Title Large",
            textStyle = AppTypography.titleLarge
        )
        FontTextFieldPreview(
            label = "Title Medium",
            textStyle = AppTypography.titleMedium
        )
        FontTextFieldPreview(
            label = "Title Small",
            textStyle = AppTypography.titleSmall
        )
        FontTextFieldPreview(
            label = "Body Large",
            textStyle = AppTypography.bodyLarge
        )
        FontTextFieldPreview(
            label = "Body Medium",
            textStyle = AppTypography.bodyMedium
        )
        FontTextFieldPreview(
            label = "Body Small",
            textStyle = AppTypography.bodySmall
        )
        FontTextFieldPreview(
            label = "Label Large (Button)",
            textStyle = AppTypography.labelLarge
        )
        FontTextFieldPreview(
            label = "Label Small (Caption)",
            textStyle = AppTypography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAllTextFieldPreviews() {
    MaterialTheme(
        typography = AppTypography
    ) {
        Surface {
            AllTextFieldPreviews()
        }
    }
}
