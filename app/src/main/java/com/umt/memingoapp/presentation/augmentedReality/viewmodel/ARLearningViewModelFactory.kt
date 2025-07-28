package com.umt.memingoapp.presentation.augmentedReality.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umt.memingoapp.data.repository.GeminiRepository

class ARLearningViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ARLearningViewModel::class.java)) {
            val apiKey = "AIzaSyDwFEkSwh7qD2-5NUsGMUOReDow6xzvxSc"
            return ARLearningViewModel(GeminiRepository(apiKey)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}