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
import androidx.hilt.navigation.compose.hiltViewModel
import com.umt.memingoapp.presentation.chatScreen.viewmodel.ChatViewModel
import com.umt.memingoapp.presentation.theme.LogoColor




@Composable
fun MessageBubble(
    text: String,
    isUser: Boolean,
    courseLanguage: String = "German",
    viewModel: ChatViewModel = hiltViewModel()
) {
    var showTranslationDialog by remember { mutableStateOf(false) }
    val translation by viewModel.translation.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 0.dp,
                        bottomEnd = if (isUser) 0.dp else 16.dp
                    )
                )
                .background(if (isUser) LogoColor else Color.Gray.copy(alpha = 0.2f))
                .clickable {
                    viewModel.translateMessage(text, courseLanguage)
                    showTranslationDialog = true
                }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                color = if (isUser) Color.White else Color.Black,
                fontSize = 16.sp
            )

            if (!isUser) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Translate",
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .background(LogoColor, shape = RoundedCornerShape(50))
                        .padding(2.dp)
                        .clickable {
                            viewModel.translateMessage(text, courseLanguage)
                            showTranslationDialog = true
                        }
                )
            }
        }
    }

    if (showTranslationDialog) {
        AlertDialog(
            onDismissRequest = { showTranslationDialog = false },
            title = { Text("Translation") },
            text = {
                Column {
                    Text("Original: $text", fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Translated: ${translation ?: "Translating..."}",
                        fontSize = 16.sp,
                        color = LogoColor
                    )
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
