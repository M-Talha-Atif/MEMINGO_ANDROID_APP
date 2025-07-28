package com.umt.memingoapp.presentation.augmentedReality.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.domain.model.uiModels.WordLearningContent
import com.umt.memingoapp.data.repository.GeminiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ARLearningViewModel(
    private val geminiRepository: GeminiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ARLearningUiState>(ARLearningUiState.Loading)
    val uiState: StateFlow<ARLearningUiState> = _uiState.asStateFlow()

    private var currentJob: Job? = null

    fun generateContent(word: String, language: String) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _uiState.value = ARLearningUiState.Loading
            try {
                val content = geminiRepository.generateWordContent(word, language)
                _uiState.value = ARLearningUiState.Success(content)
            } catch (e: Exception) {
                _uiState.value = ARLearningUiState.Error(e.message ?: "Unknown error")
            }
        }
    }


    fun cleanupResources() {
        currentJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cleanupResources()
    }
}

sealed class ARLearningUiState {
    object Loading : ARLearningUiState()
    data class Success(val content: WordLearningContent) : ARLearningUiState()
    data class Error(val message: String) : ARLearningUiState()
}