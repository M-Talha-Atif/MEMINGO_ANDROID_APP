package com.umt.memingoapp.presentation.chatScreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.umt.memingoapp.R
import com.umt.memingoapp.presentation.chatScreen.components.ChatInput
import com.umt.memingoapp.presentation.chatScreen.components.ChatTopBar
import com.umt.memingoapp.presentation.chatScreen.components.MessageBubble
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavController,
    navBack: () -> Unit,
    onMenuClick: () -> Unit
) {
    var messages by remember { mutableStateOf(listOf<Pair<String, Boolean>>()) }
    var currentMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()




    Scaffold(
        topBar = { ChatTopBar(R.drawable.av5, onBack = navBack, onMenu = onMenuClick) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Messages list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    reverseLayout = true // reverses the direction of scrolling bottom-up
                ) {
                    items(messages.reversed()) { (text, isUser) ->
                        MessageBubble(text = text, isUser = isUser)
                    }
                }

                // Input (stays visible with keyboard)
                ChatInput(
                    message = currentMessage,
                    onMessageChange = { currentMessage = it },
                    onSend = {
                        if (currentMessage.isNotBlank()) {
                            val userMsg = currentMessage
                            messages = messages + (userMsg to true)
                            currentMessage = ""

                            scope.launch {
                                delay(1000)
                                val dummyResponses = listOf(
                                    "Hola! ¿Cómo puedo ayudarte hoy?",
                                    "Hallo! Lass uns anfangen.",
                                    "Bonjour! Prêt à apprendre?"
                                )
                                messages = messages + (dummyResponses.random() to false)
                            }
                        }
                    },
                    onVoice = { /* TODO: Voice */ },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                )
            }
        }
    }
}


