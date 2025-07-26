package com.umt.memingoapp.presentation.chatScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.presentation.theme.LogoColor

@Composable
fun MessageBubble(
    text: String,
    isUser: Boolean,
    courseLanguage: String = "German" // For showing "Translated from ..."
) {
    var showTranslationDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 0.dp,
                            bottomEnd = if (isUser) 0.dp else 16.dp
                        )
                    )
                    .background(if (isUser) LogoColor else Color.Gray.copy(alpha = 0.2f))
                    .padding(12.dp)
            ) {
                Text(
                    text = text,
                    color = if (isUser) Color.White else Color.White,
                    fontSize = 16.sp
                )
            }

            // Show translate icon for AI messages
            if (!isUser) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Translate",
                    tint = LogoColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showTranslationDialog = true }
                )
            }
        }
    }

    // Translation dialog
    if (showTranslationDialog) {
        AlertDialog(
            onDismissRequest = { showTranslationDialog = false },
            title = { Text("Translation") },
            text = {
                Column {
                    Text("Original: $text", fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Translated: ${translateToEnglish(text)}", fontSize = 16.sp, color = LogoColor)
                    Spacer(Modifier.height(8.dp))
                    Text("Translated from $courseLanguage", fontSize = 12.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(onClick = { showTranslationDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

// Dummy translator function (replace with real API)
fun translateToEnglish(text: String): String {
    return "This is the English translation."
}
