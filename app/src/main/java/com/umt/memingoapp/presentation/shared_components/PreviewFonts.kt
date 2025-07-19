package com.umt.memingoapp.presentation.shared_components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun FontShowcaseScreen() {
    val fonts = listOf(
        "Noto Sans",
        "Lobster",
        "Poppins",
        "Playfair Display",
        "Roboto Slab"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(fonts) { fontName ->
            val fontFamily = FontFamily(
                Font(
                    googleFont = GoogleFont(fontName),
                    fontProvider = googleFontProvider
                )
            )
            FontSection(fontName = fontName, fontFamily = fontFamily)
        }
    }
}

@Composable
fun FontSection(fontName: String, fontFamily: FontFamily) {
    val headingStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily)
    val bodyStyle = TextStyle(fontSize = 16.sp, fontFamily = fontFamily)
    val labelStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = fontFamily)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Font: $fontName",
            style = headingStyle,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text("This is a Heading", style = headingStyle)
        Spacer(Modifier.height(4.dp))

        Text("This is a body text demonstrating paragraph style.", style = bodyStyle)
        Spacer(Modifier.height(4.dp))

        Text("Label / Caption", style = labelStyle)
        Spacer(Modifier.height(8.dp))

        var value by remember { mutableStateOf(TextFieldValue()) }
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Edit Text", style = labelStyle) },
            textStyle = bodyStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFontShowcaseScreen() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FontShowcaseScreen()
        }
    }
}
