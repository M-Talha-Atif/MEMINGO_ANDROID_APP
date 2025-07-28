package com.umt.memingoapp.presentation.chatScreen.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.umt.memingoapp.R
import com.umt.memingoapp.presentation.chatScreen.components.ChatInput
import com.umt.memingoapp.presentation.chatScreen.components.ChatTopBar
import com.umt.memingoapp.presentation.chatScreen.components.MessageBubble
import com.umt.memingoapp.presentation.chatScreen.viewmodel.ChatViewModel


@Composable
fun ChatScreen(
    navController: NavController,
    courseId: String,
    navBack: () -> Unit,
    onMenuClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    var currentMessage by remember { mutableStateOf("") }

    Log.d("Course ID", courseId)

    // Load initial message
    LaunchedEffect(courseId) {
        viewModel.loadStarterMessage(courseId)
    }

    Scaffold(
        topBar = { ChatTopBar(R.drawable.av5, onBack = navBack, onMenu = onMenuClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { (text, isUser) ->
                    MessageBubble(text = text, isUser = isUser)
                }
            }

            ChatInput(
                message = currentMessage,
                onMessageChange = { currentMessage = it },
                onSend = {
                    viewModel.sendMessage(currentMessage, courseId)
                    currentMessage = ""
                },
                onVoice = { /* TODO */ },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}



