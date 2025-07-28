package com.umt.memingoapp.presentation.chatScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.data.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: GeminiRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Pair<String, Boolean>>>(emptyList())
    val messages: StateFlow<List<Pair<String, Boolean>>> = _messages

    fun loadStarterMessage(course: String) {
        viewModelScope.launch {
            val starter = repository.generateStarterMessage(course)
            _messages.value = _messages.value + (starter to false)
        }
    }

    fun sendMessage(userMessage: String, course: String) {
        if (userMessage.isBlank()) return
        _messages.value = _messages.value + (userMessage to true)

        viewModelScope.launch {
            val response = repository.generateTutorResponse(userMessage, course)
            _messages.value = _messages.value + (response to false)
        }
    }

    private val _translation = MutableStateFlow<String?>(null)
    val translation: StateFlow<String?> = _translation

    fun translateMessage(text: String, sourceLang: String) {
        viewModelScope.launch {
            val translated = repository.translateToEnglish(text, sourceLang)
            _translation.value = translated
        }
    }
}
